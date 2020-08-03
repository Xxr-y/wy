package com.fh.cart.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    ServerResponse queryCart(Integer productId, Integer count, HttpServletRequest request);

    ServerResponse queryCartProductCount(Member member);

    ServerResponse queryCartList(Member member);

    ServerResponse deleteProduct(Member member, Integer productId);
}
