package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

public interface ProductService {
    ServerResponse queryHotProductList();

    ServerResponse queryProductList();

    ServerResponse queryProductPage(Long currentPage, Long pageSize);

    Product selectProductById(Integer productId);

    Long updateStock(Integer id, int count);
}
