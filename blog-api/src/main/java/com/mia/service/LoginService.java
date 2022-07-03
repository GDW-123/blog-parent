package com.mia.service;

import com.mia.dao.pojo.SysUser;
import com.mia.vo.Result;
import com.mia.vo.params.LoginParam;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 17:26
 */

public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);

}
