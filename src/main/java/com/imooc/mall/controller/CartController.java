package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {
  @Autowired
  CartService cartService;

  @ApiOperation("购物车列表")
  @GetMapping("/list")
  public ApiRestResponse list() {
    // 内部获取用户Id，防止横向越权
    List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
    return ApiRestResponse.success(cartVOList);
  }

  @ApiOperation("添加商品到购物车")
  @PostMapping("/add")
  public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
    List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
    return ApiRestResponse.success(cartVOList);
  }

  @ApiOperation("更新购物车")
  @PostMapping("/update")
  public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
    List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
    return ApiRestResponse.success(cartVOList);
  }

  @ApiOperation("删除购物车")
  @PostMapping("/delete")
  public ApiRestResponse delete(@RequestParam Integer productId) {
    List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
    return ApiRestResponse.success(cartVOList);
  }

  @PostMapping("/select")
  @ApiOperation("选择/不选择购物车的某商品")
  public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
    //不能传入userID，cartID，否则可以删除别人的购物车
    List<CartVO> cartVOList = cartService
        .selectOrNot(UserFilter.currentUser.getId(), productId, selected);
    return ApiRestResponse.success(cartVOList);
  }

  @PostMapping("/selectAll")
  @ApiOperation("全选择/全不选择购物车的某商品")
  public ApiRestResponse selectAll(@RequestParam Integer selected) {
    //不能传入userID，cartID，否则可以删除别人的购物车
    List<CartVO> cartVOList = cartService
        .selectAllOrNot(UserFilter.currentUser.getId(), selected);
    return ApiRestResponse.success(cartVOList);
  }
}
