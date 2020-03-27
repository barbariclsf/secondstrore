package com.squirrel.dao;

import com.squirrel.pojo.GoodsCar;
import com.squirrel.pojo.GoodsCarExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsCarMapper {
    long countByExample(GoodsCarExample example);

    int deleteByExample(GoodsCarExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCar record);

    int insertSelective(GoodsCar record);

    List<GoodsCar> selectByExampleWithBLOBs(GoodsCarExample example);

    List<GoodsCar> selectByExample(GoodsCarExample example);

    GoodsCar selectByPrimaryKey(Integer id);

    List<GoodsCar> getGoodsCarByUserId(Integer userId);

    int updateByExampleSelective(@Param("record") GoodsCar record, @Param("example") GoodsCarExample example);

    int updateByExampleWithBLOBs(@Param("record") GoodsCar record, @Param("example") GoodsCarExample example);

    int updateByExample(@Param("record") GoodsCar record, @Param("example") GoodsCarExample example);

    int updateByPrimaryKeySelective(GoodsCar record);

    int updateByPrimaryKeyWithBLOBs(GoodsCar record);

    int updateByPrimaryKey(GoodsCar record);

    public GoodsCar getGoodsByPrimaryKey(Integer id);

    public void updateByPrimaryKeyWithBLOBs(int goodsId ,GoodsCar goodsCar);


}