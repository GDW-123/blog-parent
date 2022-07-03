package com.mia.config;

import com.mia.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:05
 */

//配置拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    //登录拦截器，表示部分请求只有登陆以后才有权限进行访问
    public WebConfig(LoginInterceptor loginInterceptor){
        this.loginInterceptor = loginInterceptor;
    }

    //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致,本地测试 端口不一致 也算跨域
    //同源（即指在同一个域）就是两个页面具有相同的协议（protocol），主机（host）和端口号（port）
    //这里禁止使用.allowedOrigins("*")这种形式
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    //配置访问哪些路径需要实现拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*拦截test接口，后续实际遇到需要拦截的接口时，在配置为真正的拦截接口
        //registry.addInterceptor(loginInterceptor)
                  .addPathPatterns("/**")
                  .excludePathPatterns("/login")
                  .excludePathPatterns("/register")
        */
        //表示在访问以下路径的时候一定要登录
        registry.addInterceptor(loginInterceptor)
                //测试
                .addPathPatterns("/test")
                //表示评论的路径，只有登陆了以后才能进行评论
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
