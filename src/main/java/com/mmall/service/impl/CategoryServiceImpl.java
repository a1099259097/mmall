package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServiceRespond;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServiceRespond<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前子节点");
        }
        return ServiceRespond.createBySuccess(categoryList);
    }

    public ServiceRespond addCategory(Integer parentId,String categoryName){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServiceRespond.createByErrorMessage("add category illegal error");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if (resultCount == 0) {
            return ServiceRespond.createByErrorMessage("add category error");
        }
        return ServiceRespond.createBySuccessMessage("add category successful");
    }

    public ServiceRespond setCategoryName (Integer categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCount == 0) {
            return ServiceRespond.createByErrorMessage("update error");
        }
        return ServiceRespond.createBySuccessMessage("update successful");
    }

    public ServiceRespond<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryList = Lists.newArrayList();

        if (categoryId != null) {
            for (Category categoryItem: categorySet) {
                categoryList.add(categoryItem.getId());
            }
        }
        return ServiceRespond.createBySuccess(categoryList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem: categoryList) {
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }

}
