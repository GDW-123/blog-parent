package com.mia.service;

import com.mia.vo.Result;
import com.mia.vo.TagVo;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:56
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}

