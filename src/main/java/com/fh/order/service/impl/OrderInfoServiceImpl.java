package com.fh.order.service.impl;

import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

}
