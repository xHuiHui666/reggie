package com.wolf.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wolf.common.BaseContext;
import com.wolf.common.CustomException;
import com.wolf.common.R;
import com.wolf.domain.ShoppingCart;
import com.wolf.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 购物车模块
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据: {}",shoppingCart);
       // 设置当前用户id, 指定是哪个用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件, id为当前用户
         queryWrapper.eq(ShoppingCart::getUserId,userId);
        if (dishId != null){
            // 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else {
            // 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 执行查询条件, 结果封装到一个ShoppingCart对象中
        ShoppingCart shoppingCartFromMySql = shoppingCartService.getOne(queryWrapper);

        if (shoppingCartFromMySql != null){
            // 证明用户已经添加到购物车了,此时应该在number上加一
            Integer number = shoppingCartFromMySql.getNumber();
            shoppingCartFromMySql.setNumber(number + 1);
            // 执行更新操作
            shoppingCartService.updateById(shoppingCartFromMySql);
        }else {
            // 还没有添加到购物车, 此时应该添加
            // 首先将该购物车数量设置为1
            shoppingCart.setNumber(1);
            // 执行添加操作
            shoppingCart.setCreateTime(new Date());
            shoppingCartService.save(shoppingCart);
            // 将结果赋值给该对象
            shoppingCartFromMySql = shoppingCart;
        }

        // 将结果返回
        return R.success(shoppingCartFromMySql);

    }


    /**
     * 从购物车中删除
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("从购物车中删除: {}",shoppingCart);
        // 设置当前用户id, 指定是哪个用户操作
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 查询当前菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        if (dishId != null){
            // 表示删除的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 表示删除的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // select * from shopping_cart where setmeal_id = ? and user_id = ?
        ShoppingCart shoppingFromMySql = shoppingCartService.getOne(queryWrapper);

        if (shoppingFromMySql != null){
            // 证明该用户已经添加过该商品到购物车中
            // 此时应该将number-1
            Integer number = shoppingFromMySql.getNumber();
            if (number <=0){
                // 此时该商品还没有添加过,不能进行删除操作
                // 抛出一个业务异常
                throw new CustomException("您还未添加到购物车中!");
            }
            shoppingFromMySql.setNumber(number - 1);
            // 执行更改
            shoppingCartService.updateById(shoppingFromMySql);
        }else {
            // 此时证明该用户是第一次进行删除,此时也应该抛出一个业务异常
            throw new CustomException("您还未添加到购物车中!");
        }
        return R.success(shoppingFromMySql);

    }


    /**
     * 查看购物车
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper
                                            = new LambdaQueryWrapper<>();
        // 设置条件,当前用户id和创建时间升序排序
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        // 构建条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper
                                           = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车已清空!");
    }

}
