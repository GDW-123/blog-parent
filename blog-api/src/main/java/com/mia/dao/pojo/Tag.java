package com.mia.dao.pojo;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:17
 */
import lombok.Data;

//对应ms_tag表
@Data
public class Tag {

    private Long id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 标签名称
     */
    private String tagName;

}
