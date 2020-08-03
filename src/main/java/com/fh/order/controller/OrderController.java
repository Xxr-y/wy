package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.MemberAnnotation;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping("bulidOrder")
    @Idempotent
    public ServerResponse bulidOrder(String listStr, Integer addressId, Integer payType, @MemberAnnotation Member member){
        List<Cart> cartList = new ArrayList<>();
        if(StringUtils.isNotEmpty(listStr)){
            cartList = JSONObject.parseArray(listStr, Cart.class);
        }else {
            return ServerResponse.error("请选择商品");
        }
        return orderService.bulidOrder(cartList,addressId,payType,member);
    }


    @RequestMapping("getMToken")
    public ServerResponse getMToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }


    /*@RequestMapping("queryStock")
    public ServerResponse queryStock(List<Cart> cartList){
        return orderService.queryStock(cartList);
    }*/

}
