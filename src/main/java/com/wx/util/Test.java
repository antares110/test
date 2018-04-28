package com.wx.util;

import org.apache.http.entity.StringEntity;

public class Test {
  
  
  //客服
  public static void main (String args[]) {
    String token=TokenUtilForWX.getInstance().getToken();
    System.out.println(token);
    //添加客服
    String jstr="{\"kf_account\":\"bggkf@gh_13e3835c7e88\",\"nickname\":\"测试客服1\"}";
    
    StringEntity stringEntity;
    try {
      stringEntity = new StringEntity(new String(jstr.getBytes("UTF-8"),"ISO-8859-1"));
      stringEntity.setContentEncoding("UTF-8");
      stringEntity.setContentType("application/json");
      String url="https://api.weixin.qq.com/customservice/kfaccount/add?access_token=";
      String str = HttpSendUtil.sendPost(url+token, stringEntity);
      System.out.println(str);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
