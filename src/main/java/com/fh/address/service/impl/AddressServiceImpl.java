package com.fh.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.address.mapper.AddressMapper;
import com.fh.address.model.Address;
import com.fh.address.service.AddressService;
import com.fh.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;


    @Override
    public ServerResponse queryAddressList() {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        List<Address> addressList = addressMapper.selectList(queryWrapper);
        return ServerResponse.success(addressList);
    }

    @Override
    public ServerResponse queryStatusList() {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<Address> list = addressMapper.selectList(queryWrapper);
        return ServerResponse.success(list);
    }

}
