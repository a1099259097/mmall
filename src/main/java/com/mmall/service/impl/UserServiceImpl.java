package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.Servlet;
import javax.servlet.ServletResponse;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceRespond<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServiceRespond.createByErrorMessage("username does not exist");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.checkLogin(username, md5Password);

        if (user == null) {
            return ServiceRespond.createByErrorMessage("password error");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServiceRespond.createBySuccess("login success",user);
    }

    @Override
    public ServiceRespond<String> register(User user) {

        ServiceRespond<String> validRespond = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validRespond.isSuccess()){
            return validRespond;
        }
        validRespond = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validRespond.isSuccess()) {
            return validRespond;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceRespond.createByErrorMessage("register fail");
        }
        return ServiceRespond.createBySuccessMessage("register success");
    }

    @Override
    public ServiceRespond<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            int resultCount;
            if (Const.USERNAME.equals(type)){
                resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServiceRespond.createByErrorMessage("username already exist");
                }
            }else if (Const.EMAIL.equals(type)) {
                resultCount = userMapper.checkEmall(str);
                if (resultCount > 0) {
                    return ServiceRespond.createByErrorMessage("email already exist");
                }
            }else if (Const.PHONE.equals(type)){
                resultCount = userMapper.checkPhone(str);
                if (resultCount > 0) {
                    return ServiceRespond.createByErrorMessage("phone already exist");
                }
            }
                return ServiceRespond.createBySuccess("velification success");
        }
        return ServiceRespond.createByCodeError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    @Override
    public ServiceRespond<String> getQuestion(String username) {
        String question = userMapper.getQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {
            return ServiceRespond.createByErrorMessage("该用户未设置找回密码问题");
        }
        return ServiceRespond.createBySuccess(question);
    }

    @Override
    public ServiceRespond<String> forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount == 0) {
            //password error
            return ServiceRespond.createByErrorMessage("问题答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
        return ServiceRespond.createBySuccess(forgetToken);
    }

    @Override
    public ServiceRespond<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)){
            return ServiceRespond.createByErrorMessage("参数错误,token需要传递");
        }
        ServiceRespond<String> respond = checkValid(username, Const.USERNAME);
        if (respond.isSuccess()) {
            // username no exist
            return ServiceRespond.createByErrorMessage("用户名不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (token == null) {
            return ServiceRespond.createByErrorMessage("token已经失效");
        }else if (StringUtils.equals(token,forgetToken)){
            String md5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePasswordByUsername(username, md5NewPassword);
            if(resultCount > 0) {
                return ServiceRespond.createBySuccessMessage("修改密码成功");
            }else {
                return ServiceRespond.createByErrorMessage("修改密码操作失败");
            }
        }else {
            return ServiceRespond.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
    }

    @Override
    public ServiceRespond<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassowrd(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0){
            return ServiceRespond.createByErrorMessage("旧密码输入错误");
        }
        resultCount = userMapper.updatePasswordByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        if (resultCount == 0){
            return ServiceRespond.createByErrorMessage("updata password error");
        }else {
            return ServiceRespond.createBySuccessMessage("修改密码成功");
        }
    }

    /*@Override
    public ServiceRespond<String> updateInformation(User user, String email, String phone, String question, String answer) {
        ServiceRespond<String> respond = checkValid(email, Const.EMAIL);
        if (!respond.isSuccess()){
            // email exist
            return respond;
        }
        respond = checkValid(phone,Const.PHONE);
        if (!respond.isSuccess()) {
            //email exist
            return respond;
        }
        user.setEmail(email);
        user.setPhone(phone);
        user.setQuestion(question);
        user.setAnswer(answer);
        int resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount == 0) {
            return ServiceRespond.createByErrorMessage("update error");
        }

        return ServiceRespond.createBySuccess("更新个人信息成功",);
    }*/

    @Override
    public ServiceRespond<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount == 0) {
            return ServiceRespond.createByErrorMessage("email already exist");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount == 0) {
            return ServiceRespond.createByErrorMessage("更新个人信息失败");
        }else {
            return ServiceRespond.createBySuccess("更新个人信息成功",updateUser);
        }
    }

    @Override
    public ServiceRespond<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServiceRespond.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceRespond.createBySuccess(user);
    }

    /**
     * check Role
     */
    public boolean checkAdminRole(User user) {
        Integer role = user.getRole();
        return user != null && role == Const.Role.ROLE_ADMIN;
    }
}
