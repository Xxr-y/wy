package com.fh.product.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;


    // 查询商品是否热销
    @RequestMapping("queryHotProductList")
    @Ignore
    public ServerResponse queryHotProductList(){
        return productService.queryHotProductList();
    }


    // 查询全部商品
    @RequestMapping("queryProductList")
    @Ignore
    public ServerResponse queryProductList(){
        return productService.queryProductList();
    }


    /*

    * 分页查询
    * currentPage:每页条数
    * pageSize:当前页数
    *
    * */
    @RequestMapping("queryProductPage")
    @Ignore
    public ServerResponse queryProductPage(Long currentPage, Long pageSize){
        return productService.queryProductPage(currentPage,pageSize);
    }


}
