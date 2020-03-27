package com.squirrel.service;

import com.squirrel.pojo.Goods;
import com.squirrel.pojo.GoodsCar;
import com.squirrel.pojo.Order;

import java.util.List;

public interface GoodsCarService {

    /*插入商品
    * */
    public int insertGoodsCar(GoodsCar goodsCar);


    /*
    * 根据用户id查询
    * */
    public List<GoodsCar> selectCarGoods(Integer userId);


    /*删除*/
    public int deleteCarGoods(Integer id);

    /*根据商品id查询*/
    public GoodsCar getGoodsByPrimaryKey(Integer goodsId);

    /*
    * 更新*/
    public void updateByPrimaryKeyWithBLOBs(int goodsId ,GoodsCar goodsCar);

}
