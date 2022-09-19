package com.wolf.service;

import com.wolf.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-09-13 15:39:52
*/
public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
