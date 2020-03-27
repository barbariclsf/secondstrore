package com.squirrel.pojo;

import java.util.ArrayList;
import java.util.List;

public class GoodsCarExtend {
    private GoodsCar goodsCar;
    private List<Image> images = new ArrayList<Image>();

    public GoodsCar getGoodsCar() {
        return goodsCar;
    }

    public void setGoodsCar(GoodsCar goodsCar) {
        this.goodsCar = goodsCar;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
