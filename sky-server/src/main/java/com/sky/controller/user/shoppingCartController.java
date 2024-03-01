package com.sky.controller.user;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.impl.ShoppingCartServidelmpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user/shoppingCart")
@RestController
@Slf4j
@Api(tags = "购物车相关接口")
public class shoppingCartController {
    @Autowired
    private ShoppingCartServidelmpl shoppingCartServidelmpl ;
    /*
    添加购物车
     */
    @PostMapping("add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO  shoppingCartDTO){
        log.info("添加购物车,{}",shoppingCartDTO);
        shoppingCartServidelmpl.addshopingCart(shoppingCartDTO);
        return  Result .success();
    }
    /*
    查看购物车
     */
    @GetMapping("list")
    @ApiOperation("查看购物车")
    public  Result<List<ShoppingCart>>list(){
        log.info(" 查看购物车");
        List<ShoppingCart> list = shoppingCartServidelmpl.list();
        return  Result.success(list);

    }
      /*
        清空购物车
         */
    @DeleteMapping("clean")
    @ApiOperation(" 清空购物车")
    public  Result delete(){
        log.info("清空购物车");
        shoppingCartServidelmpl.deleteByUserId();
        return  Result.success();
    }
}
