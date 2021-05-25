package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单后台管理Controller
 */
@RestController
public class OrderAdminController {
  @Autowired
  OrderService orderService;

  @GetMapping("admin/order/list")
  @ApiOperation("管理员订单列表")
  public ApiRestResponse listForAdmin(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
    PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
  }

  @PostMapping("pay")
  @ApiOperation("支付接口")
  public ApiRestResponse pay(String orderNo) {
    orderService.pay(orderNo);
    return ApiRestResponse.success();
  }

  /**
   * 发货 订单状态流程： 0：用户已取消 10：未付款 20：已付款 30：已发货 40：交易完成
   * @param orderNo
   * @return
   */
  @PostMapping("admin/order/delivered")
  @ApiOperation("管理员发货")
  public ApiRestResponse delivered(String orderNo) {
    orderService.delivered(orderNo);
    return ApiRestResponse.success();
  }

  /**
   * 完结订单 管理员及用户都可调用
   * @param orderNo
   * @return
   */
  @PostMapping("order/finish")
  @ApiOperation("完结订单")
  public ApiRestResponse finish(String orderNo) {
    orderService.finish(orderNo);
    return ApiRestResponse.success();
  }
}
