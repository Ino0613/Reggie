package cn.iyunmc.reggie.service;

import cn.iyunmc.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;


public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
