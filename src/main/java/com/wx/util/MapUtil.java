package com.wx.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.wx.vo.WXMessageVO;

public class MapUtil {
  private static MapUtil maputil=null;
  private Map<String,MyList<String>> mymap=new HashMap<String,MyList<String>>();
  
  private MapUtil() {
    
  }
  
  public static synchronized MapUtil getInstance() {
    if(null==maputil) {
      synchronized(MapUtil.class) {
        if(null==maputil) {
          maputil=new MapUtil();
        }
      }
    }
    return maputil;
  }
  
  public synchronized void putVal(WXMessageVO msgVO) {
    String fromUser=msgVO.getFromUserName();
    MyList<String> mylist=mymap.get(fromUser);
    String jstr=JSON.toJSONString(msgVO);
    if (null==mylist) {
      mylist=new MyList<String>();
      mylist.push(jstr);
    }else {
      mylist.push(jstr);
    }
    mymap.put(fromUser, mylist);
  }
  
  public synchronized Map<String,MyList<String>> popVal(){
    Map<String,MyList<String>> retVal=new HashMap<String,MyList<String>>();
    retVal.putAll(mymap);
    mymap.clear();
    return retVal;
  } 
}
