package com.wolf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolf.domain.Dish;
import com.wolf.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {

    // 新增菜品, 同时插入菜品对应的口味,操作两张表,dish和dish_flavor
    void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和口味信息
    DishDto getByIdWithDishFlavor(Long id);

    // 更新口味信息和商品信息
    void updateWithFlavor(DishDto dishDto);

    // 批量启售和停售
    void updateStatus(int status, long[] ids);

    // 批量删除商品
    void deleteBatch(long[] ids);

}
