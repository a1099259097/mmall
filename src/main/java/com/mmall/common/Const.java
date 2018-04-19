package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public final static String CURRENT_USER = "currentUser";

    public final static String EMAIL = "email";

    public final static String PHONE = "phone";

    public final static String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;  //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }

    public interface ProductListVoOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc","price_desc");
    }

    public interface Cart{
        Integer CHECK = 1;
        Integer NO_CHECK = 0;

        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";

    }
}
