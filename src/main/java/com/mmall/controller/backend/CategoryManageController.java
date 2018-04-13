package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, String categoryName, Integer parentId){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.addCategory(categoryName, parentId);
        }else {
            return ServerResponse.createByErrorMessage("非管理员");
        }
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }else {
            return ServerResponse.createByErrorMessage("非管理员");
        }
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("非管理员");
        }
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenAndDeepCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.selectCategotyAndChildrenById(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("非管理员");
        }
    }
}
