package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /*
    批量插入口味
     */
    void insertBatch(List<DishFlavor> flavors);
/*
根据dishid删除口味
 */
    @Delete("delete  from  dish_flavor where  dish_id = #{DishId}")
    void deleteByDishId(Long DishId);

    /*
    根据菜品id批量删除口味
     */
    void deleteByDishIds(List<Long> DishIds);
/*
根据菜品id查寻口味
 */
    @Select("SELECT  * FROM  dish_flavor WHERE  dish_id  = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
