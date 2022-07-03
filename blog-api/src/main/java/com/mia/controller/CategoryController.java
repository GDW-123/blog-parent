package com.mia.controller;

import com.mia.service.CategoryService;
import com.mia.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 22:17
 */
@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 所有文章分类
     * @return
     */
    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

    /**
     * 查询所有的文章分类
     * @return
     */
    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    /**
     * 查询所有的标签
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.categoriesDetailById(id);
    }
}