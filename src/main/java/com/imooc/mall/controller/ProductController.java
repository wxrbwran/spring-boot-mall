package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;

import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 前台商品Controller
 */
@RestController
public class ProductController {
  @Autowired
  ProductService productService;

  @ApiOperation("商品详情")
  @GetMapping("product/detail")
  public ApiRestResponse detail(@RequestParam Integer id){
    Product product = productService.detail(id);
    return ApiRestResponse.success(product);
  }

  @ApiOperation("前台商品列表")
  @GetMapping("product/list")
  public ApiRestResponse listForCustomer(ProductListReq productListReq){
    PageInfo pageInfo = productService.ListForCustomer(productListReq);
    return ApiRestResponse.success(pageInfo);
  }

}
