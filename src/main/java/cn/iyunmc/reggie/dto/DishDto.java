package cn.iyunmc.reggie.dto;

import cn.iyunmc.reggie.entity.Dish;
import cn.iyunmc.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
