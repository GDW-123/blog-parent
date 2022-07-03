package com.mia.vo.params;

import lombok.Data;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 22:01
 */


@Data
public class CommentParam {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
