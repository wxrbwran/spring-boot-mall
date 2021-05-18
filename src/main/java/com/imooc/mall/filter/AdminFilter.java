package com.imooc.mall.filter;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 管理员校验过滤器
 */
public class AdminFilter implements Filter {

  @Autowired
  UserService userService;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpSession session = request.getSession();
    User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse)
          servletResponse).getWriter();
      out.write("{\n" +
          "    \"status\": 10007,\n" +
          "    \"msg\": \"用户未登录\",\n" +
          "    \"data\": null\n" +
          "}");
      out.flush();
      out.close();
      return;
    }
    if (userService.checkAdminRole(currentUser)) {
      chain.doFilter(servletRequest, servletResponse);
    } else {
      PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse)
          servletResponse).getWriter();
      out.write("{\n" +
          "    \"status\": 10009,\n" +
          "    \"msg\": \"无管理员权限\",\n" +
          "    \"data\": null\n" +
          "}");
      out.flush();
      out.close();
    }
  }

  @Override
  public void destroy() {

  }
}
