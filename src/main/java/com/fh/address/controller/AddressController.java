package com.fh.address.controller;

import com.fh.address.service.AddressService;
import com.fh.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("test")
    public String test(){
        return "test";
    }


    @RequestMapping("queryAddressList")
    public ServerResponse queryAddressList(){
        return addressService.queryAddressList();
    }


    @RequestMapping("queryStatusList")
    public ServerResponse queryStatusList(){
        return addressService.queryStatusList();
    }

}
