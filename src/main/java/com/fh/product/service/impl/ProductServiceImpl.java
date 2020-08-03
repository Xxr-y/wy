package com.fh.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse queryHotProductList() {

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("isHot",1);
        List<Product> list = productMapper.selectList(wrapper);

        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse queryProductList() {
        List<Product> list = productMapper.selectList(null);
        return ServerResponse.success(list);
    }


    @Override
    public ServerResponse queryProductPage(Long currentPage, Long pageSize) {

        //起始条数
        Long start = (currentPage-1)*pageSize;
        //查询总条数
        Long totalCount = productMapper.queryTotalCount();

        List<Product> list = productMapper.queryList(start,pageSize);
        Long totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("totalPage",totalPage);

        return ServerResponse.success(map);
    }

    @Override
    public Product selectProductById(Integer productId) {
        Product product = productMapper.selectById(productId);
        return product;
    }

    @Override
    public Long updateStock(Integer id, int count) {
        return productMapper.updateStock(id,count);
    }

}
