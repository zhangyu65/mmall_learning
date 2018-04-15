package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
        return iOrderService.manageList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo) {
        return iOrderService.manageDetail(orderNo);
    }

    @RequestMapping("seach.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSeach(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        return iOrderService.manageSeach(orderNo, pageNum, pageSize);
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo) {
        return iOrderService.manageSendGoods(orderNo);
    }


}

