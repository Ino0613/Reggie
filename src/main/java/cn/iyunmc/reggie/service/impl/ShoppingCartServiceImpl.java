package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.ShoppingCart;
import cn.iyunmc.reggie.mapper.ShoppingCartMapper;
import cn.iyunmc.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
