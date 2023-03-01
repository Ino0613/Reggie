package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.common.CustomException;
import cn.iyunmc.reggie.entity.Category;
import cn.iyunmc.reggie.entity.Dish;
import cn.iyunmc.reggie.entity.Setmeal;
import cn.iyunmc.reggie.mapper.CategoryMapper;
import cn.iyunmc.reggie.service.CategoryService;
import cn.iyunmc.reggie.service.DishService;
import cn.iyunmc.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {

        //添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId, id);
        long count = dishService.count(lqw);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count > 0) {
            //已经关联了菜品，抛出异常
            throw new CustomException("当前业务已关联了菜品，无法删除");
        }
        LambdaQueryWrapper<Setmeal> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Setmeal::getCategoryId,id);
        long meal = setmealService.count(lqw1);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        if (meal > 0) {
            //已经关联了套餐，抛出异常
            throw new CustomException("当前业务已关联了套餐，无法删除");

        }
        //正常删除
        super.removeById(id);
    }
}
