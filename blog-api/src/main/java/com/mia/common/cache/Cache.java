package com.mia.common.cache;


import java.lang.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 18:57
 */

//内存的访问速度 远远大于 磁盘的访问速度 （1000倍起）

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    //默认使用的是1分钟
    long expire() default 1 * 60 * 1000;

    String name() default "";

}