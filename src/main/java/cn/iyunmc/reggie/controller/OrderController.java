package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.OrderDetail;
import cn.iyunmc.reggie.entity.Orders;
import cn.iyunmc.reggie.entity.ShoppingCart;
import cn.iyunmc.reggie.service.OrderDetailService;
import cn.iyunmc.reggie.service.OrderService;
import cn.iyunmc.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("order:{}", orders);
        orderService.submit(orders);
        return R.success("Test");
    }

    @GetMapping("/userPage")
    public R<IPage<OrderDetail>> getPage(@RequestParam int page, @RequestParam int pageSize) {
        log.info("信息查询：{}，{}", page, pageSize);
        Page<OrderDetail> page1 = new Page<>(page, pageSize);
        Long userId = BaseContext.getCurrenId();
        LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        List<Orders> orders = orderService.list(queryWrapper);
        List<Long> list = orders.stream().map(Orders::getId).collect(Collectors.toList());
        lqw.in(OrderDetail::getOrderId, list);

        orderDetailService.page(page1, lqw);
        return R.success(page1);
    }
}
