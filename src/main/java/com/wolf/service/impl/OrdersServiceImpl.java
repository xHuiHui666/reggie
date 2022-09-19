package com.wolf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.common.BaseContext;
import com.wolf.common.CustomException;
import com.wolf.domain.*;
import com.wolf.service.*;
import com.wolf.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
* @author 007
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-09-16 18:11:14
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;



    /**
     * 用户下单
     * @param orders
     */
    @Override
    public void submit(Orders orders) {
         // 获取当前用户id
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper
                                             = new LambdaQueryWrapper<>();
        // 根据当前用户id查询该购物车的数据
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        // 执行条件
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        if (shoppingCartList == null || shoppingCartList.size() == 0){
            // 证明当前用户购物车为空,此时抛出一个业务异常
            throw new CustomException("购物车为空,不能下单");
        }

        // 查询用户信息
        User user = userService.getById(userId);
        // 查询地址数据
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null){
            throw new CustomException("地址信息为空,不能下单");
        }

        // 向订单表插入数据,一条数据
        long orderId = IdWorker.getId();

        // 总金额
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails =  shoppingCartList.stream().map((item) ->{
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setNumber(item.getNumber());
        orderDetail.setDishFlavor(item.getDishFlavor());
        orderDetail.setDishId(item.getDishId());
        orderDetail.setSetmealId(item.getSetmealId());
        orderDetail.setName(item.getName());
        orderDetail.setImage(item.getImage());
        orderDetail.setAmount(item.getAmount());
        amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
        return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(new Date());
        orders.setCheckoutTime(new Date());
        // 2为待派送
        orders.setStatus(2);
        // 总金额
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        // 设置订单号
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(user.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        // 向订单表插入数据
        this.save(orders);

        // 向订单明细表中插入数据,多条数据
        orderDetailService.saveBatch(orderDetails);

        // 下完单后,清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }

    /**
     * 查看订单,将数量赋值给orderDto对象
     * @param orderDto
     */
//    @Override
//    public Page<OrderDto> listOrder(OrderDto orderDto) {
//        // 得到当前用户的id
//        Long id = BaseContext.getCurrentId();
//        // 根据id查询明细,从order_detail中
//        LambdaQueryWrapper<OrderDetail> queryWrapper
//                = new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrderDetail::getOrderId,id);
//        // 查询order表
//        Page<Orders> ordersPage = new Page<>(orderDto.getPage(),orderDto.getPageSize());
//        this.page(ordersPage);
//
//        // 构建orderDto的page对象
//        Page<OrderDto> orderDtoPage = null;
//        BeanUtils.copyProperties(ordersPage,orderDtoPage);
//        OrderDetail orderDetail = orderDetailService.getOne(queryWrapper);
//        orderDtoPage.getRecords().stream().map((item) ->{
//            item.setSumNum(orderDetail.getNumber());
//            return item;
//        });
//        return orderDtoPage;
//    }
}




