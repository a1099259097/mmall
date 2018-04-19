package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServiceRespond;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<User> login(String username, String password, HttpSession session){
        ServiceRespond<User> respond = userService.login(username, password);
        if (respond.isSuccess()) {
            User user = respond.getData();
            if (Const.Role.ROLE_ADMIN == user.getRole()){
                session.setAttribute(Const.CURRENT_USER,user);
                return respond;
            }else {
                return ServiceRespond.createByErrorMessage("no admin, login error");
            }
        }
        return respond;
    }
}
