package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;

public interface ProductDao {

    Product getProductByID(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
