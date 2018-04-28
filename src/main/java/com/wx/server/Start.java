package com.wx.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.util.ResourceUtils;

public class Start {
       private static final Logger logger = LogManager.getLogger(Start.class);
       private static volatile boolean running = true;
       private static final String WEB_DIR = "wechatproject";

	   public static void main(String[] args) throws Exception {

	        logger.info(" log init success!");

	        QueuedThreadPool threadPool = new QueuedThreadPool();
	        threadPool.setMaxThreads(500);
	        threadPool.setMinThreads(20);
	        final Server server = new Server(threadPool);
	        int acceptors = Runtime.getRuntime().availableProcessors()/2;
	        ServerConnector connector = new ServerConnector(server,acceptors, 3);
	        // Set some timeout options to make debugging easier.
	        connector.setAcceptQueueSize(500);
	        connector.setIdleTimeout(3000);
	        connector.setPort(10095);
	        server.setConnectors(new Connector[] {
	            connector
	        });

	        WebAppContext bb = new WebAppContext();
	        bb.setServer(server);
	        bb.setContextPath("/wechatpj");
	        String webPath = getWebAppPath();
	        logger.info("the webPath =="+webPath);
	        bb.setWar(webPath);
	        server.setHandler(bb);

	        Runtime.getRuntime().addShutdownHook(new Thread() {
	            @Override
	            public void run() {
	                try {
	                    server.stop();
	                    server.join();
	                    logger.info("accountweb web server stopped...");
	                } catch (Exception e) {
	                	logger.error(e);
	                    System.exit(100);
	                }
	                synchronized (Start.class) {
	                	running = false;
	                    Start.class.notify();
	                }
	            }
	        });
	        server.start();
	        logger.info("accountweb web server started, port : 10095");
	        synchronized (Start.class) {
	            while (running) {
	                try {
	                    Start.class.wait();
	                } catch (Throwable ignored) {

	                }
	            }
	        }
	    }
	   
	    private static String getWebAppPath() throws FileNotFoundException {
	        URL thisUrl = Start.class.getResource("/");
	        if (ResourceUtils.isJarURL(thisUrl)) {
	            return ResourceUtils.getFile(WEB_DIR).getAbsolutePath();
	        } else {
	            return new File(Start.class.getResource("/").getFile()).getParent() + File.separator + WEB_DIR;
	        }
	    }
}
