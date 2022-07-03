package com.mia.dao.pojo;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:14
 */
import lombok.Data;

//对应的是ms_sys_user表
@Data
public class SysUser {

    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 是否是管理员
     */
    private Integer admin;

    /**
     * 头像（一个头像的路径）
     */
    private String avatar;

    /**
     * 注册日期
     */
    private Long createDate;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 最后一次登录的时间
     */
    private Long lastLogin;

    /**
     * 电话
     */
    private String mobilePhoneNumber;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐（用于对密码进行加密处理）
     */
    private String salt;

    /**
     * 登录状态
     */
    private String status;
}
