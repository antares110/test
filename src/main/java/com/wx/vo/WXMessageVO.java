package com.wx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WXMessageVO {
  @XStreamAlias("ToUserName")
  private String toUserName;
  @XStreamAlias("FromUserName")
  private String fromUserName;
  @XStreamAlias("CreateTime")
  private Long createTime;
  @XStreamAlias("MsgType")
  private String msgType;
  @XStreamAlias("Content")
  private String content;
  @XStreamAlias("MsgId")
  private String msgId;
  @XStreamAlias("Event")
  private String event;
  @XStreamAlias("EventKey")
  private String eventKey;
  @XStreamAlias("MenuId")
  private String menuId;

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getEventKey() {
    return eventKey;
  }

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }
  
  
}
