package com.mia.service;

import com.mia.vo.CategoryVo;
import com.mia.vo.Result;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 10:20
 */
public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
