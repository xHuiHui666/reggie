package com.wolf.mapper;

import com.wolf.domain.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-09-16 18:11:14
* @Entity com.wolf.domain.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




