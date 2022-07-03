package com.mia.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    //是否成功
    private boolean success;

    //成功或失败的编码
    private Integer code;

    //成功或失败的信息
    private String msg;

    //成功时的数据，失败的时候就是null
    private Object data;

    //表示成功的情况
    public static Result success(Object data) {
        return new Result(true,200,"success",data);
    }
    //表示失败的情况
    public static Result fail(Integer code, String msg) {
        return new Result(false,code,msg,null);
    }
}
