package com.fh.cart.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.mapper.CartMapper;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse queryCart(Integer productId, Integer count, HttpServletRequest request) {
        // 1.验证商品是否存在 (通过商品id查询单条数据)
        Product product = productService.selectProductById(productId);
        if(product == null){
            return ServerResponse.error(ServerEnum.PRODUCT_NOT_EXIST);
        }

        // 2.验证商品是否上架 (status=1为上架，=2为下架)
        if(product.getStatus() == 2){
            return ServerResponse.error(ServerEnum.PRODUCT_SOLD_OUT);
        }
        // 3.验证购物车中是否存在该商品
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        boolean exist = RedisUtil.exist1(SystemConstant.CART_KEY+member.getId(), productId.toString());
        // 如果购物车中不存在该数据
        if(!exist){
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setCount(count);
            cart.setPrice(product.getPrice());
            cart.setProductName(product.getName());
            cart.setFilePath(product.getFilePath());
            String jsonString = JSONObject.toJSONString(cart);
            // 往缓存中存入一条数据
            RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
        // 如果存在该数据,则修改商品的数量
        }else {
            String hget = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(), productId.toString());
            Cart cart = JSONObject.parseObject(hget, Cart.class);
            cart.setCount(cart.getCount()+count);
            // 修改完数量后  再重新存入到redis中
            String jsonString = JSONObject.toJSONString(cart);
            // 往缓存中存入一条数据
            RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
        }


        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryCartProductCount(Member member) {

        List<String> stringList = RedisUtil.hvals(SystemConstant.CART_KEY + member.getId());
        long totalCount = 0;
        if(stringList != null && stringList.size() > 0){
            for(String str : stringList){
                Cart cart = JSONObject.parseObject(str, Cart.class);
                totalCount += cart.getCount();
            }
        }else {
            return ServerResponse.success(0);
        }
        return ServerResponse.success(totalCount);
    }

    @Override
    public ServerResponse queryCartList(Member member) {
        List<String> stringList = RedisUtil.hvals(SystemConstant.CART_KEY + member.getId());
        List<Cart> cartList = new ArrayList<>();
        if(stringList != null && stringList.size() > 0){
            for(String str : stringList){
                Cart cart = JSONObject.parseObject(str, Cart.class);
                cartList.add(cart);
            }
        }else {
            return ServerResponse.error(ServerEnum.CART_IS_NULL.getMsg());
        }
        return ServerResponse.success(cartList);
    }

    @Override
    public ServerResponse deleteProduct(Member member, Integer productId) {
        RedisUtil.hdel(SystemConstant.CART_KEY + member.getId(),productId.toString());
        return ServerResponse.success();
    }
}
