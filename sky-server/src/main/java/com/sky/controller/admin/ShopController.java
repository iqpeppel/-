package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("adminShopController")
@Api(tags = "店家管理")
@RequestMapping("/admin/shop")
public class ShopController {
    public  static  final  String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    设置店铺的营业状态
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus( @PathVariable Integer status){
        log.info("设置店铺的营业状态:{}",status== 1 ?"营业中":"打洋");
        redisTemplate.opsForValue().set(KEY,status);
            return  Result.success();
    }
    /*
    获取营业状态
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public  Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺的营业状态:{}",status== 1 ?"营业中":"打洋");
        return  Result.success(status);
    }

}
