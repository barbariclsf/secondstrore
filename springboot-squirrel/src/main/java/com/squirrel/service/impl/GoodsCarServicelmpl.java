package com.squirrel.service.impl;

import com.squirrel.dao.GoodsCarMapper;
import com.squirrel.pojo.Goods;
import com.squirrel.pojo.GoodsCar;
import com.squirrel.service.GoodsCarService;
import com.squirrel.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("goodsCarService")
public class GoodsCarServicelmpl implements GoodsCarService {
    @Resource
    GoodsCarMapper goodsCarMapper;

    @Override
    public int  insertGoodsCar(GoodsCar goodsCar) {
       return  goodsCarMapper.insert(goodsCar);
    }

    @Override
    public List<GoodsCar> selectCarGoods(Integer userId) {
        return goodsCarMapper.getGoodsCarByUserId(userId);
    }

    @Override
    public int deleteCarGoods(Integer id) {
        return  goodsCarMapper.deleteByPrimaryKey(id);
    }

    @Override
    public GoodsCar getGoodsByPrimaryKey(Integer id) {
        return  goodsCarMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeyWithBLOBs(int goodsId, GoodsCar goodsCar) {
        goodsCar.setCatelogId(goodsId);
        goodsCarMapper.updateByPrimaryKeyWithBLOBs(goodsCar);
    }
}
