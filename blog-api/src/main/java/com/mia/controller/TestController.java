package com.mia.controller;

import com.mia.dao.pojo.SysUser;
import com.mia.util.UserThreadLocal;
import com.mia.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 22:04
 */

//测试
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        //SysUser
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
