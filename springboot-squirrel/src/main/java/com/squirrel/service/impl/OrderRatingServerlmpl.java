package com.squirrel.service.impl;

import com.squirrel.dao.OrderMapper;
import com.squirrel.dao.OrderRatingMapper;
import com.squirrel.pojo.OrderRating;
import com.squirrel.service.OrderRatingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(" orderRatingServer")
public class OrderRatingServerlmpl implements OrderRatingService {
    @Resource
    OrderRatingMapper orderRatingMapper;
    @Override
    public void addOrderRating(OrderRating orderRating) {
        orderRatingMapper.insert(orderRating);
    }
}
