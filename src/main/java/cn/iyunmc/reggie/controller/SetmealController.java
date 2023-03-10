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
        //???????????????????????????name??????like????????????
        lqw.like(name != null, Setmeal::getName, name);
        //???????????????????????????????????????????????????
        lqw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo, lqw);

        //????????????
        BeanUtils.copyProperties(pageinfo, setmealDtoPage, "records");
        List<Setmeal> records = pageinfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            //??????id
            Long categoryId = item.getCategoryId();
            //????????????id??????????????????
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //????????????
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * ????????????
     *
     * @param setmealDto
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto, HttpServletRequest request) {
        log.info("????????????: {}", setmealDto);
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        setmealService.saveWithDish(setmealDto);

        return R.success("?????????????????????");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> update(@PathVariable Long id) {
        log.info("????????????: {}", id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("????????????: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("???????????????");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("????????????: {} , {}", ids, status);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        // Step 1: ???????????????????????????Setmeal????????????????????????????????????List??????
        List<Setmeal> list = setmealService.list(lqw);
        // Step 2: ??????List????????????Setmeal?????????????????????????????????
        list.forEach(setmeal -> {
            // ??????????????????Setmeal???????????????????????????????????????????????????????????????????????????
            if (setmeal.getStatus() != status) {
                setmeal.setStatus(status);
            }
        });
        // ????????????????????????
        boolean b = setmealService.updateBatchById(list);
        System.out.println(b);
        return R.success("???????????????");
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("???????????????");
    }

    /**
     * ??????????????????????????????
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
