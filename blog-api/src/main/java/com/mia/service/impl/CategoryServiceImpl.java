package com.mia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mia.dao.mapper.CategoryMapper;
import com.mia.dao.pojo.Category;
import com.mia.service.CategoryService;
import com.mia.vo.CategoryVo;
import com.mia.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 10:21
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询分类
     * @param categoryId
     * @return
     */
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        //首先查询出这个分类
        Category category = categoryMapper.selectById(categoryId);
        //然后进行封装
        CategoryVo categoryVo = new CategoryVo();
        //拷贝
        BeanUtils.copyProperties(category,categoryVo);
        //还需要修改一个属性，之所以需要修改，是为了避免精度损失
        //封装类和实体类的区别就是id的类型不同
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    /**
     * 查询所有的标签
     * @param id
     * @return
     */
    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    /**
     * 所有文章的分类
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //根据id和分类的名称来进行查询
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }

    /**
     * 查询所有的文章分类
     * @return
     */
    @Override
    public Result findAllDetail() {
        //不需要自定义规则，查询全部的即可
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //页面交互的对象
        return Result.success(copyList(categories));
    }


    //对数据进行封装
    private Object copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        //为了防止精度损失，因此需要装换成String类型的
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

}
