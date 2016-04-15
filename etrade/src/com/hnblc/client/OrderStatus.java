package com.hnblc.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.hnblc.utils.DBsql;
import com.hnblc.utils.XmlLForStatus;

public class OrderStatus {

	/**
	 * @param args
	 */
	public static void getOrderStatus(String where,String nowtime,String nowtime2) {
		// TODO Auto-generated method stub
		Date nowTime=new Date(); 
		 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
		 String requestTime = time.format(nowTime);
			URL url = null;  
			HttpURLConnection http = null;  
			StringBuilder xml = new StringBuilder();
	        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        xml.append("<root>");
	        xml.append("<pub>");
	        xml.append("<requestTime>"+requestTime+"</requestTime>");
	        xml.append("<cbeCode/>");
	        xml.append("<type>1</type>");
	        xml.append("</pub>");
	        xml.append("<orders>");
	        //增加单个订单的查询
	        
	        //多个订单推送的循环体
	        List<Map<String, String>> orders =DBsql.getOrderList("where "+where);
	       
	        //更新此批次的状态抓取时间 EtradeStatusTime
//	        try {
//				DBsql.updateEtradeStatusTime(where,nowtime2);
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
	        for(int i = 0; i < orders.size(); i++)  
	        { 
	        	Map<String, String> order = orders.get(i); 
			        xml.append("<order>");
			        xml.append("<orderNo>"+order.get("orderNumber")+"</orderNo>");
			        xml.append("<waybillNo/>");
			        xml.append("</order>");
	        }     
	        xml.append("</orders>");
	        xml.append("</root>");
	        
			try {  
			    url = new URL("http://218.28.185.212:8080/BDIService/ws/retrieveInfo/orderStatus");  
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
			    String param = "xml="+xml;   
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
			        
			          FileWriter fwriter = null;
					   
					   new File("D:/www/api/etrade/log/CUSSTATUS/").mkdirs();
				        
				        try {
							  fwriter = new FileWriter("D:/www/api/etrade/log/CUSSTATUS/S"+nowtime+".xml");
							  fwriter.write(result.toString());
							 } catch (IOException ex) {
							  ex.printStackTrace();
							 } finally {
							  try {
							   fwriter.flush();
							   fwriter.close();
							  } catch (IOException ex) {
							   ex.printStackTrace();
							  }
							 }
				      //处理回传状态
						//String inStatus = XML.parseXml("orders","status","CUSSTATUS/"+batchNumber+"/S"+orderNumber+".xml");	 
						Map<String, String> outinStatus = XmlLForStatus.getStatus(result.toString());	 
	
			    }  
			} catch (Exception e) {  
			    System.out.println("err");  
			} finally {  
			    if (http != null) http.disconnect();   
			}  
		}
	
}
