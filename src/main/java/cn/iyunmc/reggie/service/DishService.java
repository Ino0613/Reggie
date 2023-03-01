package cn.iyunmc.reggie.service;

import cn.iyunmc.reggie.dto.DishDto;
import cn.iyunmc.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作来那个张表： dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据ID来查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
