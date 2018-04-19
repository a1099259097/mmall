package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceRespond;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FileService;
import com.mmall.service.ProductService;
import com.mmall.service.UserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/product")
public class ProductManagerController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;


    @RequestMapping(value = "save.do")
    public ServiceRespond save(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        ServiceRespond serviceRespond = productService.saveOrUpdate(product);
        return serviceRespond;
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServiceRespond setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        return productService.setSaleStatus(productId,status);
    }

    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServiceRespond productDetail(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        return productService.managerProductDetail(productId);
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServiceRespond getProductList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServiceRespond searchProduct(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                        String productName, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        return productService.searchProduct(productName,productId,pageNum,pageSize);
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    public ServiceRespond uploadFile(HttpSession session,@RequestParam(value = "upload_file") MultipartFile file) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceRespond.createByCodeError(ResponseCode.NEED_LOGIIN.getCode(),"need login admin");
        }
        if (!userService.checkAdminRole(user)){
            return ServiceRespond.createByErrorMessage("no primission operator");
        }
        String path = session.getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(path, file);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServiceRespond.createBySuccess(fileMap);
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, MultipartFile file) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map map = Maps.newHashMap();
        if (user == null) {
            map.put("success",false);
            map.put("msg","user not login");
            return map;
        }
        if (!userService.checkAdminRole(user)){
            map.put("success",false);
            map.put("msg","user not primission");
            return map;
        }

        String path = session.getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(path, file);
        if (StringUtils.isBlank(targetFileName)){
            map.put("success",false);
            map.put("msg","update error");
            return map;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix"+targetFileName) ;
        map.put("success",true);
        map.put("msg","user successful");
        map.put("file_path",url);
        map.put("Access-Control-Allow-Headers","x-File-Name");
        return map;
    }





}
