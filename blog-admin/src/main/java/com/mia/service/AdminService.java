package com.mia.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mia.mapper.AdminMapper;
import com.mia.mapper.PermissionMapper;
import com.mia.pojo.Admin;
import com.mia.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author GuoDingWei
 * @Date 2022/5/12 21:19
 */

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 通过用户名来查询用户
     * @param username 用户名
     * @return 用户信息
     */
    public Admin findAdminByUserName(String username){
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.last("limit 1");
        Admin adminUser = adminMapper.selectOne(queryWrapper);
        return adminUser;
    }

    /**
     * 通过用户id查询用户的权限
     * @param adminId
     * @return
     */
    public List<Permission> findPermissionsByAdminId(Long adminId){
        return permissionMapper.findPermissionsByAdminId(adminId);
    }
}

