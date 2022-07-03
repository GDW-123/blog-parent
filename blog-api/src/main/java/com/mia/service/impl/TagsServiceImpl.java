package com.mia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.mia.dao.mapper.TagMapper;
import com.mia.dao.pojo.Tag;
import com.mia.service.TagService;
import com.mia.vo.Result;
import com.mia.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/10 14:36
 */

@Service
public class TagsServiceImpl implements TagService {


    @Autowired
    private TagMapper tagMapper;

    /**
     * 通过id来查询文章的标签
     * @param articleId
     * @return
     */
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //MybatisPlus是无法进行多表查询的
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    //对数据进行一个封装
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        //id属性在tag类中是int类型的，但是在tagVo中是String类型的，这样做也是怕精度的损失
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }


    @Override
    public Result hots(int limit) {

        /**
         * 1，标签所拥有的文章的数量最多，就说明该标签时最热标签
         * 2，查询  根据tag_id 分组查询 技术，从大到小 排列 取前面limit个
         */

        //此时我们得到的是标签的id，但是要显示在页面上面的是标签对象
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        //此时我们需要根据标签的id来查询标签对象
        //如果查到的标签列表是空的，表示没有标签，即返回null
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        //通过标签名称查找标签对象
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    /**
     * 所有文章标签
     * @return
     */
    @Override
    public Result findAll() {
        //这里不需要自定义条件，查询出表中所有的数据即可
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    /**
     * 根据id查询标签文章列表
     * @param id
     * @return
     */
    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo copy = copy(tag);
        return Result.success(copy);
    }

}
