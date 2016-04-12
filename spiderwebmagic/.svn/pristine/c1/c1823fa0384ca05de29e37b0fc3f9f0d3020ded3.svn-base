package com.ecmoho.base.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.ecmoho.base.bean.HeaderBean;

public class UrlUtil {
	   
	   //根据url链接获取数据
	   public static String getUrlString(HeaderBean hb,String urlStr) {
			URL url = null;
			HttpURLConnection http = null;
			String result = "";
			try {
				Random random = new Random();
				// 随机思考时间
				try {
					System.out.println("休息："+ random.nextInt(3) * 1000);
					Thread.sleep(random.nextInt(3) * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				url = new URL(urlStr);
				http = (HttpURLConnection) url.openConnection();
				http.setUseCaches(false);
				http.setConnectTimeout(50000);// 设置连接超时
				http.setReadTimeout(50000);// 设置读取超时
				http.setRequestMethod("GET");
				http.setDoOutput(true);
				http.setDoInput(true);
				if(!"".equals(hb.getUserAgent())&&hb.getUserAgent()!=null){
					http.setRequestProperty("user-agent", hb.getUserAgent());
				}
				if(!"".equals(hb.getAccept())&&hb.getAccept()!=null){
					 http.setRequestProperty("accept", hb.getAccept());
				}
				if(!"".equals(hb.getAcceptLanguage())&&hb.getAcceptLanguage()!=null){
					http.setRequestProperty("accept-language", hb.getAcceptLanguage());
				}
				if(!"".equals(hb.getOrgin())&&hb.getOrgin()!=null){
					http.setRequestProperty("origin", hb.getOrgin());
				}
				if(!"".equals(hb.getReferer())&&hb.getReferer()!=null){
					http.setRequestProperty("referer", hb.getReferer());
				}
				if(!"".equals(hb.getCookie())&&hb.getCookie()!=null){
					http.setRequestProperty("cookie", hb.getCookie());
				}
			    
			    
			    
				// http.setDoOutput(true);
				http.connect();
				if (http.getResponseCode() == 200) {
					// String set_cookie =
					// http.getFirstHeader("Set-Cookie").getValue();
					BufferedReader in = new BufferedReader(new InputStreamReader(
							http.getInputStream(), "utf-8"));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						result += inputLine;
					}
					in.close();
				} else {
					// 如果登陆不成功则用手工录入cookie的形式
					System.out.println("登录不成功转用手工录入cookie的形式");
				}
			} catch (Exception e) {
				System.out.println("err");
			} finally {
				if (http != null)
					http.disconnect();
			}
			return result;
		}
		
}
