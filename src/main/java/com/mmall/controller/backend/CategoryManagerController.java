package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.CategoryService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServiceRespond<List<Category>> getCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            //user no login
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        boolean isAdmin = userService.checkAdminRole(user);
        if (!isAdmin) {
            return ServiceRespond.createByErrorMessage("no admin");
        }
        ServiceRespond<List<Category>> respond = categoryService.getChildrenParallelCategory(categoryId);
        return respond;
    }

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceRespond addCategory(HttpSession session, Integer parentId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        boolean isAdmin = userService.checkAdminRole(user);
        if (!isAdmin){
            return ServiceRespond.createByErrorMessage("no admin");
        }
        ServiceRespond respond = categoryService.addCategory(parentId, categoryName);
        return respond;
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServiceRespond setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        boolean isAdmin = userService.checkAdminRole(user);
        if (!isAdmin){
            return ServiceRespond.createByErrorMessage("no admin");
        }
        ServiceRespond serviceRespond = categoryService.setCategoryName(categoryId, categoryName);
        return serviceRespond;
    }

    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServiceRespond<List<Integer>> getDeepCategory(HttpSession session, Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),ResponseCode.NEED_LOGIIN.getDesc());
        }
        boolean isAdmin = userService.checkAdminRole(user);
        if (!isAdmin){
            return ServiceRespond.createByErrorMessage("no admin");
        }
        ServiceRespond<List<Integer>> respond = categoryService.selectCategoryAndChildrenById(categoryId);
        return respond;
    }



}
