package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.OrderQueryParams;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
