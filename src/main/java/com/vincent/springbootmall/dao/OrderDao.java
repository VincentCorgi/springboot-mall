package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
