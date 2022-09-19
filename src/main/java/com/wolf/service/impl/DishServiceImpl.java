package com.wolf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.common.CustomException;
import com.wolf.domain.Dish;
import com.wolf.domain.DishFlavor;
import com.wolf.dto.DishDto;
import com.wolf.mapper.DishMapper;
import com.wolf.service.DishFlavorService;
import com.wolf.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品, 同时保存口味信息
     * @param dishDto
     */

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
       // 保存菜品的基本信息到dish表
        this.save(dishDto);

        // 菜品id
        Long dishId = dishDto.getId();

        // 保存菜品口味到dish_flavor表
        List<DishFlavor> flavors = dishDto.getFlavors();
      flavors =  flavors.stream().map((item) ->{
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());

      dishFlavorService.saveBatch(flavors);

    }

    /**
     *  根据 id 查询 商品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithDishFlavor(Long id) {
      // 1.查询商品基本信息, 从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        // 2.查询该商品对应的口味信息, 从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> queryWrapper
                                        = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        // 3.将口味设置到 dishDto对象中
        dishDto.setFlavors(dishFlavors);

        // 4.返回该对象
        return dishDto;
    }

    /**
     * 更新口味信息和商品信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
       // 1.更新dish表
        this.updateById(dishDto);

       // 2.清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper
                                        = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            // 将当前商品的id设置到该口味中
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 3.添加当前提交过来的口味数据
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 批量启售和停售
     * @param status
     */
    @Override
    public void updateStatus(int status,long[] ids) {

        if (status == 1) {
            // 启售
            for (Long id : ids) {
                Dish dish = this.getById(id);
                dish.setStatus(1);
                this.updateById(dish);
            }
            return;
        }else if (status == 0){
            // 停售
            for (Long id : ids) {
                Dish dish = this.getById(id);
                dish.setStatus(0);
                this.updateById(dish);
            }
            return;
        }

        // 如果都不是两个情况,则抛出异常
        throw new CustomException("未知错误!");

    }

    /**
     * 批量删除菜品和对应菜品的信息
     * @param ids
     */
    @Override
    public void deleteBatch(long[] ids) {

        for (long id : ids) {
            // 删除菜品口味
            LambdaQueryWrapper<DishFlavor> queryWrapper
                                             = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id);
           dishFlavorService.remove(queryWrapper);
           // 删除菜品
           this.removeById(id);
        }

    }

}
