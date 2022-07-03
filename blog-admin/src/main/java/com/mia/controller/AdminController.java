package com.mia.controller;

import com.mia.model.params.PageParam;
import com.mia.pojo.Permission;
import com.mia.service.PermissionService;
import com.mia.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 20:16
 */

//后台管理部分，要从http://localhost:8889/login.html进入
//权限管理，实现简单的增删查改，就是普通的三层架构的调用
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }
}

