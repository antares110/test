package com.wx.util;

import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.wx.vo.WXMessageVO;

/**
 * xml转换方法
 * @ClassName:  XmlTransformUtil   
 * @Description:xml与obj相互转换  
 * @author: cuiyang-ghq 
 * @date:   2018年1月30日 上午11:46:52   
 *
 */
public class XmlTransformUtil {
	
    private static String PREFIX_CDATA = "<![CDATA[";  
    private static String SUFFIX_CDATA = "]]>"; 
	
    public static XStream initXStream(boolean isAddCDATA) {  
        XStream xstream = null;  
        if (isAddCDATA) {  
            xstream = new XStream(new DomDriver("UTF-8",new XmlFriendlyReplacer("_-", "_")) {
                @Override  
                public HierarchicalStreamWriter createWriter(Writer out) {  
                    return new PrettyPrintWriter(out,new XmlFriendlyReplacer("_-", "_")) { 
                        protected void writeText(QuickWriter writer, String text) {  
                        	writer.write(PREFIX_CDATA + text + SUFFIX_CDATA); 
                        }  
                    };  
                }  
            });  
        } else {  
            xstream = new XStream(new DomDriver("UTF-8",new XmlFriendlyReplacer("_-", "_")));  
        }  
        return xstream;  
    } 
	
	/**
	 * 对象转xml
	 * @Title: objToXml   
	 * @Description: 将对象转为xml 
	 * @param: @param obj
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String objToXml(Object obj,boolean iscdata) {
		String rStr="";
		XStream xStream = initXStream(iscdata);
		xStream.processAnnotations(obj.getClass()); // 识别obj类中的注解
//        StringWriter sw = new StringWriter();
//        xStream.marshal(obj, new CompactWriter(sw));

		rStr= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xStream.toXML(obj);
        Pattern p = Pattern.compile("\t|\r|\n");  
        Matcher m = p.matcher(rStr);  
        String dest = m.replaceAll(""); 
		return rStr;
	}

	/**
	 * 将xml转为对象
	 * @Title: xmlToObj   
	 * @Description: 将xml转为对象   
	 * @param: @param xmlStr
	 * @param: @param cls
	 * @param: @return      
	 * @return: T      
	 * @throws
	 */
	public static <T> T xmlToObj(String xmlStr, Class<T> cls) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(cls);
		T t = (T) xstream.fromXML(xmlStr);
		return t;
	}
	
//	public static void main (String args[]) {
//	  String xmlStr="<xml><ToUserName><![CDATA[gh_13e3835c7e88]]></ToUserName><FromUserName><![CDATA[oun5n1Gq94XJBrljmUfZdxIleT-M]]></FromUserName><CreateTime>1524134936</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[look]]></Content><MsgId>6546109705260266450</MsgId></xml>";
//	  WXMessageVO aa=XmlTransformUtil.xmlToObj(xmlStr, WXMessageVO.class);
//	  System.out.println(aa.getContent());
//	}
}
