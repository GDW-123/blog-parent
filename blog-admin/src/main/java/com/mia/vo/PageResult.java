package com.mia.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 20:20
 */

@Data
public class PageResult<T> {

    private List<T> list;

    private Long total;
}

