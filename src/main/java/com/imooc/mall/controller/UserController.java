package com.imooc.mall.controller;

/**
 * 描述：用户控制器
 */

import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping("/test")
  @ResponseBody
  public User personalPage() {
    return userService.getUser();
  }
}
