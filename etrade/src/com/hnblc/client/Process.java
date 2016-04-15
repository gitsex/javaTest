package com.hnblc.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnblc.utils.DBsql;

public class Process {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        String batchNumber ="YPG20160414010";
//		 String batchNumber = DBsql.readBatchNumber();
//		 System.out.println(batchNumber);
		
		//申通谁送
//	    List<Map<String, String>> Order =DBsql.getOrderList("where is_lock=0 AND Ismatch=1 AND batchNumber='"+batchNumber+"' AND  logisticsExchangeStatus=0 AND orderStatus='已确认' ORDER BY id DESC");
//	  
//		for(int i=0;i<Order.size();i++)
//		{
//			 Map<String, String> item = Order.get(i);  
//			 
//			 String orderNumber = item.get("orderNumber");
//		
//				try {
//					  StoClient.doGet(orderNumber);
//				    } catch (UnsupportedEncodingException e1) {
//						// TODO Auto-generated catch block
//							e1.printStackTrace();
//					}
//				    
//		}
//		
		// 圆通推送
		
		List<Map<String, String>> Order =DBsql.getOrderList("where is_lock=0 AND Ismatch=1 AND batchNumber='"+batchNumber+"' AND  logisticsExchangeStatus=0 AND orderStatus='已确认' ORDER BY id DESC");
		  
		for(int i=0;i<Order.size();i++)
		{
			 Map<String, String> item = Order.get(i);  
			 
			 String orderNumber = item.get("orderNumber");
		
				try {
					  YtClient.doGet(orderNumber);
				    } catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
							e1.printStackTrace();
					}
				    
		}
	 //海关推送
		Order =DBsql.getOrderList("where is_lock=0 AND Ismatch=1 AND batchNumber='"+batchNumber+"' AND logisticsExchangeStatus=1 AND customsTransStatus=0 AND orderStatus='已确认' ORDER BY id DESC");
        System.out.println(Order.size());
		for(int i=0;i<Order.size();i++)
		{
			 Map<String, String> item = Order.get(i);  
			 
			 String orderNumber = item.get("orderNumber");
			//
			 try {	
				 
					OrderClient.doSend(orderNumber);
				 
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long  nowtimestamp=new Date().getTime();
		String nowtime = sdf.format(nowtimestamp);
		
		//更新推送时间 customsTransTime
		 //状态报错
		 Map<String, String> update= new HashMap<String,String>();
		 update.put("customsTransTime",nowtime);
		 try {
			DBsql.updateOrderForbatchNumber(update,batchNumber);
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		 }
		
	}	
		
}



