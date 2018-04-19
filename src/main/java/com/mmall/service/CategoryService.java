package com.mmall.service;

import com.mmall.common.ServiceRespond;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    ServiceRespond<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServiceRespond addCategory(Integer parentId,String categoryName);
    ServiceRespond setCategoryName (Integer categoryId, String categoryName);
    ServiceRespond<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
