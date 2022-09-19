package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.DishFlavor;
import com.wolf.service.DishFlavorService;
import com.wolf.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-09-14 12:17:01
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService{

}




