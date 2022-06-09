package com.lock.model;

import com.util.RandomUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsVO implements Serializable {

    private static final long serialVersionUID = 526886549959179138L;

    private String name;

    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GoodsVO{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public static GoodsVO produceOne() {
        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setName("商品-"+ RandomUtil.nextInt(10));
        goodsVO.setPrice(new Double(RandomUtil.nextInt(1000)));
        return goodsVO;
    }
}
