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

public class LogisticsBack {

	/**
	 * @param args
	 */
	public static  Map<String, String> account;
	
	public static void doLogisticsBack(String orderNumber,String expressNumber) {
		// TODO Auto-generated method stub
		
		 Map<String, String> item = new HashMap<String, String>();  
		 List<Map<String, String>> Order =DBsql.getOrderList("where orderNumber='"+orderNumber+"'  Limit 1");
		   if(Order.size()>0)
		   {

			item = Order.get(0);  
			account = DBsql.getAccount(item.get("shopName"));
		   }
		   
			URL url = null;  
			HttpURLConnection http = null;  
			try {  
			    url = new URL("http://openapi.ecmoho.com/M/trade_sync_logistics");  
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
			    
			    //采用ERP单号直接回填
			    String param = "erporderNumber="+item.get("trace_no")+"&logisticCode="+item.get("tracking_com")+"&expressNumber="+expressNumber+"&consign_type=0&Token=261d1aef9464c32bad82fc3adafe5448";   
			    
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
			        
			        JSONObject retUtf8tObj=JSONObject.fromObject(result);
					 
					 //判断是否抓取成功
					String isSuccess=  retUtf8tObj.getString("code"); 

//					JSONObject retUtf8tObjMessage=JSONObject.fromObject(retUtf8tObj.getString("message"));
//					
//				    System.out.println(retUtf8tObjMessage.getString("error"));  
//						
//						
					 in.close();  	
			        
			        //更新物流推送状态
			        if(isSuccess.equals("0"))
			        {
			        	 Map<String, String> update= new HashMap<String,String>();
						 update.put("erpTransStatus","1");
						 try {
							DBsql.updateOrder(update,orderNumber);
						 } catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						 }
						 
			        }else{
			        	
			        	 Map<String, String> update= new HashMap<String,String>();
						 update.put("erpTransStatus","2");
						 try {
							DBsql.updateOrder(update,orderNumber);
						 } catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						 }
						 
			        	 Date nowTime=new Date(); 
						 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
						 String creatTime = time.format(nowTime);
						 //推送过程订单报错  描述中增加查看回传报文的链接
						 Map<String, String> Error= new HashMap<String,String>();
						 Error.put("type","运单回传报错");
						 
						 String message = retUtf8tObj.getString("message");
						 message = message.replace("\"", "'");
						 Error.put("errordesc","运单回写失败:"+message);
						 Error.put("creatTime",creatTime);
						 Error.put("shopName",account.get("shopName"));
						 Error.put("readTime", "00-00-00 00:00:00");
						 Error.put("reSendTime", "00-00-00 00:00:00");
						 Error.put("toUser", "chenshenghong@ecmoho.com");
						 Error.put("orderNumber", orderNumber);
						 try {
							DBsql.addError(Error);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			    }else{
			    	//状态报错
					 Map<String, String> update= new HashMap<String,String>();
					 update.put("erpTransStatus","2");
					 try {
						DBsql.updateOrder(update,orderNumber);
					 } catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
					
					 Date nowTime=new Date(); 
					 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
					 String creatTime = time.format(nowTime);
					 //推送过程订单报错  描述中增加查看回传报文的链接
					 Map<String, String> Error= new HashMap<String,String>();
					 Error.put("type","运单回传报错");
					 Error.put("errordesc","运单回写接口链接超时");
					 Error.put("creatTime",creatTime);
					 Error.put("readTime", "00-00-00 00:00:00");
					 Error.put("reSendTime", "00-00-00 00:00:00");
					 Error.put("toUser", "chenshenghong@ecmoho.com");
					 Error.put("orderNumber", orderNumber);
					 try {
						DBsql.addError(Error);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
