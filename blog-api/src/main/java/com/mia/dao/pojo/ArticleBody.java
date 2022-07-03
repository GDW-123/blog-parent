package com.mia.dao.pojo;

import lombok.Data;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 22:18
 */

/**
 * 对应文章详情表中的数据
 */
@Data
public class ArticleBody {

    private Long id;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 用于网页显示的文章内容
     */
    private String contentHtml;

    /**
     * 文章id
     */
    private Long articleId;
}