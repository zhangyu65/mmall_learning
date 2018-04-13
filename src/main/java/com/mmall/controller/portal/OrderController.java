package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService iOrderService;



    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, Integer shippingId){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping("cancle.do")
    @ResponseBody
    public ServerResponse cancle(HttpServletRequest request, Long orderNo){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iOrderService.cancle(user.getId(), orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }






























    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallBack(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();

        Map requestParams =  request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String) iter.next();
            String valueStr = "";
            String[] values = (String[])requestParams.get(name);
            for (int i = 0; i<values.length; i++){
                valueStr = (i==values.length-1) ? valueStr+values[i] : valueStr+values[i]+",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数：{}",requestParams.get("sign"),requestParams.get("trade_status"),params.toString());

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝回掉异常",e);
        }

        //TODO yanzheng


        ServerResponse serverResponse = iOrderService.aliCallBack(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest request, Long orderNo){
        String  loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
