package com.wolf.service;

import com.wolf.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 007
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-09-16 18:11:14
*/
public interface OrdersService extends IService<Orders> {

    // 用户下单
    void submit(Orders orders);

    // 查看订单,将数量赋值给orderDto对象
//    Page<OrderDto>  listOrder(OrderDto orderDto);

}
