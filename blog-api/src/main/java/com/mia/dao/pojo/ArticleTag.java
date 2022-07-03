package com.mia.dao.pojo;

import lombok.Data;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 22:36
 */

@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
