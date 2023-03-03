package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.ShoppingCart;
import cn.iyunmc.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("shoppingCart: {}", shoppingCart);
        //设置用户ID，指定当前是哪个用户的购物车数据
        Long currenId = BaseContext.getCurrenId();
        shoppingCart.setUserId(currenId);

        //查询当前菜品或者套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, currenId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            //添加到购物车的是菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //SLQ: select * from shopping_cart where user_id = ? and dish_id/

        ShoppingCart cartServiceOne = cartService.getOne(lqw);
        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            cartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            cartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车：...");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrenId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = cartService.list(lqw);

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> delete() {
        log.info("清空购物车...");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrenId());
        cartService.remove(lqw);
        return R.success("清除成功！");
    }
}
