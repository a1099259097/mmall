package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceRespond;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface ProductService {


    ServiceRespond saveOrUpdate(Product product);
    ServiceRespond<String> setSaleStatus(Integer productId,Integer status);
    ServiceRespond<ProductDetailVo> managerProductDetail(Integer productId);
    ServiceRespond<PageInfo> getProductList(Integer pageNum, Integer pageSize);
    ServiceRespond<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);
    ServiceRespond<PageInfo> getProductByKeywordCategory(Integer categoryId, String keyword,
                                                         Integer pageNum, Integer pageSize, String orderBy);
}
