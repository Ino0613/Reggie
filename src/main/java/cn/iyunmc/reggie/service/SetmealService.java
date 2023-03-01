package cn.iyunmc.reggie.service;

import cn.iyunmc.reggie.dto.SetmealDto;
import cn.iyunmc.reggie.entity.Setmeal;
import cn.iyunmc.reggie.mapper.SetmealMapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param id
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 获取修改列表
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);
}


