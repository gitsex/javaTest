package com.hnblc.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hnblc.utils.Curl;
import com.hnblc.utils.DBsql;
import com.hnblc.utils.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ErpClient {

	public static void GetOrders(String orderNumber) throws UnsupportedEncodingException {
		       //抓取批次
		 Date nowTime=new Date(); 
		 SimpleDateFormat time = new SimpleDateFormat("yyMMddHH"); 
		 String batchNumber = "YD"+time.format(nowTime);
		 
		        FileWriter fwriter = null;
		        String isExist =null;
				int isLog,isInvoice;
				isLog=isInvoice=0;
	        	String	name ="易恒订单查询";
	        	String  nick="普丽普莱海外旗舰店";
	        	String  method="swapi.data.erp.order.detail";
	        	String  format="json";
	        	Long  timestamp=new Date().getTime();
	        	timestamp = timestamp/1000;
				String timestampString = String.valueOf(timestamp);
	        	
	        	//每次请求处理一个订单
	        	
	        	String  sign=MD5.MD5_32(MD5.encode(nick.getBytes("UTF-8"))+MD5.encode(method.getBytes())
	        			+MD5.encode(format.getBytes())+MD5.encode(timestampString.getBytes()));
	        	
				 name =java.net.URLEncoder.encode("易恒订单查询","utf-8");
				 String nick_encode=java.net.URLEncoder.encode(nick,"utf-8");
				 
				 String Param="orderNumber="+orderNumber+"&name="+name+"&nick="+nick_encode+"&method="+method+"&format="+format+"&isLog="
				 +isLog+"&isInvoice="+isInvoice+"&timestamp="+timestampString+"&sign="+sign+"&Token=261d1aef9464c32bad82fc3adafe5448";
				 String ret = Curl.sendGet("http://openapi.ecmoho.com/M/OrderDetail",Param);
				 //response.getWriter().println(ret);
				 System.out.print(ret);
				 //处理json信息
				 JSONObject retUtf8tObj=JSONObject.fromObject(ret);
				 
				 //判断是否抓取成功
				 String isSuccess=  retUtf8tObj.getString("isSuccess"); 
				 if(isSuccess==null || isSuccess=="false")
				 {
					 
					 try {
						  fwriter = new FileWriter("D:/etrade/log/ERP/"+orderNumber+".log");
						  fwriter.write(ret);
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
						 //更新数据库
						 Map<String, String> update= new HashMap<String,String>();
						 update.put("erpMatchingStatus","2");
						 DBsql.updateOrder(update,orderNumber);
						//订单解锁
						 DBsql.unLock(orderNumber);
				 }else{
					 //获取订单与商品信息
							 
					            JSONObject erpOrders= retUtf8tObj.getJSONObject("erpOrders"); //
					           
					            JSONArray erpOrderArr = erpOrders.getJSONArray("erpOrder");
					            
					            JSONObject erpOrderObj = (JSONObject) erpOrderArr.get(0);
					            
					            Map<String, String> update= new HashMap<String,String>();
					            update.put("shopName",nick);
					            update.put("orderNumber",orderNumber);
					            update.put("batchNumber",batchNumber);
					            update.put("created",(String) erpOrderObj.get("created"));
								update.put("initialActualAmount",(String) erpOrderObj.get("actualAmount"));
								update.put("orderStatus",(String) erpOrderObj.get("orderStatus"));
								update.put("created",(String) erpOrderObj.get("created"));
								update.put("payTime",(String) erpOrderObj.get("payTime"));
								update.put("orderDate",(String) erpOrderObj.get("orderDate"));
								update.put("memberName",(String) erpOrderObj.get("memberName"));
								update.put("cosignee",(String) erpOrderObj.get("consignee"));
								update.put("address",(String) erpOrderObj.get("address"));
								update.put("mobilePhone",(String) erpOrderObj.get("mobilePhone"));
								update.put("telephone",(String) erpOrderObj.get("telephone"));
								update.put("memo",(String) erpOrderObj.get("memo"));
								//update.put("sellerMemo",(String) erpOrderObj.get("sellerMemo"));
								update.put("deliveryCost",(String) erpOrderObj.get("deliveryCost"));
								update.put("amount",(String) erpOrderObj.get("totalAmount"));
								update.put("mark",(String) erpOrderObj.get("actualAmount"));
								update.put("province",(String) erpOrderObj.get("province"));
								update.put("city",(String) erpOrderObj.get("city"));
								update.put("area",(String) erpOrderObj.get("area"));
								
								update.put("is_lock","0");
								update.put("logistics_no",(String) erpOrderObj.get("expressNumber"));
								update.put("erpMatchingStatus","1");
								//如果订单存在 则更新订单
							     isExist =DBsql.isExist(orderNumber);
								if(isExist.equals(""))
								{
									DBsql.addOrder(update);
								}
								else{
									DBsql.updateOrder(update, orderNumber);
								}
								
   				                
   				                //更新订单商品部分
   				                
   				                JSONObject erpOrderItems = erpOrderObj.getJSONObject("erpOrderItems");
   				                JSONArray erpOrderItem = erpOrderItems.getJSONArray("erpOrderItem");
   				                
		   				             for(int i=0;i<erpOrderItem.size();i++){
		   				            	 
		   				            	JSONObject erpOrderItemRow= (JSONObject) erpOrderItem.get(i); //
		   				            	
		   				            	Map<String, String> insertgoods= new HashMap<String,String>();
		   				            	insertgoods.put("orderNumber",orderNumber);
		   				            	insertgoods.put("price",(String) erpOrderItemRow.get("amount"));
		   				            	insertgoods.put("productNumber",(String) erpOrderItemRow.get("productNumber"));
		   				            	insertgoods.put("productName",(String) erpOrderItemRow.get("productName"));
		   				            	insertgoods.put("orderCount",String.valueOf(erpOrderItemRow.get("orderCount")));
		   				            	insertgoods.put("giftCount",String.valueOf(erpOrderItemRow.get("giftCount")));
		   				            	
		   				                isExist =DBsql.isExistGoods(orderNumber,(String) erpOrderItemRow.get("productNumber"));
		   				            	if(isExist.equals(""))
										{
		   				            		DBsql.addOrderGoods(insertgoods);
										}
										else{
											DBsql.updateOrderGoods(insertgoods, orderNumber);
										}
		   				            	
		   				             }
		   				        //订单解锁
		   				          DBsql.unLock(orderNumber);
				 }
				 
				
        }  

	

	  
}
