package com.hnblc.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

import com.hnblc.utils.DBsql;

public class LogisticsInfo {

	/**
	 * @param args
	 */
	public static  Map<String, String> account;
	
	public static void doLogisticsInfoGet(String orderNumber,String expressNumber) {
		// TODO Auto-generated method stub
		
		
			URL url = null;  
			HttpURLConnection http = null;  
			try {  
			    url = new URL("http://openapi.ecmoho.com/Yto/track?no="+expressNumber+"&format=json");  
			    http = (HttpURLConnection) url.openConnection();  
			    http.setDoInput(true);  
			    http.setDoOutput(true);  
			    http.setUseCaches(false);  
			    http.setConnectTimeout(50000);//设置连接超时  
			    http.setReadTimeout(50000);//设置读取超时  
			    http.setRequestMethod("POST");  
			    http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");  
			    http.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
			    http.setRequestProperty("Connection","keep-alive");
			     http.setRequestProperty("User-Agent", 
		          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
			    //http.setRequestProperty("Cookie",reffer_cookie);
			    http.setRequestProperty("X-Requested-With:","XMLHttpRequest");
			    http.setDoOutput(true);
			    http.connect();  
			    String param = "no="+expressNumber+"&format=json";   
			    
			    System.out.print(param);
			    
			    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
			    osw.write(param);  
			    osw.flush();  
			    osw.close(); 
			    
			    String result="";
			    System.out.println(http.getResponseCode());  
			    //登录接口警报节点
			    if (http.getResponseCode() == 200) { 
			    	// String set_cookie = http.getFirstHeader("Set-Cookie").getValue(); 
			        BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));  
			        String inputLine;  
			        while ((inputLine = in.readLine()) != null) {  
			            result += inputLine;  
			        }  
			        
			        System.out.println(result);  
			        
					 //判断是否抓取成功
					int isSuccess= result.indexOf("false"); 
					
			        in.close();  	
			        
			        System.out.println(isSuccess); 
			        //更新物流推送状态
			        if(isSuccess<0)
			        {
			        	 Map<String, String> update= new HashMap<String,String>();
						 update.put("LogisticsInfo","1");
						 try {
							DBsql.updateOrder(update,orderNumber);
						 } catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						 }
						 
			        }
			    }
			    
			    //sleep
			} catch (Exception e) {  
			    System.out.println("err");  
			} finally {  
			    if (http != null) http.disconnect();   
			}  
		}
	
}
