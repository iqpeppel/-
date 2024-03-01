package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /*
    新增菜品和口味
     */
        public   void  saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFalvor(Long id);
//修改菜品
    void updateWithFalvor(DishDTO dishDTO);
    /**
     *根据菜品查询口味
     */
    List<DishVO> listWithFlavor(Dish dish);

    List<Dish> getBycategoryId(Long categoryId);
}
