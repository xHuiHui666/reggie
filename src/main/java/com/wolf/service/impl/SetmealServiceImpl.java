package com.wolf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.common.CustomException;
import com.wolf.domain.Setmeal;
import com.wolf.domain.SetmealDish;
import com.wolf.dto.SetmealDto;
import com.wolf.mapper.SetmealMapper;
import com.wolf.service.DishService;
import com.wolf.service.SetmealDishService;
import com.wolf.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐, 同时要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息,操作setmeal
        this.save(setmealDto);

        // 得到该套餐下的所有菜品对象
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
       setmealDishes = setmealDishes.stream().map((item) ->{
            // 因为mybatis-plus框架添加套餐后,就会将雪花算法生成的dishId保存到setmealDto对象中
             item.setSetmealId(setmealDto.getId().toString());
             return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联关系, 操作setmeal_dish
         setmealDishService.saveBatch(setmealDishes);



    }

    /**
     * 批量删除套餐, 同时删除套餐和菜品的关联关系
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        // 1.查询套餐状态, 确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper
                                     = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0){
            // 2. 如果不能删除, 则抛出业务异常
            throw new CustomException("套餐正在售卖中,不能删除!!!");
        }

        // 3.如果可以删除,先删除套餐表中的数据
        this.removeByIds(ids);

        // 4.删除关系表的菜品数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
