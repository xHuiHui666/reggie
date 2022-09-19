package com.wolf.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolf.common.R;
import com.wolf.domain.Category;
import com.wolf.domain.Dish;
import com.wolf.domain.DishFlavor;
import com.wolf.dto.DishDto;
import com.wolf.service.CategoryService;
import com.wolf.service.DishFlavorService;
import com.wolf.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 保存菜品
     *
     * @param dishDto
     * @return
     */

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("添加菜品: {}", dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        // 删除缓存
        redisTemplate.delete(key);
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功^_^!");
    }

    /**
     * 菜品信息分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);

        // 添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<DishDto> list = pageInfo.getRecords().stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            // 得到分类id
            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        // 将记录存放到dishDto中
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id 查询对应的商品和该商品的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithDishFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品: {}", dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        // 删除缓存
        redisTemplate.delete(key);
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功^_^!");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, long[] ids) {
        log.info("对商品状态批量更改");
        dishService.updateStatus(status, ids);
        return R.success("修改成功^_^!");
    }

    /**
     * 批量删除菜品和菜品味道
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(long[] ids) {
        dishService.deleteBatch(ids);
        return R.success("删除成功^_^!");
    }

//    /**
//     * 套餐显示 (添加套餐窗口)
//     *
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        // 添加条件
//        LambdaQueryWrapper<Dish> queryWrapper
//                = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        // 添加条件, 查询状态为1(启售状态)的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        // 添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        // 执行条件,返回对应的list集合
//        List<Dish> dishes = dishService.list(queryWrapper);
//
//        return R.success(dishes);
//    }

    /**
     * 套餐显示 (添加套餐窗口)
     *
     * @param dishDto
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(DishDto dishDto) {
        List<DishDto> dishDtoList = null;
        // key: dish_123231_1
       String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        // 从redis中获取缓存数据
         dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

         if (dishDtoList != null && dishDtoList.size()>0){
             // 如果存在, 直接返回, 无需查询数据库
             return R.success(dishDtoList);
         }
        // 如果不存在, 查询数据库, 并将查询到的菜品缓存到redis中
        // 添加条件
        LambdaQueryWrapper<Dish> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDto.getCategoryId() != null, Dish::getCategoryId, dishDto.getCategoryId());
        // 添加条件, 查询状态为1(启售状态)的菜品
        queryWrapper.eq(Dish::getStatus,1);
        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 执行条件,返回对应的list集合
        List<Dish> list = dishService.list(queryWrapper);

         dishDtoList = list.stream().map((item) ->{
            DishDto dishdto = new DishDto();

            BeanUtils.copyProperties(item,dishdto);

            Long categoryId = item.getCategoryId();
            // 根据id查询对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishdto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishdto.setFlavors(dishFlavors);

           return dishdto;
        }).collect(Collectors.toList());

         // 将从数据库查询后的菜品缓存到redis中
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

}
