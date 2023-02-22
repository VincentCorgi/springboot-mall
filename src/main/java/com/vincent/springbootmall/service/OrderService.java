package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.OrderQueryParams;
import com.vincent.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
