package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.ShoppingCart;
import com.wolf.service.ShoppingCartService;
import com.wolf.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-09-16 13:35:26
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




