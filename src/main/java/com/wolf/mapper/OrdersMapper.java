package com.wolf.mapper;

import com.wolf.domain.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2022-09-16 18:11:14
* @Entity com.wolf.domain.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




