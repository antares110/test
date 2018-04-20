package com.wx.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class WXAccessTokenVO {
  
  @JSONField(name = "access_token")
  private String accessToken;
  @JSONField(name = "expires_in")
  private String expiresIn;
  @JSONField(name = "errcode")
  private String errcode;
  @JSONField(name = "errmsg")
  private String errmsg;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getErrcode() {
    return errcode;
  }

  public void setErrcode(String errcode) {
    this.errcode = errcode;
  }

  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }
  
  
}
