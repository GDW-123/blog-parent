package com.mia.vo;

import lombok.Data;
import java.util.List;
/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:50
 */




@Data
public class ArticleVo {
    //一定要记得加 要不然会出现精度损失，当然也可以直接将id改成String类型
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;

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
     * 文章权重（用于文章是否置顶）
     */
    private int weight;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 作者id
     */
    private String author;

    /**
     * 文章主体部分
     */
    private ArticleBodyVo body;

    /**
     * 文章标签
     */
    private List<TagVo> tags;

    /**
     * 文章分类
     */
    private CategoryVo category;

}
