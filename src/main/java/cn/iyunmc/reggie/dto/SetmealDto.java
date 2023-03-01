package cn.iyunmc.reggie.dto;

import cn.iyunmc.reggie.entity.Setmeal;
import cn.iyunmc.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
