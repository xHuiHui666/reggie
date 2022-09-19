package com.wolf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolf.common.R;
import com.wolf.domain.Category;
import com.wolf.domain.Setmeal;
import com.wolf.dto.SetmealDto;
import com.wolf.service.CategoryService;
import com.wolf.service.SetmealDishService;
import com.wolf.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
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

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息: {}",setmealDto);
          setmealService.saveWithDish(setmealDto);

        return R.success("套餐新增成功^_^!");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        // 设置查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper
                                        = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        // 拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<SetmealDto> list =  pageInfo.getRecords().stream().map((item) ->{
             SetmealDto setmealDto = new SetmealDto();

             // 拷贝对象
             BeanUtils.copyProperties(item,setmealDto);

             // 得到分类id,并查询该id对应的分类对象
             Category category = categoryService.getById(item.getCategoryId());
             if (category != null){
                // 如果分类对象不为null,则将其分类名称赋值给setmealDto对象中
                 setmealDto.setCategoryName(category.getName());
             }
            return setmealDto;
         }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }


    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids: {}",ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功^_^!");
    }


    /**
     * 返回套餐集合
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper
                                      = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId())
                     .eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus())
                       .orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        return R.success(setmealList);
    }





}
