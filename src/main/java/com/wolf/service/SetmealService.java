package com.wolf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolf.domain.Setmeal;
import com.wolf.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    // 新增套餐, 同时要保存套餐和菜品的关联关系
    void saveWithDish(SetmealDto setmealDto);

    // 批量删除套餐
    void removeWithDish(List<Long> ids);
}
