package com.fh.order.service;

import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

import java.util.List;

public interface OrderService {
    ServerResponse bulidOrder(List<Cart> cartList, Integer addressId, Integer payType, Member member);

}
