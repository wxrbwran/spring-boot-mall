package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 *
 */
@RestController
public class ProductAdminController {
  @Autowired
  ProductService productService;

  @PostMapping("admin/product/add")
  public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
    productService.add(addProductReq);
    return ApiRestResponse.success();
  }

  @PostMapping("admin/upload/file")
  public ApiRestResponse upload(HttpServletRequest httpServletRequest,
    @RequestParam("file") MultipartFile file) {
    String fileName = file.getOriginalFilename();
    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    UUID uuid = UUID.randomUUID();
    String newFileName = uuid.toString() + suffixName;
    // 创建文件
    File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
    File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
    if (!fileDirectory.exists()) {
      if (!fileDirectory.mkdir()) {
        throw new ImoocMallException(ImoocMallExceptionEnum.MAKE_DIR_FAILED);
      }
    }
    try {
      file.transferTo(destFile);
    } catch (IOException e) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAILED);
    }
    try {
      return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
    } catch (URISyntaxException e) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAILED);
    }
  }

  public URI getHost(URI uri) {
    URI effectiveURI;
    try {
      effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
    } catch (URISyntaxException e) {
      effectiveURI = null;
    }
    return effectiveURI;
  }

  @ApiOperation("后台更新商品")
  @PostMapping("admin/product/update")
  public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
    Product product = new Product();
    BeanUtils.copyProperties(updateProductReq, product);
    productService.update(product);
    return ApiRestResponse.success();
  }

  @ApiOperation("后台删除商品")
  @PostMapping("admin/product/delete")
  public ApiRestResponse deleteProduct(@RequestParam Integer id) {
    productService.delete(id);
    return ApiRestResponse.success();
  }

  @ApiOperation("后台批量上下架商品")
  @PostMapping("admin/product/batchUpdateSellStatus")
  public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
    productService.batchUpdateSellStatus(ids, sellStatus);
    return ApiRestResponse.success();
  }

  @ApiOperation("后台商品列表")
  @GetMapping("admin/product/list")
  public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
  }
}
