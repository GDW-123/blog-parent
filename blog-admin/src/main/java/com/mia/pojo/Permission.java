package com.mia.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 20:21
 */

@Data
public class Permission {

    //自增
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String description;
}

