package com.xxxx.crm.utils;


import com.github.pagehelper.PageException;
import com.xxxx.crm.exceptions.ParamsException;

public class AssertUtil {


  public  static void isTrue(Boolean flag,String msg){
    if(flag){
      throw  new ParamsException(msg);//这里的问题、、、
    }
  }

}
