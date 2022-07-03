package com.mia.service;

import com.mia.vo.Result;
import com.mia.vo.params.CommentParam;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 12:49
 */
public interface CommentsService {

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    /**
     * 评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);

}
