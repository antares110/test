package com.wx.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



/**
 * 发送http请求
 * 
 * @ClassName: HttpSendUtil
 * @Description: 发送post以及get请求
 * @author: cuiyang-ghq
 * @date: 2018年1月30日 上午10:15:21
 *
 */
public class HttpSendUtil {
	
	private static final Log logger = LogFactory.getLog(HttpSendUtil.class);

	public static String sendGet(String url, List<NameValuePair> params) throws Exception {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间
				.setConnectionRequestTimeout(5000) // 设置请求超时时间
				.setSocketTimeout(5000).setRedirectsEnabled(true)// 默认允许自动重定向
				.build();
		String httpUrl = url;
		String paramStr = "";
		paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
		HttpGet httpGet = new HttpGet(httpUrl + "?" + paramStr);
		httpGet.setConfig(requestConfig);
		String srtResult = "";
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				srtResult = EntityUtils.toString(httpResponse.getEntity());// 获得返回的结果
			} else {
				logger.info("httpget status :" + httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new Exception("HttpSendUtil.sendGet  exception:"+e.getMessage(),e);
		} finally {
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("HttpSendUtil.sendGet close httpClient exception:"+e.getMessage(),e);
			}
		}

		return srtResult;
	}

	public static String sendPost(String url, List<NameValuePair> params) throws Exception {
		String strResult = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 配置超时时间
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(5000).setRedirectsEnabled(true).build();

		HttpPost httpPost = new HttpPost(url);
		// 设置超时时间
		httpPost.setConfig(requestConfig);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			// 设置post求情参数
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse != null) {

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					strResult = EntityUtils.toString(httpResponse.getEntity());
				} else {
					logger.info("httppost status:" + httpResponse.getStatusLine().getStatusCode());
				}
			}
		} catch (Exception e) {
			throw new Exception("HttpSendUtil.sendPost  exception:"+e.getMessage(),e);
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close(); // 释放资源
				}
			} catch (IOException e) {
				logger.error("HttpSendUtil.sendPost close httpClient exception:"+e.getMessage(),e);
			}
		}

		return strResult;
	}
	
	public static String sendPost(String url, StringEntity stringEntity) throws Exception {
		String strResult = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 配置超时时间
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(5000).setRedirectsEnabled(true).build();

		HttpPost httpPost = new HttpPost(url);
		// 设置超时时间
		httpPost.setConfig(requestConfig);
		try {
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			// 设置post求情参数
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse != null) {

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					strResult = EntityUtils.toString(httpResponse.getEntity());
				} else {
					logger.info("httppost status:" + httpResponse.getStatusLine().getStatusCode());
				}
			}
		} catch (Exception e) {
			throw new Exception("HttpSendUtil.sendPost  exception:"+e.getMessage(),e);
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close(); // 释放资源
				}
			} catch (IOException e) {
				logger.error("HttpSendUtil.sendPost close httpClient exception:"+e.getMessage(),e);
			}
		}

		return strResult;
	}

//	 public static void main(String args[]) {
//	
////	 String xmlStr="<?xml version=\"1.0\" encoding=\"utf-8\"?><requests><global><token>92497FD73BF446C3359444AD539E0E29072B11A1B71D5E80</token></global><request><funcId>hex_policy_upload_uploadRealTimePolicyDataFunction</funcId><policy_no><![CDATA[0070006512018000004]]></policy_no><policy_type><![CDATA[1]]></policy_type><policy_state><![CDATA[1]]></policy_state><policy_state_desc><![CDATA[正常]]></policy_state_desc><policy_start_date><![CDATA[2018-02-02]]></policy_start_date><policy_end_date><![CDATA[2019-02-01]]></policy_end_date><policy_total_amount><![CDATA[24000.00]]></policy_total_amount><insure_address><![CDATA[]]></insure_address><insure_way><![CDATA[2]]></insure_way><applicant_name><![CDATA[北京行驶证车辆]]></applicant_name><applicant_credential_code><![CDATA[1]]></applicant_credential_code><applicant_credential_no><![CDATA[430104196704124342]]></applicant_credential_no><insurant_datas><![CDATA[[{\"insurant_amount\":24000,\"insurant_credential_code\":\"01\",\"insurant_credential_no\":\"430104196704124342\",\"insurant_name\":\"北京行驶证车辆\"}]]]></insurant_datas><beneficiary_datas><![CDATA[[{\"beneficiary_credential_code\":1,\"beneficiary_credential_no\":\"21010319810525151X\",\"beneficiary_name\":\"测试一\"},{\"beneficiary_credential_code\":1,\"beneficiary_credential_no\":\"430104196704124342\",\"beneficiary_name\":\"测试二\"}]]]></beneficiary_datas><insurance_type_datas><![CDATA[[{\"insurance_type_code\":\"060006\",\"insurance_type_name\":\"附加交通意外伤害保险\"},{\"insurance_type_code\":\"060116\",\"insurance_type_name\":\"附加疾病身故保险\"},{\"insurance_type_code\":\"065101\",\"insurance_type_name\":\"人身意外伤害综合保险\"}]]]></insurance_type_datas></request></requests>";
//	String xmlStr="<?xml version=\"1.0\" encoding=\"UTF-8\"?><requests><global><token><![CDATA[92497FD73BF446C3359444AD539E0E2939DA1FA9A0D38D8D]]></token></global><request><funcId>hex_policy_upload_uploadRealTimePolicyDataFunction</funcId><policy_no><![CDATA[LBBQ1801031520100677]]></policy_no><policy_type><![CDATA[1]]></policy_type><policy_state><![CDATA[1]]></policy_state><policy_state_desc><![CDATA[正常]]></policy_state_desc><policy_start_date><![CDATA[2018-01-04]]></policy_start_date><policy_end_date><![CDATA[2018-01-10]]></policy_end_date><policy_total_amount><![CDATA[2000000.00]]></policy_total_amount><insure_address><![CDATA[]]></insure_address><insure_way><![CDATA[1]]></insure_way><applicant_name><![CDATA[勃朗宁]]></applicant_name><applicant_credential_code><![CDATA[1]]></applicant_credential_code><applicant_credential_no><![CDATA[37120219950301552X]]></applicant_credential_no><insurant_datas><![CDATA[[{\"insurant_amount\":2000000,\"insurant_credential_code\":\"01\",\"insurant_credential_no\":\"37120219950301552X\",\"insurant_name\":\"勃朗宁\"}]]]></insurant_datas><beneficiary_datas><![CDATA[[{\"beneficiary_credential_code\":1,\"beneficiary_credential_no\":\"21010319810525151X\",\"beneficiary_name\":\"测试一\"},{\"beneficiary_credential_code\":1,\"beneficiary_credential_no\":\"430104196704124342\",\"beneficiary_name\":\"测试二\"}]]]></beneficiary_datas><insurance_type_datas><![CDATA[[{\"insurance_type_code\":\"CTAA01\",\"insurance_type_name\":\"阳光人寿和顺交通工具意外伤害保险\"}]]]></insurance_type_datas></request></requests>";
//	 EnDeUtil ende = new EnDeUtil("insurance");
//     String xmlStrEn= ende.encrypt(xmlStr);
//	 StringEntity stringEntity = null;
//      try {
//        stringEntity = new StringEntity(xmlStrEn);
//      } catch (UnsupportedEncodingException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//     stringEntity.setContentEncoding("UTF-8");
//     stringEntity.setContentType("application/xml");
//     String retJsonStr = HttpSendUtil.sendPost("http://redalert.sia1995.net/insurance/common/outdata.do", stringEntity);
//	 System.out.println(retJsonStr);
//	 }
}
