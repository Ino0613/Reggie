package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.OrderDetail;
import cn.iyunmc.reggie.mapper.OrderDetailMapper;
import cn.iyunmc.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
