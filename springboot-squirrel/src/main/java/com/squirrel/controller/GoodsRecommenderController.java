package com.squirrel.controller;

import com.squirrel.pojo.Goods;
import com.squirrel.pojo.GoodsExtend;
import com.squirrel.pojo.Image;
import com.squirrel.pojo.User;
import com.squirrel.service.GoodsService;
import com.squirrel.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GoodsRecommenderController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ImageService imageService;
    private final int PAGE_SIZE = 20;

    private final int CUR_PAGE = 1;
    // 推荐结果个数
    private final int RECOMMENDER_NUM = 6;

    @RequestMapping(value = "/mahout/recommend")
    public ModelAndView recommendlist(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User cur_user = (User) session.getAttribute("cur_user");
        modelAndView.addObject("cur_user", cur_user);
        List<Goods> goodsRBI = goodsService.recommendGoodsBasedItem(cur_user.getId(), RECOMMENDER_NUM);
        List<GoodsExtend> goodsExtendList = new ArrayList<GoodsExtend>();
        if(goodsRBI != null){
            for(int i = 0;i<goodsRBI.size();i++) {
                GoodsExtend goodsExtend = new GoodsExtend();
                Goods goods = goodsRBI.get(i);
                System.out.println("推荐： "+goods.getName());
                List<Image> imageList = imageService.getImagesByGoodsPrimaryKey(goods.getId());
                goodsExtend.setGoods(goods);
                goodsExtend.setImages(imageList);
                goodsExtendList.add(i,goodsExtend);
            }
        }
        modelAndView.addObject("goodsExtendList", goodsExtendList);
        modelAndView.setViewName("/goods/catelogGoods");
        return modelAndView;

    }
}
