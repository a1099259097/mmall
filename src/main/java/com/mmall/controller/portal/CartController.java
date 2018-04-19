package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.pojo.User;
import com.mmall.service.CartService;
import com.mmall.vo.CartProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/list.do")
    @ResponseBody
    //todo no finish
    public ServiceRespond getCartList(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        // alreadly login
//        ServiceRespond<PageInfo> result = cartService.getCartList(user.getId());
        return null;
    }

    @RequestMapping(value = "/add.do")
    @ResponseBody
    public ServiceRespond add(HttpSession session, Integer productId, Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        // alreadly login
        ServiceRespond<List<CartProductVo>> result = cartService.add(user.getId(),productId,count);
        return result;
    }



}
