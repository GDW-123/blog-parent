package com.mia.common.aop;

import java.lang.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 10:57
 */


/**
 * 自定义注解，使用Target（元注解）进行标注
 * 日志注解
 */

//Type代表可以放在类上面，Method代表可以放在方法上面

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    //定义属性，可以设置默认值
    String module() default "";

    String operator() default "";
}