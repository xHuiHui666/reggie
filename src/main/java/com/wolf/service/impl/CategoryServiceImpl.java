package com.wolf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.common.CustomException;
import com.wolf.domain.Category;
import com.wolf.domain.Dish;
import com.wolf.domain.Setmeal;
import com.wolf.service.CategoryService;
import com.wolf.mapper.CategoryMapper;
import com.wolf.service.DishService;
import com.wolf.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-09-13 15:39:52
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{


    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类 , 删除之前需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件,根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        // 1.查询当前分类是否关联了菜品, 如果已经关联, 则抛出异常
        if (count1>0){
            // 说明已经关联了菜品, 抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除!");
        }

        // 2.查询当前套餐是否关联了菜品, 如果已经关联, 则抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            // 已经关联菜单, 抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除!");
        }

        // 3.正常删除
        super.removeById(id);

    }
}




