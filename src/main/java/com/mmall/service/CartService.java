package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceRespond;
import com.mmall.vo.CartProductVo;

import java.util.List;

public interface CartService {

//    ServiceRespond<PageInfo> getCartList(Integer userId);

    ServiceRespond<List<CartProductVo>> add(Integer userId, Integer productId, Integer count);
}
