package com.mia.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 11:08
 */
public class HttpContextUtils {

    public static HttpServletRequest getHttpServletRequest(){

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
