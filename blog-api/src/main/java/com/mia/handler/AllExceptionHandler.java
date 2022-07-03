package com.mia.handler;

import com.mia.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 16:29
 */

/**
 * 不管是controller层还是service，dao层，都有可能报异常，如果是预料中的异常，可以直接捕获处理，
 * 如果是意料之外的异常，需要统一进行处理，进行记录，并给用户提示相对比较友好的信息。
 */
//对加了@Controller注解的方法进行拦截处理 AOP的实现
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json数据
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
