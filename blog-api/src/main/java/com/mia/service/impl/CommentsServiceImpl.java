package com.mia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mia.dao.mapper.CommentMapper;
import com.mia.dao.pojo.Comment;
import com.mia.dao.pojo.SysUser;
import com.mia.service.CommentsService;
import com.mia.service.SysUserService;
import com.mia.util.UserThreadLocal;
import com.mia.vo.CommentVo;
import com.mia.vo.Result;
import com.mia.vo.UserVo;
import com.mia.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 12:50
 */

@Service
public class CommentsServiceImpl implements CommentsService {


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据文章id查询评论
     * @param id 文章id
     * @return
     */
    @Override
    public Result commentsByArticleId(Long id) {

        /**
         * 1，根据文章id查询评论列表 从comment表中查询
         * 2，根据作者的id查询作者的信息
         * 3，判断  如果level = 1 要去查询它有没有子评论
         * 4，如果有 根据评论id 进行查询 (parent_id)
         */

        //自定义查询规则
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        //进行数据查询
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    //对数据进行封装
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    //将id改成String类型的，防止精度溢出的情况
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));

        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        //子评论
        Integer level = comment.getLevel();
        if(level == 1){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //给谁评论
        if(level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 如果level等于1，那么就表示还有则评论，根据父评论的id查询子评论
     * @param id 评论的id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        //当level等于2的时候就表示是子评论了
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

    /**
     * 评论
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        //将评论放入ThreadLocal中
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        //如果这条评论不是子评论就将level设置为1，是的话就设置为2
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
}
