package com.squirrel.service.impl;

import com.squirrel.dao.OrderMapper;
import com.squirrel.pojo.Goods;
import com.squirrel.pojo.Order;
import com.squirrel.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("orderService")
public class OrderServicelmpl implements OrderService {
    @Resource
    OrderMapper orderMapper;

    @Override
    public void addOrder(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public List<Order> seletOrderById(Integer uid) {
       List<Order> orders = orderMapper.getOredersByUserId(uid);
       return  orders;
    }
    @Override
    public Order seletOneOrderByPid(Integer pid){
        return  orderMapper.seletOneOrderByPid(pid);
    }
}
