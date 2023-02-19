package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;

public interface ProductService {

    Product getProductByID(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
