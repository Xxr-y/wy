package com.fh.cart.controller;

import com.fh.cart.service.CartService;
import com.fh.common.MemberAnnotation;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @RequestMapping("queryCart")
    public ServerResponse queryCart(Integer productId, Integer count, HttpServletRequest request){
        return cartService.queryCart(productId,count,request);
    }


    @RequestMapping("queryCartProductCount")
    public ServerResponse queryCartProductCount(@MemberAnnotation Member member){
        return cartService.queryCartProductCount(member);
    }


    @RequestMapping("queryCartList")
    public ServerResponse queryCartList(@MemberAnnotation Member member){
        return cartService.queryCartList(member);
    }


    @RequestMapping("deleteProduct/{productId}")
    public ServerResponse deleteProduct(@MemberAnnotation Member member,@PathVariable("productId") Integer productId){
        return cartService.deleteProduct(member,productId);
    }


}
