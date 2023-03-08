package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.dto.SetmealDto;
import cn.iyunmc.reggie.entity.Category;
import cn.iyunmc.reggie.entity.Setmeal;
import cn.iyunmc.reggie.entity.SetmealDish;
import cn.iyunmc.reggie.service.CategoryService;
import cn.iyunmc.reggie.service.SetmealDishService;
import cn.iyunmc.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    //    @GetMapping("/page")
//    public R<Page> getAll1(int page, int pageSize, String name) {
//        Page<Setmeal> pageinfo = new Page<>(page, pageSize);
//        Page<SetmealDto> setmealDtoPage = new Page<>();
//        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
//        lqw.like(name != null, Setmeal::getName, name);
//        lqw.orderByDesc(Setmeal::getUpdateTime);
//        setmealService.page(pageinfo, lqw);
//        BeanUtils.copyProperties(pageinfo, setmealDtoPage, "records");
//        List<Setmeal> records = pageinfo.getRecords();
//        List<SetmealDto> list = records.stream().map((item) -> {
//            SetmealDto setmealDto = new SetmealDto();
//            Long categoryId = item.getCategoryId();
//            BeanUtils.copyProperties(item, setmealDto);
//            Category category = categoryService.getById(categoryId);
//            String categoryname = category.getName();
//            setmealDto.setCategoryName(categoryname);
//            return setmealDto;
//
//        }).collect(Collectors.toList());
//        setmealDtoPage.setRecords(list);
//        return R.success(setmealDtoPage);
//    }
    @GetMapping("/page")
    public R<Page> getAll(int page, int pageSize, String name) {
        Page<Setmeal> pageinfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();


        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        lqw.like(name != null, Setmeal::getName, name);
        //添加排序条件，根据更新时间降序排列
        lqw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo, lqw);

        //对象拷贝
        BeanUtils.copyProperties(pageinfo, setmealDtoPage, "records");
        List<Setmeal> records = pageinfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto, HttpServletRequest request) {
        log.info("套餐信息: {}", setmealDto);
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功！");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> update(@PathVariable Long id) {
        log.info("套餐信息: {}", id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("套餐信息: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("套餐信息: {} , {}", ids, status);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        // Step 1: 查询所有符合条件的Setmeal对象，并将结果存储在一个List中。
        List<Setmeal> list = setmealService.list(lqw);
        // Step 2: 遍历List，对每个Setmeal对象执行状态更新操作。
        list.forEach(setmeal -> {
            // 如果查询到的Setmeal对象的状态值不等于要更改的状态值，则进行更改操作。
            if (setmeal.getStatus() != status) {
                setmeal.setStatus(status);
            }
        });
        // 执行批量更新操作
        boolean b = setmealService.updateBatchById(list);
        System.out.println(b);
        return R.success("更新成功！");
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("更新成功！");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lqw.eq(Setmeal::getStatus, setmeal.getStatus());
        lqw.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(lqw);

        return R.success(setmealList);
    }
}
