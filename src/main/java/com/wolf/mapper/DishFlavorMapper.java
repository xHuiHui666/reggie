package com.wolf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolf.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2022-09-14 12:17:01
* @Entity com.wolf.domain.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




