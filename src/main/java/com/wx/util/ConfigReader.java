package com.wx.util;

import java.util.Properties;

public class ConfigReader {

	private final String CONFIG_PATH="/app.properties";
	private static Properties properties=new Properties();
	private static ConfigReader config=new ConfigReader();
	public static ConfigReader GetInstance(){
		return config;
	}
	
	private ConfigReader()
	{
		try {
			properties.load(this.getClass().getResourceAsStream(CONFIG_PATH));
		} catch (Exception e) {
			System.out.println("sgis.properties is not exist");
			e.printStackTrace();
		}
		
	}
	
	public String GetConfigValue(String key)
	{
		if(properties.containsKey(key))
			return properties.getProperty(key);
		return null;
	}

}
