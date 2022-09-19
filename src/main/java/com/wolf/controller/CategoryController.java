package com.wolf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolf.common.R;
import com.wolf.domain.Category;
import com.wolf.domain.Employee;
import com.wolf.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存菜单
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("商品添加");
        boolean b = categoryService.save(category);
        return b == true?R.success("添加成功^_^!"):R.error("添加失败-_-!");
    }

    /**
     *  分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        log.info("分页查询菜单");
        Page<Category> pageInfo = new Page<>(page,pageSize);
        // 设置查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        // 执行条件
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除商品分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除商品分类id: {}",id);
        categoryService.remove(id);

        return R.success("删除成功^_^!");


    }

    /**
     * 根据id修改分类信息
     * @param
     * @return
     */

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息: {}",category);
        boolean b = categoryService.updateById(category);
        return b == true?R.success("修改成功^_^!"):R.error("修改失败-_-!");
    }


    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq( category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        // 返回结果
        return R.success(list);
    }


}
