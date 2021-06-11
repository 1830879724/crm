package com.xxxx.crm.utils;


import com.github.pagehelper.PageException;

public class AssertUtil {


  public  static void isTrue(Boolean flag,String msg){
    if(flag){
      throw  new PageException(msg);
    }
  }

}
