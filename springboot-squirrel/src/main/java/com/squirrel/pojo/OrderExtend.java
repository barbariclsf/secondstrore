package com.squirrel.pojo;

import java.util.ArrayList;
import java.util.List;

public class OrderExtend {
    private Order order;
    private String name;
    private String unmae;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private List<Image> images = new ArrayList<Image>();

    public Order getOrder() {
        return order;
    }

    public String getUnmae() {
        return unmae;
    }

    public void setUnmae(String unmae) {
        this.unmae = unmae;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
