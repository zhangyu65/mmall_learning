package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iOrderService.manageList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo) {
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iOrderService.manageDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("seach.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSeach(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iOrderService.manageSeach(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo) {
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iOrderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


}

