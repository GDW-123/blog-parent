package com.mia.service.impl;

import com.alibaba.fastjson.JSON;
import com.mia.dao.pojo.SysUser;
import com.mia.service.LoginService;
import com.mia.service.SysUserService;
import com.mia.util.JWTUtils;
import com.mia.vo.ErrorCode;
import com.mia.vo.Result;
import com.mia.vo.params.LoginParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 17:29
 */
@Service
@Transactional  //加上事务，一般建议加在 接口上，通用一些，但是加在实现类上面也是可以的
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //盐，用于密码加密
    //破解md5加密算法的方法一般是存储常用的盐，因此设计的盐越复杂，被破解的概率就越低
    private static final String slat = "wenXY!@#";

    @Override
    public Result login(LoginParam loginParam) {

        /**
         * 1，检查参数是否合法
         * 2，根据用户名和密码去user表中查询，是否存在
         * 3，如果不存在，登陆失败
         * 4，如果存在，使用jwt 生成token 返回给前端
         * 5，token放入redis当中，redis  token：user信息，设置过期时间
         *    （登录认证的时候  先认证token字符串是否合法，去redis认证是否存在）
         */
        //获取账号和密码
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //判断账号或者密码是否为空，如果为空，则表示登录失败
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //加密
        //password += slat;

        //通过这个账号和密码找到这个用户
        SysUser sysUser = sysUserService.findUser(account,password);
        //如果找不到这个用户，就表示不存在该用户，登录失败
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //如果获取到了这个用户，就将这个用户id放到这个token令牌中，返回给客户端，
        //当客户端需要请求数据的时候，服务器就根据这个token来确认这个用户的身份
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    /**
     * 检查token是否合法
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {
        //如果是空的的话，那么就表示不合法
        if(StringUtils.isBlank(token)){
            return null;
        }
        //如果不存在这个token，也表示不合法
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
        //从Redis中取出键为token的值
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        //如果不存在该键，也表示是不合法的
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        //将json字符串转换成object类型
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /**
     * 退出登录
     * 删除掉登录的时候创建的token即可
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    /**
     * 用户注册
     * @param loginParam 注册需要用户填入的参数
     * @return 返回用户注册是否成功
     */
    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1，判断参数 是否合法
         * 2，判断账户是否存在，存在 返回账户已经被注册
         * 3，不存在，注册账户
         * 4，生成token
         * 5，存入redis 并返回
         * 6，注意，加上事务，一旦中间的任何过程出现问题，注册的用户需要回滚
         */
        //首先获取用户从前端传过来的数据
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        //只有这三个数据有一个为空，就表示注册失败
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //从数据库中根据账号查询数据，只要这个数据存在，就表示这个用户已经存在，不允许注册
        SysUser sysUser = this.sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //程序到这里，表示科技进行注册
        sysUser = new SysUser();
        sysUser.setNickname(nickname);//昵称
        sysUser.setAccount(account);//账号
        sysUser.setPassword(password);//密码
        sysUser.setCreateDate(System.currentTimeMillis());//创建时间
        sysUser.setLastLogin(System.currentTimeMillis());//最后一次登录时间
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");//头像
        sysUser.setAdmin(1); //1 为true 即表示是管理员
        sysUser.setDeleted(0); // 0 为false 即表示是否删除
        sysUser.setSalt(""); //设置加密盐
        sysUser.setStatus(""); //设置状态
        sysUser.setEmail(""); //这是email
        this.sysUserService.save(sysUser);//保存用户注册的信息

        //在进行注册的时候同时根据这个用户id来创建一个token
        String token = JWTUtils.createToken(sysUser.getId());
        //将这个token放到redis中，设置一个过期时间，这样避免了在获取用户信息的时候每次都是通过io进行拿取，很大程度提高了效率
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

}
