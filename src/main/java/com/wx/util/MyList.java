package com.wx.util;

import java.util.LinkedList;

public class MyList<T> {
  private LinkedList<T> storage=new LinkedList<T>();
  
  public synchronized void push(T e) {//需要加上同步  
    storage.addFirst(e);  
  }  
  
  public synchronized T pop() {  
      T ret=storage.getLast();
      this.remove();
      return ret;
  }  
  
  public void remove() {  
      storage.removeLast();  
  }  
  
  public boolean empty() {  
      return storage.isEmpty();  
  }  
  
  @Override  
  public String toString() {  
      return storage.toString();  
  }  
  
  
  public static void main(String args[]) {
    MyList<String> s=new MyList<String>();
    s.push("aaa");
    s.push("bbb");
    s.push("ccc");
    
    
    while(!s.empty()) {
      String pp=s.pop();
      System.out.println(pp);
    }
  }
}
