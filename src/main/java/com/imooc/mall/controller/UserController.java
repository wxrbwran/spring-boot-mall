package com.imooc.mall.controller;

/**
 * 描述：用户控制器
 */

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping("/test")
  @ResponseBody
  public User personalPage() {
    return userService.getUser();
  }

  @PostMapping("/register")
  @ResponseBody
  public ApiRestResponse register(@RequestBody Map<String, String> map) throws ImoocMallException {
    String userName = map.get("userName");
    String password = map.get("password");

    if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }
    if (password.length() < 8) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
    }
    userService.register(userName, password);
    return ApiRestResponse.success();
  }
  @PostMapping("/login")
  @ResponseBody
  public ApiRestResponse login(@RequestParam("userName") String userName,
                               @RequestParam("password") String password,
                               HttpSession session) throws ImoocMallException {
    if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }
    User user = null;
    user = userService.login(userName, password);
    user.setPassword(null);
    session.setAttribute(Constant.IMOOC_MALL_USER, user);
    return ApiRestResponse.success(user);
  }

  @PostMapping("/user/update")
  @ResponseBody
  public  ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws ImoocMallException {
    User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    User user = new User();
    user.setId(currentUser.getId());
    user.setPersonalizedSignature(signature);
    userService.updateInfomation(user);
    return ApiRestResponse.success();
  }

  @PostMapping("/user/logout")
  @ResponseBody
  public ApiRestResponse logout(HttpSession session){
    session.removeAttribute(Constant.IMOOC_MALL_USER);
    return ApiRestResponse.success();
  }

  @PostMapping("/adminLogin")
  @ResponseBody
  public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                               @RequestParam("password") String password,
                               HttpSession session) throws ImoocMallException {
    if (StringUtils.isEmptyOrWhitespaceOnly(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }
    User user = null;
    user = userService.login(userName, password);
    if (userService.checkAdminRole(user)) {
      // 是管理员，执行操作
      user.setPassword(null);
      session.setAttribute(Constant.IMOOC_MALL_USER, user);
      return ApiRestResponse.success(user);
    }
    return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
  }

}
