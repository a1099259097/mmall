package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceRespond;
import com.mmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServiceRespond searchProduct(Integer categoryId, String keyword,
                                        @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return productService.getProductByKeywordCategory(categoryId, keyword, pageNum, pageSize, keyword);

    }

    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServiceRespond getProductDetail(Integer productId){
        return productService.managerProductDetail(productId);
    }


}
