package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单Controller
 */
@RestController
public class OrderController {
  @Autowired
  OrderService orderService;

  @PostMapping("order/create")
  @ApiOperation("创建订单")
  public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) {
    String orderNum = orderService.create(createOrderReq);
    return ApiRestResponse.success(orderNum);
  }
}
