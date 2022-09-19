package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.OrderDetail;
import com.wolf.service.OrderDetailService;
import com.wolf.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-16 18:11:14
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




