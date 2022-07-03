package com.mia.util;

import com.mia.dao.pojo.SysUser;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 22:09
 */

/**
 * 本地线程变量
 * 在Java的多线程编程中，为保证多个线程对共享变量的安全访问，通常会使用synchronized来保证同一时刻只有一个线程对共享变量进行操作。
 * 这种情况下可以将类变量放到ThreadLocal类型的对象中，使变量在每个线程中都有独立拷贝，不会出现一个线程读取变量时而被另一个线程修改的现象
 */
public class UserThreadLocal {

    private UserThreadLocal(){

    }

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
