package com.mia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 19:59
 */
//涉及到的表有ms_admin，ms_permission，ms_admin_permission
@SpringBootApplication
public class AdminApp {

    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class,args);
    }
}
