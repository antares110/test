package com.wx.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.wx.vo.WXAccessTokenVO;
import com.wx.vo.WXoauth2VO;

/**
 * 获取token
 * @ClassName:  tokenUtilForWX   
 * @Description:   
 * @author: cuiyang-ghq 
 * @date:   2018年1月30日 上午10:13:49   
 *
 */
public class TokenUtilForWX {
	private static volatile TokenUtilForWX tokenUtilForWX = null;
	private String token = null;
	private static String funcid = ConfigReader.GetInstance().GetConfigValue("wechat.func.getacctoken");
	private static String url = ConfigReader.GetInstance().GetConfigValue("wechat.server.url");
	private static String appid = ConfigReader.GetInstance().GetConfigValue("wechat.appID");
	private static String appsecret = ConfigReader.GetInstance().GetConfigValue("wechat.appsecret");

	private static final Log logger = LogFactory.getLog(TokenUtilForWX.class);

	private TokenUtilForWX() {
		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("grant_type", "client_credential"));
		params.add(new BasicNameValuePair("appid", appid));
		params.add(new BasicNameValuePair("secret", appsecret));
		try {
			String tokenStr = HttpSendUtil.sendGet(url+funcid, params);
			token = this.getTokenStr(tokenStr);
		} catch (Exception e1) {
			logger.error("TokenUtilForWX Exception:"+e1.getMessage(),e1);
		}
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			public void run() {
				try {
					String tokenStr = HttpSendUtil.sendGet(url+funcid, params);
					token = getTokenStr(tokenStr);
				} catch (Exception e) {
					logger.error("tokenUtilForWX timer Exception:"+e.getMessage(),e);
				} 
			}
		};
		timer.schedule(timerTask, 100 * 60 * 1000L, 100 * 60 * 1000L);
	}

	public static TokenUtilForWX getInstance() {
		if (null == tokenUtilForWX) {
			synchronized (TokenUtilForWX.class) {
				if (null == tokenUtilForWX) {
					tokenUtilForWX = new TokenUtilForWX();
				}
			}
		}

		return tokenUtilForWX;
	}

	/**
	 * 获取token 
	 * @Title: getToken 
	 * @Description: 获取token 
	 * @param: 
	 * @return:String
	 * @throws
	 */
	private String getTokenStr(String tokenJson) {
	    WXAccessTokenVO tokenObj = null;
		String token = "";
		if (null != tokenJson) {
			logger.info("WXtokenJson:"+tokenJson);
			tokenObj = JSON.parseObject(tokenJson, WXAccessTokenVO.class);
			if (null!=tokenObj && null!=tokenObj.getAccessToken() && !"".equals(tokenObj.getAccessToken())) {
			  token = tokenObj.getAccessToken();
			}else {
			  logger.info("can not get wechat accesstoken :"+tokenObj.getErrcode()+","+tokenObj.getErrmsg());
			}
		}
		return token;
	}
	
	/**
	 * 获取token
	 * @Title: getToken   
	 * @Description: 最多尝试3次获取token
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
  public String getToken() {
    if (null != token && !"".equals(token)) {
      if(null==token || "".equals(token)) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
      }
      return token;
    }
    synchronized (this) {
      if (null != token && !"".equals(token)) {
        return token;
      }
      Boolean flag = false;
      int c = 0;
      while (!flag && c < 3) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "client_credential"));
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("secret", appsecret));
        try {
          String tokenStr = HttpSendUtil.sendGet(url+funcid, params);
          token = this.getTokenStr(tokenStr);
          if (null != token && !"".equals(token)) {
            flag = true;
          } else {
            c = c + 1;
          }
        } catch (Exception e1) {
          logger.error("tokenUtilForWX Exception:" + e1.getMessage(), e1);
        }
      }
    }

    return token;
  }
	
	/**
	 * 重新获取token
	 * @Title: regetToken   
	 * @Description: 重新获取token
	 * @param: @return 
	 * @return: String
	 * @throws
	 */
	public synchronized String regetToken() {
      //重新获取
	  token="";
	  return this.getToken();
	}
	
	
	public static void main (String args[]) {
	  String token=TokenUtilForWX.getInstance().getToken();
	  System.out.println(token);
//	  String jstr="{\"button\":[{\"type\":\"view\",\"name\":\"测试菜单\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8e5f5d2d8e884804&redirect_uri=http%3a%2f%2f47.106.137.161%2fwechatpj%2fauth&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"}]}";
	  String jstr="{\"touser\":\"oun5n1Gq94XJBrljmUfZdxIleT-M\",\"template_id\":\"SDRICMxGpiqt74xOpn3p369eGaPYLoLtzO1RjMDF1-o\",\"data\":{\"test1\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"test2\":{\"value\":\"巧克力\",\"color\":\"#173177\"}}}";
	  StringEntity stringEntity;
      try {
        stringEntity = new StringEntity(new String(jstr.getBytes("UTF-8"),"ISO-8859-1"));
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
//        String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
        String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        String str = HttpSendUtil.sendPost(url+token, stringEntity);
        System.out.println(str);
        
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("access_token", token));
//        String getm = HttpSendUtil.sendGet("https://api.weixin.qq.com/cgi-bin/menu/get", params);
//        System.out.println(new String(getm.getBytes("ISO-8859-1"),"UTF-8"));
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	  
//	  String j="{\"access_token\":\"8_WIBv5-qqq4097kksd4xKjSM_esPDgw0dWM7dMXTv4hsLJVU75FHaY3Kh7Xmk79_YO_u1GT4kkTgx_JPm0D_hJu4LyFuFK4E6zvxb3V_jXsY\",\"expires_in\":7200,\"refresh_token\":\"8_SFYb0hltpidGvNkyAcp3z57lpsdTozBzzkQdd4VX4mnySQNpxo4q140r3QfceuhBXF3dIoDTBnB07LIE-HBssP4ieg0YFKYM8YBwK1Vxmok\",\"openid\":\"oun5n1Gq94XJBrljmUfZdxIleT-M\",\"scope\":\"snsapi_userinfo\"}";
//	  
//	  WXoauth2VO v=JSON.parseObject(j, WXoauth2VO.class);
//	  System.out.println(v.getAccessToken());
	}

}
