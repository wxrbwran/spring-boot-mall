package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.model.vo.CategoryVO;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 描述： CategoryService实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  CategoryMapper categoryMapper;

  @Override
  public void add(AddCategoryReq addCategoryReq) {
    Category category = new Category();
    BeanUtils.copyProperties(addCategoryReq, category);
    Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
    if (categoryOld != null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
    }
    int count = categoryMapper.insertSelective(category);
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
    }
  }

  @Override
  public void update(UpdateCategoryReq updateCategoryReq) {
    String categoryName = updateCategoryReq.getName();
    if (categoryName != null) {
      Category categoryOld =  categoryMapper.selectByName(categoryName);
      if (categoryOld != null && !categoryOld.getId().equals(updateCategoryReq.getId())) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
      }
    }
    Category category = new Category();
    BeanUtils.copyProperties(updateCategoryReq, category);
    int count = categoryMapper.updateByPrimaryKeySelective(category);
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
    }
  }

  @Override
  public void delete(Integer id) {
    Category categoryOld =  categoryMapper.selectByPrimaryKey(id);
    if (categoryOld == null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
    int count = categoryMapper.deleteByPrimaryKey(id);
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
  }

  @Override
  public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize, "type, order_num");
    List<Category> categoryList = categoryMapper.selectList();
    PageInfo pageInfo = new PageInfo<>(categoryList);
    return pageInfo;
  }

  @Override
  @Cacheable("listCategoryForCustomer")
  public List<CategoryVO> listCategoryForCustomer(Integer parentId) {
    ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
    recursivelyFindCategories(categoryVOList, parentId);
    return  categoryVOList;
  }

  private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
    List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
    if (!CollectionUtils.isEmpty(categoryList)) {
      for (Category category : categoryList) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        categoryVOList.add(categoryVO);
        recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
      }
    }
  }
}
