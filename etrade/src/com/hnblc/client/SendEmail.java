package com.hnblc.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendEmail {

	/**
	 * @param args
	 */
	public static void DoSendEmail() {
		// TODO Auto-generated method stub
			URL url = null;  
			HttpURLConnection http = null;  
			try {  
			    url = new URL("http://121.41.95.196:88/Etrade/Db/warnMessage");  
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
			    String param = "";   
			    
			    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
			    osw.write(param);  
			    osw.flush();  
			    osw.close(); 
			    
			    String result="";
			    //登录接口警报节点
			    if (http.getResponseCode() == 200) { 
			    	// String set_cookie = http.getFirstHeader("Set-Cookie").getValue(); 
			        BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));  
			        String inputLine;  
			        while ((inputLine = in.readLine()) != null) {  
			            result += inputLine;  
			        }  
			        System.out.println(result);  
			        in.close();  	
			        
			    }else{
			    	 System.out.println("err");  
			    }
			    
			    //sleep
			} catch (Exception e) {  
			    System.out.println("err");  
			} finally {  
			    if (http != null) http.disconnect();   
			}  
		}
	
}
