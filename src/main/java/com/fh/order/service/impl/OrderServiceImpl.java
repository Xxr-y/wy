package com.fh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.order.service.OrderService;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public ServerResponse bulidOrder(List<Cart> cartList, Integer addressId, Integer payType, Member member) {

        // 订单id
        String orderId = IdUtil.createId();

        // 组装 订单详情 数据
        List<OrderInfo> orderInfoList = new ArrayList<>();

        // 商品总价格
        BigDecimal totalPrice = new BigDecimal("0.00");

        // 库存不足的集合
        List<String> stockNotFull = new ArrayList<>();

        for (Cart cart : cartList) {
            Product product = productService.selectProductById(cart.getProductId());
            if(product.getStock() < cart.getCount()){
                // 库存不足
                stockNotFull.add(cart.getProductName());
            }else {
                // 减库存(这个商品传过来的数量为几，就将库存减几)  判断库存是否充足
                Long updStock = productService.updateStock(product.getId(),cart.getCount());
                if(updStock == 1){
                    // 库存充足  生成订单详情
                    OrderInfo orderInfo = bulidOrderInfo(orderId, cart);
                    // 将订单详情放入到list中
                    orderInfoList.add(orderInfo);
                    // 计算商品总价
                    BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(),cart.getCount()+"");
                    totalPrice = BigDecimalUtil.add(totalPrice,subTotal);
                }else {
                    // 库存不足
                    stockNotFull.add(cart.getProductName());
                }
            }
        }
        // 生成订单  首先判断是否有库存不足的商品
        if(orderInfoList != null && orderInfoList.size() == cartList.size()){
            // 库存都够  保存订单详情
            for (OrderInfo orderInfo : orderInfoList) {
                orderInfoMapper.insert(orderInfo);
                // 更新redis购物车
                updateRedisCart(member, orderInfo);
            }
            // 生成订单
            bulidOrder(addressId, payType, member, orderId, totalPrice);
            return ServerResponse.success(orderId);
        }else {
            return ServerResponse.error(stockNotFull);
        }
    }

    private OrderInfo bulidOrderInfo(String orderId, Cart cart) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setProductName(cart.getProductName());
        orderInfo.setFilePath(cart.getFilePath());
        orderInfo.setPrice(cart.getPrice());
        orderInfo.setOrderId(orderId);
        orderInfo.setCount(cart.getCount());
        orderInfo.setProductId(cart.getProductId());
        return orderInfo;
    }

    private void bulidOrder(Integer addressId, Integer payType, Member member, String orderId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setId(orderId);
        order.setMemberId(member.getId());
        order.setTotalPrice(totalPrice);
        order.setStatus(SystemConstant.ORDER_STATUS_WAIN);
        orderMapper.insert(order);
    }

    private void updateRedisCart(Member member, OrderInfo orderInfo) {
        String cartJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString());
        if(StringUtils.isNotEmpty(cartJson)){
            Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
            if(cart1.getCount() <= orderInfo.getCount()){
                // 删除购物车中该商品
                RedisUtil.hdel(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString());
            }else {
                // 更新购物车
                cart1.setCount(cart1.getCount()-orderInfo.getCount());
                String s = JSONObject.toJSONString(cart1);
                RedisUtil.hset(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString(),s);
            }
        }
    }


}
