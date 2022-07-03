package com.mia.dao.pojo;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:13
 */
import lombok.Data;

//对应的是ms_article表
//Article中的commentCounts，viewCounts，weight 字段为int，会造成更新阅读次数的时候，将其余两个字段设为初始值0
@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    /**
     * 文章id
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String summary;

    /**
     * 文章的评论数
     */
    private Integer commentCounts;

    /**
     * 文章的查看次数
     */
    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight = Article_Common;


    /**
     * 创建时间
     */
    private Long createDate;
}

