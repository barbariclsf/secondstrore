package com.squirrel.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.squirrel.alipay.AlipayConfig;
import com.squirrel.pojo.*;
import com.squirrel.service.GoodsCarService;
import com.squirrel.service.GoodsService;
import com.squirrel.service.OrderRatingService;
import com.squirrel.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.alipay.api.internal.util.AlipaySignature.rsaCheckV1;

@Controller
public class PayController {
   @Autowired
    OrderService orderService;
   @Autowired
    GoodsService goodsService;
    @Autowired
    GoodsCarService goodsCarService;
    @Autowired
    OrderRatingService orderRatingService;

    @RequestMapping(value = "/goAlipay", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public  String alipay(HttpServletRequest request, HttpServletRequest response, Integer id, String name,String price, HttpSession session) throws UnsupportedEncodingException, AlipayApiException {

        double totalPrice = Double.valueOf(price);
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);


        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = UUID.randomUUID().toString();
        //订单名称，必填
        String subject = name;

        //金额
        String total_amount = String.valueOf( totalPrice);

        //过期时间
        String timeout_express = "1c";

        //选填
        String body ="";

        //保存交易记录
        Order order = new Order();
        order.setPid(id);
        order.setUid((Integer) session.getAttribute("user_id"));
        order.setNum(1);
        order.setPrice(totalPrice);
        order.setOrderNum(out_trade_no);
        order.setTime(new Date());


        User user = (User) session.getAttribute("cur_user");
        session.setAttribute("ordergood"+user.getId(),order);

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout_express +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }
    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException, AlipayApiException {
        response.setContentType("text/html;charset=utf-8");
        System.out.println("jiji");
        boolean verifyResult = rsaCheckV1(request);
        if(verifyResult){
            //验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付成功保存到数据库
            User user = (User) session.getAttribute("cur_user");
            Order order = (Order)session.getAttribute("ordergood"+user.getId());
            order.setTradenum(trade_no);
            //得到总物品中的商品id，设置已被买状态
            Integer pid = order.getPid();
            Goods goods = goodsService.getGoodsByPrimaryKey(pid);
            goods.setState(1);
            goodsService.updateGoodsByPrimaryKeyWithBLOBs(pid,goods);

            //插入评分表
            OrderRating orderRating = new OrderRating();
            orderRating.setPid(goods.getId());
            orderRating.setUserid(user.getId());
            orderRating.setRating(4);
            orderRating.setTime(order.getTime());
            orderRatingService.addOrderRating(orderRating);

            //设置购物车中的商品状态
            GoodsCar goodsCar = goodsCarService.getGoodsByPrimaryKey(pid);
            if(goodsCar != null)
            {
                goodsCar.setState(1);
                goodsCarService.updateByPrimaryKeyWithBLOBs(pid,goodsCar);
            }

            //更新订单表
            orderService.addOrder(order);
            return "redirect:/user/mybuyed";

        }else{
            return "redirect:/error/404";

        }
    }

    public boolean rsaCheckV1(HttpServletRequest request){
        // https://docs.open.alipay.com/54/106370
        // 获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        try {
            boolean verifyResult = AlipaySignature.rsaCheckV1(params,
                    AlipayConfig.merchant_private_key,
                    AlipayConfig.charset,
                    AlipayConfig.sign_type);

            return verifyResult;
        } catch (AlipayApiException e) {

            return true;
        }
    }
}
