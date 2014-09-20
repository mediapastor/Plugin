package com.tvd.gameview.ext.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MessageUtils {

	public static String getString(String pKey) {
		InputStream inputStream = MessageUtils.class
				.getResourceAsStream("/com/message/message.properties");
		return getString(inputStream, pKey);
	}
	
	public static String getGlobalProperty(String pKey) {
		return null;
	}
	
	public static String getString(InputStream pInputStream, String pKey) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(pInputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String lineContent = bufferedReader.readLine();
			while(lineContent != null) {
				String strs[] = lineContent.split("=");
				if(strs[0].trim().equals(pKey)) {
					return strs[1].trim();
				}
				lineContent = bufferedReader.readLine();
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			pInputStream.close();
		} 
		catch(IOException e) {
			e.printStackTrace();
		} finally {
		}
		return "Default message";
	}
	
}
