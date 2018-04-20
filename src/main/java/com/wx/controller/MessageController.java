package com.wx.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.wx.util.CheckoutUtil;
import com.wx.util.HttpSendUtil;
import com.wx.util.XmlTransformUtil;
import com.wx.vo.WXMessageVO;
import com.wx.vo.WXoauth2UserInfoVO;
import com.wx.vo.WXoauth2VO;

@Controller
public class MessageController {
  
  
  private static final Log logger = LogFactory.getLog(MessageController.class);
  
  private static final String TEST_PAGE="test/testPage";
  

  /**
   * 微信消息接收和token验证1
   * @param model
   * @param request
   * @param response
   * @throws IOException
   */
  @RequestMapping(value="/message",method=RequestMethod.GET)
  public void getGetMessage(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {

      logger.info("wexin token auth!");
      PrintWriter print;
      // 微信加密签名
      String signature = request.getParameter("signature");
      // 时间戳
      String timestamp = request.getParameter("timestamp");
      // 随机数
      String nonce = request.getParameter("nonce");
      // 随机字符串
      String echostr = request.getParameter("echostr");
      // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
      if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
          try {
              print = response.getWriter();
              print.write(echostr);
              print.flush();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      
  }
  
  
  
  @RequestMapping(value="/message",method=RequestMethod.POST)
  public void getPostMessage(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {

      logger.info("wexin psot message!");
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String line = null;
      String message = new String();
      while ((line = reader.readLine()) != null) {
          message += line;
      }
      System.out.println(message);
      WXMessageVO msgVO=XmlTransformUtil.xmlToObj(message, WXMessageVO.class);
      PrintWriter print;
      if (null!=msgVO && null!=msgVO.getContent()) {
          try {
              WXMessageVO retMsg= new WXMessageVO();
              retMsg.setToUserName(msgVO.getFromUserName());
              retMsg.setFromUserName(msgVO.getToUserName());
              Date d=new Date();
              retMsg.setCreateTime(d.getTime());
              retMsg.setMsgType("text");
              retMsg.setContent("echo:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8e5f5d2d8e884804&redirect_uri=http%3a%2f%2f47.106.137.161%2fwechatpj%2fauth&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
              String retStr=XmlTransformUtil.objToXml(retMsg, true);
              
              print = response.getWriter();
              print.write(retStr);
              print.flush();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

  }
  
  
  
  
  @RequestMapping(value="/auth")
  public String getAuth(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {

      logger.info("wexin auth!");
      String code = request.getParameter("code");

      if (code != null) {
          try {
              //获取网页access_toekn
              WXoauth2VO astoken=this.getPageAccessToken(code);
              logger.info("###+++"+astoken.getAccessToken());
              
              if (null!=astoken && null!=astoken.getAccessToken()) {
                WXoauth2UserInfoVO user = this.getAuthUserInfo(astoken);
                model.addAttribute("user",user);
              }

          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      return TEST_PAGE;
  }
  
  
  private WXoauth2VO getPageAccessToken(String code) throws Exception {
    WXoauth2VO asVO=null;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("appid", "wx8e5f5d2d8e884804"));
    params.add(new BasicNameValuePair("secret", "9c3bf3a4e5d26e493f7d55e45fd0ca4c"));
    params.add(new BasicNameValuePair("code", code));
    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
    String str=HttpSendUtil.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
    logger.info("page access token return:"+str);
    asVO=JSON.parseObject(str, WXoauth2VO.class);
    
    return asVO;
  }
  
  private WXoauth2UserInfoVO getAuthUserInfo(WXoauth2VO authVO) throws Exception {
    WXoauth2UserInfoVO userVO=null;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("access_token", authVO.getAccessToken()));
    params.add(new BasicNameValuePair("openid", authVO.getOpenId()));
    params.add(new BasicNameValuePair("lang", "zh_CN"));
    String str=HttpSendUtil.sendGet("https://api.weixin.qq.com/sns/userinfo", params);
    logger.info("auth user info json:"+new String(str.getBytes("ISO-8859-1"),"UTF-8"));
    userVO=JSON.parseObject(new String(str.getBytes("ISO-8859-1"),"UTF-8"), WXoauth2UserInfoVO.class);
    return userVO;
  }
  
}
