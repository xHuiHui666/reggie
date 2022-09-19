package com.wolf.mapper;

import com.wolf.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-09-13 15:39:52
* @Entity com.wolf.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




