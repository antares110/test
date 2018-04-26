package com.wx.vo;

public class WXKfInfoVO {
  
  private String touser;
  
  private String msgtype;
  
  private WXKfTextVO text;

  public String getTouser() {
    return touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

  public String getMsgtype() {
    return msgtype;
  }

  public void setMsgtype(String msgtype) {
    this.msgtype = msgtype;
  }

  public WXKfTextVO getText() {
    return text;
  }

  public void setText(WXKfTextVO text) {
    this.text = text;
  }
  
  
}
