package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /*
    添加相关的购物车
     */
    void  addshopingCart(ShoppingCartDTO shoppingCartDTO );

    /*
    查看购物车
     */
    List<ShoppingCart> list();

    /*
     清空购物车
     */
    void deleteByUserId();
}
