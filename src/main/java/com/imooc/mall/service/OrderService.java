package com.imooc.mall.service;

import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;

import java.util.List;

public interface OrderService {

  String create(CreateOrderReq createOrderReq);
}
