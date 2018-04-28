package com.wx.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;


public class StartTest extends AbstractHandler {
  
  private String name;
  
  public StartTest(String a) {
    this.name=a;
  }
  
  public StartTest() {
    this.name="World";
  }

  @Override
  public void handle(String arg0, Request arg1, HttpServletRequest arg2, HttpServletResponse arg3)
      throws IOException, ServletException {
    arg3.setContentType("text/html; charset=utf-8");
    arg3.setStatus(HttpServletResponse.SC_OK);
    arg3.getWriter().println("<h1>Hello "+this.name+"</h1>");
    arg1.setHandled(true);
  }

//  public static void main(String args[]) throws Exception {
//    
//    //创建服务
//    Server server=new Server();
//    //创建连接器
//    ServerConnector http=new ServerConnector(server);
//    http.setHost("localhost");
//    http.setPort(8080);
//    http.setIdleTimeout(30000);
//    server.addConnector(http);
//    //创建handler
//    ContextHandler context1=new ContextHandler();
//    context1.setContextPath("/hello1");
//    context1.setHandler(new StartTest());
//    
//    ContextHandler context2=new ContextHandler();
//    context2.setContextPath("/hello2");
//    context2.setHandler(new StartTest("xiaoming"));
//    
//    ContextHandlerCollection contexts = new ContextHandlerCollection();
//    contexts.setHandlers(new Handler[] { context1, context2 });
//    
//    server.setHandler(contexts);
//    server.start();
//    server.join();
//  }
  
  
  public static void main(String args[]) throws Exception {
    Server server = new Server(8080);

    // WebAppContext对象是用来控制它自己赖以生存的环境的。
    // 在这个栗子里面，context path设置为“/”
    // 这样它就可以处理根context下的请求了
    // 我们还可以看到它设置了war的位置
    // 整个host的其他配置都是可用的可配置的
    // 你可用配置注解扫描的支持（需要通过PlusConfiguration）
    // 还可用选择webapp在哪里自动解压
    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath("/");
    File warFile = new File(
            "../../tests/test-jmx/jmx-webapp/target/jmx-webapp");
    webapp.setWar(warFile.getAbsolutePath());
    // 一个WebAppContext是一个ContextHandler
    // 所以它也需要被配置到server里面这样server才会知道请求将被送往哪里
    server.setHandler(webapp);

    // Start things up!
    server.start();

    server.dumpStdErr();

    server.join();
  }
}
