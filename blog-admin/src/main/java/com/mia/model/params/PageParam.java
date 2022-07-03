package com.mia.model.params;


import lombok.Data;

/**
 * @Author GuoDingWei
 * @Date 2022/5/12 20:22
 */

@Data
public class PageParam {

    //当前页
    private Integer currentPage;

    //每页显示的数量
    private Integer pageSize;

    private String queryString;
}
