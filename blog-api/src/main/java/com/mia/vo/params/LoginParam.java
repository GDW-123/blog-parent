package com.mia.vo.params;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 17:28
 */
import lombok.Data;

@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
