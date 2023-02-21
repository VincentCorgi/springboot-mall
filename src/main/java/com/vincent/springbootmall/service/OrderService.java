package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
