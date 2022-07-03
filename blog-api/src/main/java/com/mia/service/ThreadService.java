package com.mia.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mia.dao.mapper.ArticleMapper;
import com.mia.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 11:16
 */
@Component
public class ThreadService {

    /**
     * 实现一个多线程下的文章访问量的更新
     * Async实现异步调用，这里的taskExecutor就是创建的bean
     * @param articleMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        //首先获取当前的次数
        int viewCount = article.getViewCounts();
        Article articleUpdate = new Article();
        //然后将该次数加一
        articleUpdate.setViewCounts(viewCount + 1);
        //定义规则进行修改
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下 线程安全
        updateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}