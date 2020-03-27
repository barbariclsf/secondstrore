package com.squirrel.dao;

import com.squirrel.pojo.OrderRating;
import com.squirrel.pojo.OrderRatingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderRatingMapper {
    long countByExample(OrderRatingExample example);

    int deleteByExample(OrderRatingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderRating record);

    int insertSelective(OrderRating record);

    List<OrderRating> selectByExample(OrderRatingExample example);

    OrderRating selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderRating record, @Param("example") OrderRatingExample example);

    int updateByExample(@Param("record") OrderRating record, @Param("example") OrderRatingExample example);

    int updateByPrimaryKeySelective(OrderRating record);

    int updateByPrimaryKey(OrderRating record);
}