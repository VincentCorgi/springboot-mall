package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.OrderDao;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.model.OrderItem;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.OrderService;
import com.vincent.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    // 控制多張table 一定要加 @Transactional 0 or 1
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductByID(buyItem.getProductId());

            // 計算總價錢
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem to OrderItem;
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(userId, orderItemList);

        return orderId;
    }
}
