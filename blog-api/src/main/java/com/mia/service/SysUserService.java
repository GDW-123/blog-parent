package com.mia.service;

import com.mia.dao.pojo.SysUser;
import com.mia.vo.Result;
import com.mia.vo.UserVo;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:56
 */
public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findSysUserById(Long authorId);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 注册成功时保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);
}
