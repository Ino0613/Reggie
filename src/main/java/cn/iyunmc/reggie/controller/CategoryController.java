package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.Category;
import cn.iyunmc.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> categoryList(int page, int pageSize, String name) {

        Page<Category> pageinfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name), Category::getName, name);
        lqw.orderByAsc(Category::getSort);
        categoryService.page(pageinfo, lqw);
        return R.success(pageinfo);

    }

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {

        categoryService.save(category);

        return R.success("新增菜品成功！");
    }

    @DeleteMapping
    public R<String> deleteByIds(Long ids) {
        log.info("成功删除分类 {}", ids);
        if (ids != null) {
            categoryService.remove(ids);
        }
        return R.success("删除成功!");
    }

    /**
     * 根据id修改分类信息
     *
     * @param request
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateByIds(HttpServletRequest request, @RequestBody Category category) {
        if (category != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setThreadLocal(empId);
            categoryService.updateById(category);
        }
        return R.success("修改成功!");
    }

    @GetMapping("/list")
    public R<List<Category>> getCategoryId(Category category) {

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null, Category::getType, category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }
}
