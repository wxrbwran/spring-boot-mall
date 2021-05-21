package com.imooc.mall.common;


import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 描述： 常量
 */
@Component
public class Constant {
  public final static String SALT = "k'a4a/4s3d$%^&1_qe.a)s{d";
  public final static String IMOOC_MALL_USER = "imooc_mall_user";
  public static String FILE_UPLOAD_DIR;

  @Value("${file.upload.dir}")
  public void setFileUploadDir(String fileUploadDir) {
    FILE_UPLOAD_DIR = fileUploadDir;
  }

  public interface ProductListOrderBy{
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
//    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");

  }

}
