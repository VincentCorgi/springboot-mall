package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.OrderDao;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dao.UserDao;
import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.OrderQueryParams;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.model.User;
import com.vincent.springbootmall.service.OrderService;
import com.vincent.springbootmall.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    // ????????????table ???????????? @Transactional 0 or 1
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // ?????? user ????????????
        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("??? userId {} ?????????", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductByID(buyItem.getProductId());

            // ?????? product ?????????????????????????????????
            if (product == null) {
                log.warn("?????? {} ?????????", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("?????? {} ?????????????????????????????????????????????????????? {}?????????????????? {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // ??????????????????
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            // ???????????????
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // ?????? BuyItem to OrderItem;
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // ????????????
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }
}
