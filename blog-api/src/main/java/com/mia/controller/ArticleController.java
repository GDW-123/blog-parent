package com.mia.controller;

import com.mia.common.aop.LogAnnotation;
import com.mia.common.cache.Cache;
import com.mia.service.ArticleService;
import com.mia.vo.Result;
import com.mia.vo.params.ArticleParam;
import com.mia.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:23
 */

//@RestController相当于Controller＋ResponseBody
@RestController
//处理请求映射
@RequestMapping("articles")
public class ArticleController {

    //依赖注入
    @Autowired
    private ArticleService articleService;

    /**
     * 在首先显示文章
     * @param pageParams
     * @return
     */
    //Result是统一结果返回
    @PostMapping
    //加上此注解，代表对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    //表示缓存，即在expire的时间以后才会进行显示
    @Cache(expire = 5 * 60 * 1000,name = "articles")
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据,一般都用POST方式进行提交
    //这里涉及到三张表ms_article（文章列表），ms_tag（标签），ms_sys_user（）
    public Result articles(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 文章归档
     * 文章归档需要用到时间，因此需要创建一个区别于pojo的实体类dos来在sql语句中进行时间的计算
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 通过id来查询文章，即文章详情页面
     * 涉及到的表有 ms_article_body  ms_category
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
