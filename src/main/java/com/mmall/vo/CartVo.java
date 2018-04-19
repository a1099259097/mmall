package com.mmall.vo;

import java.math.BigDecimal;

public class CartVo<T> {

    private T data;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    public CartVo() {
    }

    public CartVo(T data, boolean allChecked, BigDecimal cartTotalPrice) {
        this.data = data;
        this.allChecked = allChecked;
        this.cartTotalPrice = cartTotalPrice;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
