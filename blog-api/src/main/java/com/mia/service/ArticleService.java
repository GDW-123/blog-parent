package com.mia.service;

import com.mia.vo.Result;
import com.mia.vo.params.ArticleParam;
import com.mia.vo.params.PageParams;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:34
 */
public interface ArticleService {

    /**
     *分页查询文章列表
     * @param pageParams
     * @return
     * */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文档
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文档
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 通过id查询文章
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
