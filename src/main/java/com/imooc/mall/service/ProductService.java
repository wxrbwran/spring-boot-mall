package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.ProductListReq;

/**
 * 商品服务接口
 */
public interface ProductService {
  void add(AddProductReq addProductReq);

  void update(Product updateProduct);

  void delete(Integer id);

  void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

  PageInfo listForAdmin(Integer pageNum, Integer pageSize);

  PageInfo ListForCustomer(ProductListReq productListReq);

  Product detail(Integer id);
}
