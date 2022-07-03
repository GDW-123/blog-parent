package com.mia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mia.dao.mapper.ArticleBodyMapper;
import com.mia.dao.mapper.ArticleMapper;
import com.mia.dao.dos.Archives;
import com.mia.dao.mapper.ArticleTagMapper;
import com.mia.dao.pojo.Article;
import com.mia.dao.pojo.ArticleBody;
import com.mia.dao.pojo.ArticleTag;
import com.mia.dao.pojo.SysUser;
import com.mia.service.*;
import com.mia.util.UserThreadLocal;
import com.mia.vo.ArticleBodyVo;
import com.mia.vo.ArticleVo;
import com.mia.vo.Result;
import com.mia.vo.TagVo;
import com.mia.vo.params.ArticleParam;
import com.mia.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:40
 */

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryService categoryService;


    /**
     * 分页查询article数据库
     * 实现首页中数据的展示
     * */

    @Override
    public Result listArticle(PageParams pageParams) {
        //分页查询
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

        IPage<Article> articleIPage = this.articleMapper.listArticle(page,
                                                                    pageParams.getCategoryId(),
                                                                    pageParams.getTagId(),
                                                                    pageParams.getYear(),
                                                                    pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }

    //由于很多地方都需要对数据进行封装，尽量不要在原数据中进行改动，避免以后有新的需求的时候出现不必要的错误
    //vo的作用就是方便在前端传入数据，现在习惯都是将前端的数据封装成一个类进行传递，因此创建一个新的vo类，将需要从页面获取的类放入到vo中
    //在不同的页面上面是显示不同的内容的，因此可以在需要该部分的时候写上true，
    //后序就可以从别的表上面查询到该数据，如果不需要的话，就改成false，表示不显示该部分的数据
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : records){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    //设置封装体中的数据
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        //将id转换成String类型，避免出现精度损失的情况
        articleVo.setId(String.valueOf(article.getId()));
        //使用工具类将article中的数据都复制过来
        BeanUtils.copyProperties(article,articleVo);
        //实现数据的扩充
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        //由于每个表都有一个id，所以我们可以通过id进行查询相应的数据
        //标签
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        //作者，最后只需要获取昵称即可
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findSysUserById(authorId).getNickname());
        }
        //主体内容
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        //分类
        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result hotArticle(int limit) {
        //LambdaQueryWrapper是mybatis plus中的一个条件构造器对象，只是是需要使用Lambda 语法使用 Wrapper
        //使用该类可以自定义规则
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //表示按照访问次数进行倒序排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //表示会按照id和title进行查询
        queryWrapper.select(Article::getId,Article::getTitle);
        //取前面几条数据
        queryWrapper.last("limit " + limit);
        //queryWrapper表示自定义的规则，下面按照该规则进行查询
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //最热文章只需要显示标题即可的，不需要显示标签和作者
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        //和最热文章一样，自定义规则
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //先根据发表的时间倒序排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        //然后通过文章id和文章的标题来进行查找
        queryWrapper.select(Article::getId,Article::getTitle);
        //最后显示最新的前几篇文章
        queryWrapper.last("limit "+limit);
        //规则定义好以就可以进行查询了
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //最新文章只需要显示标题即可的，不需要显示标签和作者
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1，根据id查询 文章信息
         * 2，根据bodyId 和categoryId去做关联查询
         * 主体内什么都是需要的
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);

        /**
         * 看完文章后增加阅读数
         * 但是在查看文章的同时做了一个更新的操作，在这个时候更新操作就要加写锁，阻塞其他的读的操作，性能就会降低
         * 更新增加了此次接口的耗时，一旦更新出现问题，不能影响查看文章的操作
         * 这个时候就需要使用线程池，把更新操作扔到主线程中，这个时候该操作就和主线程无关了
         */
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        //此接口  要加入到登录拦截当中
        SysUser sysUser = UserThreadLocal.get();

        /**
         * 1，发布文章 目的 构建Article对象
         * 2，作者id 当前的登录用户
         * 3，标签  要将标签加入到 关联列表中
         * 4，body 内容存储
         */

        //Article对象，为这个对象设置值
        Article article = new Article();
        article.setAuthorId(sysUser.getId());//作者id
        article.setWeight(Article.Article_Common);//权重（是否置顶）
        article.setViewCounts(0);//查看量（刚发布的时候肯定是0）
        article.setTitle(articleParam.getTitle());//文章标题
        article.setSummary(articleParam.getSummary());//文章简介
        article.setCommentCounts(0);//评论数（刚发布的时候肯定是0）
        article.setCreateDate(System.currentTimeMillis());//创建的时间
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        this.articleMapper.insert(article);

        //设置tags标签
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }

        //创建body主体内容
        //content与contentHtml的区别是放在页面上面的数据是有格式调整的
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        //根据这个id来修改article的数据
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }
}
