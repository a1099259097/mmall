package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<User> login(String username, String password, HttpSession session) {
        ServiceRespond<User> respond = userService.login(username, password);
        if (respond.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,respond.getData());
        }
        return respond;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceRespond.createBySuccess();
    }

    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> register(User user){
        return userService.register(user);
    }


    @RequestMapping(value = "check_valid")
    @ResponseBody
    public ServiceRespond<String> checkValid(String str,String type) {
        ServiceRespond<String> respond = userService.checkValid(str, type);
        return respond;
    }


    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServiceRespond.createBySuccess(user);
        }
        return ServiceRespond.createByErrorMessage("user does not login,don't get user infomation");
    }

    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> getQuestion(String username) {
        return userService.getQuestion(username);
    }

    //TODO https://gitee.com/imooccode/happymmallwiki/wikis/Home
    //TODO forget_check_answer.do 2018-03-21 04:21:16

    @RequestMapping(value = "forget_check_answer",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> forgetCheckAnswer(String username, String question, String answer) {
        ServiceRespond<String> respond = userService.forgetCheckAnswer(username, question, answer);
        return respond;
    }

    @RequestMapping(value = "forget_reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> forgetResetPassword(String username, String passwordNew,String forgetToken) {
        ServiceRespond<String> respond = userService.forgetResetPassword(username, passwordNew, forgetToken);
        return respond;
    }

    @RequestMapping(value = "reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<String> resetPassword(String passwordOld, String passwordNew,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServiceRespond<String> respond = userService.resetPassword(passwordOld, passwordNew, user);
        return respond;
    }

    @RequestMapping(value = "update_information",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<User> updateInformation(HttpSession session, User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServiceRespond.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServiceRespond<User> respond = userService.updateInformation(user);
        if (respond.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,respond.getData());
        }
        return respond;
    }

    @RequestMapping(value = "get_information",method = RequestMethod.POST)
    @ResponseBody
    public ServiceRespond<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"用户未登录,无法获取当前用户信息,status=10,强制登录");
        }
        return userService.getInformation(currentUser.getId());
    }


}
