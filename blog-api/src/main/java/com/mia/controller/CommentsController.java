package com.mia.controller;

import com.mia.service.CommentsService;
import com.mia.vo.Result;
import com.mia.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 12:47
 */
@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 评论列表
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    //@PathVariable()接收请求路径中占位符的值
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }

    /**
     * 评论
     * @RequestBody 接受前端传来的参数
     * @param commentParam
     * @return
     */
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
