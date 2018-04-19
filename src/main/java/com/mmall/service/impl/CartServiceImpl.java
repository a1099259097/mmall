package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.CartService;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServiceRespond<List<CartProductVo>> add(Integer userId, Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceRespond.createByCodeError(ResponseCode.ERROR.getCode(),"this product is empty");
        }
        Cart cart = cartMapper.selectByProductId(productId);
        int quantity;
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setChecked(1);
            quantity = product.getStock();
            if (quantity >= count) {
                cart.setQuantity(count);
            }else {
                cart.setQuantity(count);
                product.setStock(0);
                productMapper.updateByPrimaryKeySelective(product);
            }
        }else {

        }

        int insertCount = cartMapper.insert(cart);
        return null;
    }


    public ServiceRespond<CartVo> getCartList(Integer userId) {
        CartVo cartVo = getCartVo(userId);


        return null;
    }

    private CartVo getCartVo(Integer userId) {
        List<CartProductVo> cartProductVoList = this.getCartProductVoList(userId);
        return null;
    }

    /**
     * 获取 CartProduct
     * @param userId
     * @return
     */
    private List<CartProductVo> getCartProductVoList(Integer userId) {
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(cartList)) {
            List<CartProductVo> cartProductVoList = Lists.newArrayList();
            for (Cart cartItem : cartList) {
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    //product is exist
                    CartProductVo cartProductVo = this.assembleCartProductVo(cartItem, product);
                    cartProductVoList.add(cartProductVo);
                }
            }
            return cartProductVoList;
        }
        return null;
    }

    /**
     * 组装 CartProductVo
     * @param cart
     * @param product
     * @return
     */
    public CartProductVo assembleCartProductVo(Cart cart, Product product){
        if (cart != null && product != null) {
            //cart assemble
            CartProductVo cartProductVo = new CartProductVo();
            cartProductVo.setId(cart.getId());
            cartProductVo.setUserId(cart.getUserId());
            cartProductVo.setProductId(cart.getProductId());
            cartProductVo.setQuantity(cart.getQuantity());
            //product assemble
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductSubtitle(product.getSubtitle());
            cartProductVo.setProductMainImage(product.getMainImage());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStatus(product.getStatus());

//            cartProductVo.setProductTotalPrice();

        }
        return null;
    }


}
