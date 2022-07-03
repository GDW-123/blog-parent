package com.mia.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mia.dao.pojo.Tag;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 13:22
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);


    /**
     * 查询最热的标签，前limit条
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    /**
     * 通过上一步获得的id来查询具体的标签对象
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
