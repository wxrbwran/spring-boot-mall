package com.imooc.mall.service;

import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;

/**
 * 描述： CategoryService接口
 */

public interface CategoryService {

  void add(AddCategoryReq addCategoryReq);

//  void update(Category updateCategoryReq);

  void update(UpdateCategoryReq updateCategoryReq);

  void delete(Integer id);
}
