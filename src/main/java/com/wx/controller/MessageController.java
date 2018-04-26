package com.wx.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.wx.util.CheckoutUtil;
import com.wx.util.HttpSendUtil;
import com.wx.util.MyList;
import com.wx.util.TokenUtilForWX;
import com.wx.util.XmlTransformUtil;
import com.wx.vo.WXKfInfoVO;
import com.wx.vo.WXKfRespVO;
import com.wx.vo.WXKfTextVO;
import com.wx.vo.WXMessageVO;
import com.wx.vo.WXoauth2UserInfoVO;
import com.wx.vo.WXoauth2VO;

@Controller
public class MessageController {
  
  
  private static final Log logger = LogFactory.getLog(MessageController.class);
  
  private static final String TEST_PAGE="test/testPage";
  private static final String INDEX_PAGE="index.html";
  private MyList<String> mylist=new MyList<String>();
  

  /**
   * 微信消息接收和token验证12
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
              String echoStr="";
              boolean flag=false;
              switch(msgVO.getContent()) {
                case "hello":echoStr="hello!"; break;
                case "bye":echoStr="bye bye!"; break;
                default: echoStr="received your message! wait for response! "; flag=true;
              }
              
              if (flag) {
                String jstr=JSON.toJSONString(msgVO);
                mylist.push(jstr);
              }
              WXMessageVO retMsg= new WXMessageVO();
              retMsg.setToUserName(msgVO.getFromUserName());
              retMsg.setFromUserName(msgVO.getToUserName());
              Date d=new Date();
              retMsg.setCreateTime(d.getTime());
              retMsg.setMsgType("text");
              retMsg.setContent(echoStr);
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
  
  /**
   * 获取微信用户信息
   * @Title: getWechatInfo   
   * @Description: TODO(这里用一句话描述这个方法的作用)   
   * @param: @param model
   * @param: @param request
   * @param: @param response
   * @param: @throws IOException      
   * @return: void      
   * @throws
   */
  @RequestMapping(value="/wechatinfo",method=RequestMethod.GET)
  @ResponseBody
  public void getWechatInfo(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
      
      String retJson="{}";
      logger.info("get wechat info!");
      String code = request.getParameter("code");

      if (code != null) {
          try {
              //获取网页access_toekn
              WXoauth2VO astoken=this.getPageAccessToken(code);
              logger.info("###+++"+astoken.getAccessToken());
              
              if (null!=astoken && null!=astoken.getAccessToken()) {
                WXoauth2UserInfoVO user = this.getAuthUserInfo(astoken);
                retJson=JSON.toJSONString(user);
              }
              
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      
      response.getWriter().write(retJson);
      response.getWriter().flush();
  }
  
  
  /**
   * 获取消息列表
   * @Title: getWxkfInfo   
   * @Description: TODO(这里用一句话描述这个方法的作用)   
   * @param: @param request
   * @param: @param response
   * @param: @throws Exception      
   * @return: void      
   * @throws
   */
  @RequestMapping(value="/wxkfinfo",method=RequestMethod.GET)
  @ResponseBody
  public void getWxkfInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
    List<String> retList=new ArrayList<String>();
    while(!mylist.empty()) {
      retList.add(mylist.pop());
    }
    String retJson=JSON.toJSONString(retList);
    response.getWriter().write(retJson);
    response.getWriter().flush();
    
  }
  
  @RequestMapping(value="/wxkfinfo",method=RequestMethod.POST)
  @ResponseBody
  public void respWxkfInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
    
    
    BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
    String line = null;
    String message = new String();
    while ((line = reader.readLine()) != null) {
        message += line;
    }
    Map reqMap=(Map) JSON.parse(message);
    String retmsg=(String) reqMap.get("retMsg");
    String touser=(String) reqMap.get("toUser");
    //拼接客服消息
    WXKfInfoVO kfinfo=new WXKfInfoVO();
    WXKfTextVO kftext=new WXKfTextVO();
    kftext.setContent(retmsg);
    kfinfo.setText(kftext);
    kfinfo.setTouser(touser);
    kfinfo.setMsgtype("text");
    String reqJson=JSON.toJSONString(kfinfo);
    //发送消息
    String str=this.sendWXKfInfo(reqJson);
    response.getWriter().write(str);
    response.getWriter().flush();
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
  
  private String sendWXKfInfo(String reqJson) {
    String respStr="";
    try {
      StringEntity stringEntity = new StringEntity(new String(reqJson.getBytes("UTF-8"),"ISO-8859-1"));
      stringEntity.setContentEncoding("UTF-8");
      stringEntity.setContentType("application/json");
      String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
      String str = HttpSendUtil.sendPost(url+TokenUtilForWX.getInstance().getToken(), stringEntity);
      logger.info("sendWXKfInfo resp json:"+new String(str.getBytes("ISO-8859-1"),"UTF-8"));
      respStr=new String(str.getBytes("ISO-8859-1"),"UTF-8");
    }catch(Exception e) {
      e.printStackTrace();
    }
    
    return respStr;
  }
  
}
