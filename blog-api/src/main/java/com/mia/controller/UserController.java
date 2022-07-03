package com.mia.controller;


import com.mia.service.SysUserService;
import com.mia.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 20:24
 */
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户信息
     * Authorization：HTTP协议中的 Authorization 请求消息头含有服务器用于验证用户代理身份的凭证，
     *                通常会在服务器返回401 Unauthorized 状态码以及WWW-Authenticate 消息头之后在后续请求中发送此消息头。
     * @param token 通过在登录的时候创建的token来获取用户的信息
     * @return
     */
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){

        return sysUserService.findUserByToken(token);
    }
}
