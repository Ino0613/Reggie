package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.CustomException;
import cn.iyunmc.reggie.entity.AddressBook;
import cn.iyunmc.reggie.entity.Orders;
import cn.iyunmc.reggie.entity.ShoppingCart;
import cn.iyunmc.reggie.mapper.OrderMapper;
import cn.iyunmc.reggie.service.AddressBookService;
import cn.iyunmc.reggie.service.OrderService;
import cn.iyunmc.reggie.service.ShoppingCartService;
import cn.iyunmc.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;
    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        //获得当前用户id
        Long currenId = BaseContext.getCurrenId();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, currenId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lqw);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单！");
        }

        //查询用户数据


        //向订单表插入数据，一条数据
        this.save(orders);
        //向订单明细表插入数据

    }
}
