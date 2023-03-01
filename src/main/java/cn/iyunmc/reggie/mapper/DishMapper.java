package cn.iyunmc.reggie.mapper;

import cn.iyunmc.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
