package com.mmall.service;

import com.mmall.common.ServiceRespond;
import com.mmall.pojo.User;

public interface UserService {

    ServiceRespond<User> login(String username, String password);

    ServiceRespond<String> register(User user);

    ServiceRespond<String> checkValid(String str, String type);

    ServiceRespond<String> getQuestion(String username);

    ServiceRespond<String> forgetCheckAnswer(String username, String question, String answer);

    ServiceRespond<String> forgetResetPassword(String username, String newPassword, String Token);

    ServiceRespond<String> resetPassword(String passwordOld, String passwordNew, User user);

//    ServiceRespond<String> updateInformation(User user, String email, String phone, String question, String answer);
    ServiceRespond<User> updateInformation(User user);

    ServiceRespond<User> getInformation(Integer userId);

    boolean checkAdminRole(User user);
}
