package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 目录Controller
 */
@Controller
public class CategoryController {
  @Autowired
  UserService userService;

  @Autowired
  CategoryService categoryService;

  /**
   * @desc 后台添加目录
   * @param addCategoryReq
   * @return ApiRestResponse
   */
  @ApiOperation("后台添加目录")
  @PostMapping("admin/category/add")
  @ResponseBody
  public ApiRestResponse addCategory(HttpSession session,
    @Valid @RequestBody AddCategoryReq addCategoryReq) {
    User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    if (userService.checkAdminRole(currentUser)) {
      categoryService.add(addCategoryReq);
      return ApiRestResponse.success();
    } else {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
//    return ApiRestResponse.success();
  }

  @ApiOperation("后台更新目录")
  @PostMapping("admin/category/update")
  @ResponseBody
  public ApiRestResponse updateCategory(HttpSession session,
     @Valid @RequestBody UpdateCategoryReq updateCategoryReq) {

    User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    if (userService.checkAdminRole(currentUser)) {
      categoryService.update(updateCategoryReq);
      return ApiRestResponse.success();
    } else {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
  }

  @ApiOperation("删除目录")
  @PostMapping("admin/category/delete")
  @ResponseBody
  public ApiRestResponse deleteCategory(@RequestBody Map<String, Integer> map) {
    int id = map.get("id");
    categoryService.delete(id);
    return ApiRestResponse.success();
  }

  @ApiOperation("后台目录列表")
  @GetMapping("admin/category/list")
  @ResponseBody
  public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
    System.out.println(pageInfo);
    return ApiRestResponse.success(pageInfo);
  }

  @ApiOperation("前台目录列表")
  @GetMapping("category/list")
  @ResponseBody
  public ApiRestResponse listCategoryForCustomer() {
    List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(0);
    return ApiRestResponse.success(categoryVOList);
  }
}
