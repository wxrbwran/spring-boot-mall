package com.imooc.mall.service.impl;

/**
 * 描述： UserService实现类
 */

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserMapper userMapper;

  @Override
  public User getUser() {
    return userMapper.selectByPrimaryKey(1);
  }

  @Override
  public void register(String userName, String password) throws ImoocMallException {
    // 查询重名
    User result = userMapper.selectByName(userName);
    if (result != null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
    }

    // 写入数据库
    User user = new User();
    user.setUsername(userName);
    try {
      user.setPassword(MD5Utils.getMD5String(password));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    int count = userMapper.insertSelective(user);

    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
    }
  }

  @Override
  public User login(String userName, String password) throws ImoocMallException {
    String md5Password = null;
    try {
      md5Password = MD5Utils.getMD5String(password);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    User user = userMapper.selectLogin(userName, md5Password);
    if (user == null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
    }
    return user;
  }

  @Override
  public void updateInfomation(User user) throws ImoocMallException {
    int updateCount = userMapper.updateByPrimaryKeySelective(user);
    if (updateCount > 1) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
    }
  }

  @Override
  public boolean checkAdminRole(User user) {
    // 1: 普通用户 2: 管理员
    boolean equals = user.getRole().equals(2);
    return equals;
  }
}
