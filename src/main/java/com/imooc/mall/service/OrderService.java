package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderVO;

import java.util.List;

public interface OrderService {

  String create(CreateOrderReq createOrderReq);

  OrderVO detail(String orderNo);

  PageInfo listForCustomer(Integer pageNum, Integer pageSize);

  void cancel(String orderNo);

  String qrcode(String orderNo);

  PageInfo listForAdmin(Integer pageNum, Integer pageSize);

  void pay(String orderNo);

  void delivered(String orderNo);

  void finish(String orderNo);
}
