package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.Orders;
import cn.iyunmc.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("order:{}", orders);
        orderService.submit(orders);
        return R.success("Test");
    }
}
