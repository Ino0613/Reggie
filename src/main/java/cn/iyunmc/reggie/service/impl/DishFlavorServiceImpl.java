package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.DishFlavor;
import cn.iyunmc.reggie.mapper.DishFlavorMapper;
import cn.iyunmc.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService{
}
