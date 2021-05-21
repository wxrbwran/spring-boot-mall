package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 描述： CategoryService接口
 */

public interface CategoryService {

  void add(AddCategoryReq addCategoryReq);

//  void update(Category updateCategoryReq);

  void update(UpdateCategoryReq updateCategoryReq);

  void delete(Integer id);

  PageInfo listForAdmin(Integer pageNum, Integer pageSize);

  List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
