package com.wolf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolf.common.BaseContext;
import com.wolf.common.R;
import com.wolf.domain.OrderDetail;
import com.wolf.domain.Orders;
import com.wolf.dto.OrdersDto;
import com.wolf.service.OrderDetailService;
import com.wolf.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单模块
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据: {}",orders);

        ordersService.submit(orders);
     return R.success("下单成功^_^!");
    }

    /**
     * 查看订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> page(int page, int pageSize){
       Page<Orders> ordersPage = new Page<>(page,pageSize);
       LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
       ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
       ordersService.page(ordersPage,ordersLambdaQueryWrapper);

       Page<OrdersDto> orderDtoPage = new Page<>();

        BeanUtils.copyProperties(ordersPage,orderDtoPage,"records");
       // 遍历ordersPage 中的记录 并依次将item对象拷贝到orderDto对象中
    List<OrdersDto> list= ordersPage.getRecords().stream().map((item) ->{
        OrdersDto ordersDto = new OrdersDto();
        LambdaQueryWrapper<OrderDetail> queryWrapper
                                             = new LambdaQueryWrapper<>();
           queryWrapper.eq(OrderDetail::getOrderId,item.getId());
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
//        OrderDetail orderdetail = orderDetailService.getOne(queryWrapper);
        // 设置属性值到orderDto中
        ordersDto.setOrderDetails(orderDetails);
        BeanUtils.copyProperties(item,ordersDto);
        return ordersDto;
    }).collect(Collectors.toList());
       // 将上面遍历出的集合设置到orderDtoPage的records中
       orderDtoPage.setRecords(list);
        // 返回该对象
        return R.success(orderDtoPage);
    }

}
