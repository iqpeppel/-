package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServicelmpl implements DishService {
    @Autowired
    private   DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
   /*
   新增菜品和口味
    */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //菜品表插入一个
        //口味表插入多个
        dishMapper.insert(dish);
        //获取insert的主键
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()!=0){
            flavors .forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
        }
        dishFlavorMapper.insertBatch(flavors);

    }
/*
菜品分页查询
 */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page  = dishMapper.pageQuery(dishPageQueryDTO);
        return  new PageResult(page.getTotal() ,page.getResult());
    }

    /*
    菜品的批量删除
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否可以被删除
        for ( Long id : ids){

         Dish  dish  =  dishMapper.getById(id);
         //起售中的菜品不能被删除
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //被套餐关联的菜品不能被删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null && setmealIds.size()>0){
            //菜品被套餐关联了
            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
       /* for (Long id : ids) {
             dishMapper.deleteById(id);
            //删除相关联的口味数据
            dishFlavorMapper.deleteByDishId(id);
        }*/
        //批量删除菜品
        dishMapper.deleteByIds(ids);
        //批量删除口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /*
    根据id查寻口味和菜品
     */
    @Override
    public DishVO getByIdWithFalvor(Long id) {
        //根据id查询菜品
        Dish dish  = dishMapper.getById(id);
        //根据id查询口味数据
      List<DishFlavor> dishFlavors  = dishFlavorMapper.getByDishId(id);
      //将查询到的数据封装到VO中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
/*
修改菜品
 */
    @Override
    public void updateWithFalvor(DishDTO dishDTO) {
        Dish dish = new Dish();
        //修改菜品
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除原来的口味
        dishFlavorMapper.deleteByDishId(dish.getId());
        //插入新的口味进去
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()!=0){
            flavors .forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
        }
        dishFlavorMapper.insertBatch(flavors);
    }
/*
查询菜品何和味
 */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);


            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
/*
根据类别名查询菜品
 */
    @Override
    public List<Dish> getBycategoryId(Long categoryId) {
        List<Dish> list = dishMapper.getBycategoryId(categoryId);
        return list;
    }


}
