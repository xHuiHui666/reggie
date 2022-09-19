package com.wolf.mapper;

import com.wolf.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-09-16 13:35:26
* @Entity com.wolf.domain.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




