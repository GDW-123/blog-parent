package com.mia.handler;

import com.alibaba.fastjson.JSON;
import com.mia.dao.pojo.SysUser;
import com.mia.service.LoginService;
import com.mia.util.UserThreadLocal;
import com.mia.vo.ErrorCode;
import com.mia.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 21:18
 */

/**
 * 每次访问需要登录的资源的时候，都需要在代码中进行判断，一旦登录的逻辑有所改变，代码都得进行变动，非常不合适。
 * 那么可不可以统一进行登录判断呢？
 * 可以，使用拦截器，进行登录拦截，如果遇到需要登录才能访问的接口，如果未登录，拦截器直接返回，并跳转登录页面
 */

/**
 * 拦截器的概念：
 * 在执行某段程序之前会执行该拦截器中的程序，只用当拦截器中的程序的返回结果是true，那么就表示放行，就可以执行该段程序，否则就会被拦截掉
 *  过滤器可以简单的理解为“取你所想取”，过滤器关注的是web请求；拦截器可以简单的理解为“拒你所想拒”，拦截器关注的是方法调用
 */

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    /**
     * 在执行某段程序之前进行拦截
     * 配置拦截规则
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行controller方法(Handler)之前进行执行
        /**
         * 1，需要判断 请求的接口路径 是否为HandlerMethod(controller方法)
         * 2，判断 token是否为空，如果为空，未登录
         * 3，如果token 不为空，登录验证 loginService checkToken
         * 4，如果认证成功，放行即可
         */

        if(!(handler instanceof HandlerMethod)){
            //表示如果访问的不是controller的方法，那么就不需要进行拦截，直接放行
            //比如说时访问静态资源 默认去classpath下的static 目录下去查询
            return true;
        }
        //获取token
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        //如果该token为空，则表示未登录，会跳转到登陆页面
        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //如果获取不到该token，也表示未登录，会跳转到登陆页面
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //登录验证成功，放行
        //我希望在controller中 直接获取用户的信息怎么获取?
        //吧用户信息放在ThreadLocal中，这样这个线程就可以在自己的工作内存中拿到这个变量
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除 ThreadLocal中用完的信息 会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
