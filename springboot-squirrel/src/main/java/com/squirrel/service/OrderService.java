package com.squirrel.service;

import com.squirrel.pojo.Goods;
import com.squirrel.pojo.Order;

import java.util.List;

public interface OrderService {

    //添加交易记录
    public  void addOrder(Order order);

    //查找购买记录
    public List<Order> seletOrderById(Integer uid);

    //根据商品id查找购买记录
    public Order seletOneOrderByPid(Integer pid);



}
