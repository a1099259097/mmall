package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(@Param(value = "username") String username);

    int checkEmall(@Param(value = "email") String email);

    int checkPhone(@Param(value = "phone") String phone);

    int checkEmailByUserId(@Param(value = "email") String email,@Param(value = "userId") int userId);

    User checkLogin(@Param(value = "username") String username, @Param(value = "password")String password);

    String getQuestionByUsername(@Param(value = "username") String username);

    int checkAnswer(@Param(value = "username") String username, @Param(value = "question") String question, @Param(value = "answer") String answer);

    int updatePasswordByUsername(@Param(value = "username") String username, @Param(value = "newPassword") String newPassword);

    int checkPassowrd(@Param(value = "password") String password, @Param(value = "userId") int userId);


}