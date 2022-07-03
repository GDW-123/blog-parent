package com.mia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mia.dao.mapper.SysUserMapper;
import com.mia.dao.pojo.SysUser;
import com.mia.service.LoginService;
import com.mia.service.SysUserService;
import com.mia.vo.ErrorCode;
import com.mia.vo.LoginUserVo;
import com.mia.vo.Result;
import com.mia.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * @Author GuoDingWei
 * @Date 2022/5/10 14:57
 */

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private LoginService loginService;

    /**
     * 根据评论中的作者id查询作者的信息
     * @param id
     * @return
     */
    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        //如果没有查到的话，就使用默认的用户信息
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("郭定伟");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    /**
     * 根据id查询用户的信息
     * @param authorId
     * @return
     */
    @Override
    public SysUser findSysUserById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        //如果查不到就使用一个默认值
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("郭定伟");
        }
        return sysUser;
    }

    /**
     * 根据用户账号和密码查找这个用户
     * @param account  账号
     * @param password 密码
     * @return 返回这个用户对象
     */
    @Override
    public SysUser findUser(String account, String password) {
        //mybatis-plus的自定义查找规则
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //查找条件是传入的参数和表中的数据相等
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        //根据伤处的条件查找下列的属性
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        //一般用户名都不会重复的，所以只需要查找一条即可
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    @Override
    public Result findUserByToken(String token) {
        /**
         * 1,token合法性校验
         *   是否为空，解析是否成功，redis是否存在
         * 2，如果校验失败，返回错误
         * 3，如果成功，返回对应的结果 LoginUserVo
         */
        //对token进行合法性校验
        SysUser sysUser = loginService.checkToken(token);
        //如果值为null，则表示不存在该用户的信息，返回获取失败
        if(sysUser == null){
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        //如果找到了,就将该用户对象返回
        LoginUserVo loginUserVo = new LoginUserVo();
        //避免精度损失，因此所有的id在进行封装的时候都被设置成了String类型了
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

    /**
     * 根据账号查询用户
     * @param account 传入的账号信息
     * @return 查询到的用户信息
     */
    @Override
    public SysUser findUserByAccount(String account) {
        //自定义规则
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //用户传入的account来进行比较
        queryWrapper.eq(SysUser::getAccount,account);
        //取第一条
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 保存用户注册的信息
     * @param sysUser 将用户注册的信息保存到数据库中
     */
    @Override
    public void save(SysUser sysUser) {
        //注意 默认生成的id 是分布式id 采用了雪花算法
        /**
         * 雪花算法：生成的ID是一个64 bit的long型的数字且按时间趋势递增。大致由首位无效符、时间戳差值、机器编码，序列号四部分组成
         * 作用：确保生成的ID是唯一的
         * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 0000000000 - 000000000000
         * 符号位（1）                时间戳（41）          机器码（10）            序列号（12）
         * 时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截) * 得到的值）
         * 优点：
         *      （1）能满足高并发分布式系统环境下ID不重复
         *      （2）基于时间戳，可以保证基本有序递增
         *      （3）不依赖第三方的库或者中间件
         *      （4）生成效率极高
         */
        this.sysUserMapper.insert(sysUser);
    }
}
