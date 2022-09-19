package com.wolf.mapper;

import com.wolf.domain.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-09-16 12:21:07
* @Entity com.wolf.domain.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




