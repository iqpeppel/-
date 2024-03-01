package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserSercicelmpl implements UserService {
    //微信服务接口
    public  static  final  String   wx_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper ;
    /*
    微信登录
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginVO) {
//调用getopenid方法
        String openid = getopenid(userLoginVO.getCode());
        //判断是否正确
        if(openid == null)   //抛出业务异常
            throw  new LoginFailedException(MessageConstant.LOGIN_FAILED);
        //是否为新用户
        User user = userMapper.getByopenId(openid);
        if(user == null){
             user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
             userMapper.insert(user);
        }
        //完成自动注册
        //返回用户

        return user;
    }
    private  String getopenid(String code){
        //调用微信接口
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json  =   HttpClientUtil.doGet(wx_LOGIN,map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return  openid;
    }
}
