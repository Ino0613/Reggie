package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.dto.DishDto;
import cn.iyunmc.reggie.entity.Category;
import cn.iyunmc.reggie.entity.Dish;
import cn.iyunmc.reggie.entity.DishFlavor;
import cn.iyunmc.reggie.mapper.DishMapper;
import cn.iyunmc.reggie.service.CategoryService;
import cn.iyunmc.reggie.service.DishFlavorService;
import cn.iyunmc.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> dishList(int page, int pageSize, String name) {
        log.info("page : {},pageSize:{},name:{}", page, pageSize, name);

        //构造分页构造器对象
        Page<Dish> pageinfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //添加排序条件
        lqw.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageinfo, lqw);

        //对象拷贝
        BeanUtils.copyProperties(pageinfo, dishDtoPage, "records");
        List<Dish> records = pageinfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //分类ID
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }

//    @GetMapping("/list")
//    public R<String> addDish(HttpServletRequest request, @RequestBody Dish dish) {
//        Long empId = (Long) request.getSession().getAttribute("employee");
////        dish.setUpdateTime(LocalDateTime.now());
////        dish.setCreateTime(LocalDateTime.now());
////        dish.setCreateUser(empId);
////        dish.setUpdateUser(empId);
//
//        BaseContext.setThreadLocal(empId);
//        dishService.updateById(dish);
//        return R.success("修改成功！");
//    }

//    @GetMapping("/list")
//    public R<List<Dish>> listDish(Dish dish) {
//        Long categoryId = dish.getCategoryId();
//        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(categoryId != null, Dish::getCategoryId, categoryId);
//        //查询状态为1
//        lqw.eq(Dish::getStatus, 1);
//        //添加排序条件
//        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(lqw);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> listDish(Dish dish) {
        List<DishDto> dishDtosList = null;
        //动态构造key
        //dish_13523....._1
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        //先从redis中获取缓存数据
        dishDtosList = (List<DishDto>)redisTemplate.opsForValue().get(key);

        //如果存在，直接返回，无需查询数据i库
        if (dishDtosList != null) {
            return R.success(dishDtosList);
        }



        Long categoryId = dish.getCategoryId();
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId != null, Dish::getCategoryId, categoryId);
        //查询状态为1
        lqw.eq(Dish::getStatus, 1);
        //添加排序条件
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lqw);

        dishDtosList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId1 = item.getCategoryId();

            Category category = categoryService.getById(categoryId1);

            if (category != null) {
                String name = category.getName();
                dishDto.setCategoryName(name);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> QueryWrapper = new LambdaQueryWrapper<>();
            QueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(QueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtosList,60, TimeUnit.MINUTES);

        return R.success(dishDtosList);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * 根据id来查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<DishDto> getDish(@PathVariable Long id) {
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);

        //清理所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改成功！");
    }

    //    @PostMapping("/status/{status}")
//    public R<String> changeStatus(@PathVariable Integer status, Long ids) {
//        Dish dish = dishService.getById(ids);
//        dish.setStatus(status);
//        dishService.updateById(dish);
//        return R.success("修改成功！");
//    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam("ids") Long[] ids) {
        log.info("菜品信息：{}，状态：{}", ids, status);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId, ids);
        List<Dish> list = dishService.list(lqw);
        list.forEach(dish -> {
            dish.setStatus(status);
        });

        dishService.updateBatchById(list);


        return R.success("修改成功!");
    }

    @DeleteMapping
    public R<String> deleteDish(Long ids) {
        dishService.removeById(ids);
        return R.success("删除成功！");
    }
}
