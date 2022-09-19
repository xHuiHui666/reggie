package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.AddressBook;
import com.wolf.service.AddressBookService;
import com.wolf.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-09-16 12:21:07
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




