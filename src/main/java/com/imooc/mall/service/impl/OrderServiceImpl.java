package com.imooc.mall.service.impl;

/**
 * 描述： 购物车实现类
 */

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.util.OrderCodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  CartService cartService;

  @Autowired
  ProductMapper productMapper;

  @Autowired
  CartMapper cartMapper;

  @Autowired
  OrderMapper orderMapper;

  @Autowired
  OrderItemMapper orderItemMapper;

  @Override
  public String create(CreateOrderReq createOrderReq) {
    // 拿到用户id
    Integer userId = UserFilter.currentUser.getId();
    // 购物车以勾选商品
    List<CartVO> cartVOList = cartService.list(userId);
    ArrayList<CartVO> cartVOArrayListTemp = new ArrayList<>();
    for (CartVO cartVO : cartVOList) {
      if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
        cartVOArrayListTemp.add(cartVO);
      }
    }
    cartVOList = cartVOArrayListTemp;
    // 如果勾选为空，报错
    if (CollectionUtils.isEmpty(cartVOList)) {
      throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
    }
    // 判断商品是否存在，上下架状态，库存
    validSaleStatusAndStock(cartVOList);
    // 把购物车对象转为item对象
    List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
    // 扣库存
    for (OrderItem orderItem : orderItemList) {
      Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
      int stock = product.getStock() - orderItem.getQuantity();
      if (stock < 0) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
      }
      product.setStock(stock);
      productMapper.updateByPrimaryKeySelective(product);
    }
    // 把购物车中的以勾选商品删除
    cleanCart(cartVOList);
    // 生成订单
    Order order = new Order();
    // 生成订单号，有自己的规则
    String orderNum = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
    order.setOrderNo(orderNum);
    order.setUserId(userId);
    order.setTotalPrice(totalPrice(orderItemList));
    order.setReceiverName(createOrderReq.getReceiverName());
    order.setReceiverMobile(createOrderReq.getReceiverMobile());
    order.setReceiverAddress(createOrderReq.getReceiverAddress());
    order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
    order.setPostage(0);
    order.setPaymentType(1);
    // 插入到order表
    orderMapper.insertSelective(order);
    // 循环保存每个商品到order_item表
    for (OrderItem orderItem : orderItemList) {
      orderItem.setOrderNo(order.getOrderNo());
      orderItemMapper.insertSelective(orderItem);
    }
    // 把结果返回
    return orderNum;
  }

  private Integer totalPrice(List<OrderItem> orderItemList) {
    Integer totalPrice = 0;
    for (OrderItem orderItem : orderItemList) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }

  private void cleanCart(List<CartVO> cartVOList) {
    for (CartVO cartVO : cartVOList) {
      cartMapper.deleteByPrimaryKey(cartVO.getId());
    }
  }

  private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
    List<OrderItem> orderItemList = new ArrayList<>();
    for (int i = 0; i < cartVOList.size(); i++) {
      CartVO cartVO = cartVOList.get(i);
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(cartVO.getProductId());
      // 记录商品快照信息
      orderItem.setProductName(cartVO.getProductName());
      orderItem.setProductImg(cartVO.getProductImage());
      orderItem.setUnitPrice(cartVO.getPrice());
      orderItem.setQuantity(cartVO.getQuantity());
      orderItem.setTotalPrice(cartVO.getTotalPrice());
      orderItemList.add(orderItem);
    }
    return orderItemList;
  }

  private void validSaleStatusAndStock(List<CartVO> cartVOList) {
    for (CartVO cartVO : cartVOList) {
      Integer productId = cartVO.getProductId();
      Integer count = cartVO.getQuantity();
      Product product = productMapper.selectByPrimaryKey(productId);
      // 判断商品是否存在及上架
      if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
      }
      // 判断商品库存
      if (count > product.getStock()) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
      }
    }

  }
}
