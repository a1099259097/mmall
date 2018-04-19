package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.CategoryService;
import com.mmall.service.ProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    public ServiceRespond saveOrUpdate(Product product){
        if (product == null) {
            return ServiceRespond.createByErrorMessage("新增或更新产品参数不正确");
        }
        if (StringUtils.isNotBlank(product.getSubImages())){
            String[] subImagesArray = product.getSubImages().split(",");
            if (subImagesArray.length > 0){
                product.setMainImage(subImagesArray[0]);
            }
        }
        if (product.getId() != null) {
            //update product
            int resultCount = productMapper.updateByPrimaryKey(product);
            if (resultCount > 0) {
                return ServiceRespond.createBySuccessMessage("update successful");
            }else {
                return ServiceRespond.createByErrorMessage("update error");
            }
        }else {
            int rowCount = productMapper.insert(product);
            if (rowCount > 0) {
                return ServiceRespond.createBySuccessMessage("save successful");
            }else {
                return ServiceRespond.createByErrorMessage("save error");
            }
        }
    }

    @Override
    public ServiceRespond<String> setSaleStatus(Integer productId,Integer status) {
        if (productId == null || status == null) {
            return ServiceRespond.createByErrorMessage("parameter error");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServiceRespond.createBySuccess("修改产品状态成功");
        }else {
            return ServiceRespond.createByErrorMessage("修改产品状态失败");
        }
    }

    public ServiceRespond<ProductDetailVo> managerProductDetail(Integer productId){
        if (productId == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceRespond.createByErrorMessage("产品不存在或已下架");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServiceRespond.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://192.168.25.4"));
        productDetailVo.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));
        return productDetailVo;

    }

    public ServiceRespond<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectAll();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productDetailVo = assembleProductListVo(product);
            productListVoList.add(productDetailVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceRespond.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setPrice(product.getPrice());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://192.168.25.4"));
        return productListVo;
    }

    public ServiceRespond<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVo.setStatus(null);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceRespond.createBySuccess(pageResult);
    }

    public ServiceRespond<PageInfo> getProductByKeywordCategory(Integer categoryId, String keyword,
                                                  Integer pageNum, Integer pageSize, String orderBy){
        if (StringUtils.isBlank(keyword) && categoryId == null){
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null && StringUtils.isBlank(keyword)) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum, pageSize);
                ArrayList<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo result = new PageInfo(productListVoList);
                return ServiceRespond.createBySuccess(result);
            }
            categoryIdList = categoryService.selectCategoryAndChildrenById(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuffer().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListVoOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServiceRespond.createBySuccess(pageInfo);
    }


}
