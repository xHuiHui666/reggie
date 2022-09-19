package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.SetmealDish;
import com.wolf.service.SetmealDishService;
import com.wolf.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service实现
* @createDate 2022-09-15 10:00:10
*/
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
    implements SetmealDishService{
}




