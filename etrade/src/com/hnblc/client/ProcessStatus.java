package com.hnblc.client;


import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnblc.utils.Curl;
import com.hnblc.utils.DBsql;

public class ProcessStatus {

	
	public static int getorderstatusdifftime= 1; //报关状态推送频率
	
	public static int logisticstdifftime =1; //物流推送频率
	
	public static int logisticstinfodifftime =3600*18; //物流推送频率
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long  nowtimestamp=new Date().getTime();
		String nowtime = sdf.format(nowtimestamp);
    	nowtimestamp = (Long)nowtimestamp/1000;
	

		//订单抓取 每隔10分钟 推送已审核订单
		
		 Map<String, String> task= DBsql.getTaskLog();
		 
//		 String sendsttime = task.get("sendsttime");
//		 long sttime = getTime(sendsttime);
		 
		 String getorderstatustime = task.get("getorderstatustime");
		 long orderstatustime = getTime(getorderstatustime);
		 
		 String sendlogisticstime = task.get("sendlogisticstime");
		 long logisticstime = getTime(sendlogisticstime);
		 
		 String sendemailtime = task.get("sendemailtime");
		 long emailtime = getTime(sendemailtime);
		 
		 String sendlogisticsinfotime = task.get("sendlogisticsinfotime");
		 long logisticsinfotime = getTime(sendlogisticsinfotime);
		 

		 
	//订单状态更新  每30分钟处理4小时内未更新海关状态的订单，最多提交1000单，并记录推送时间
		if(nowtimestamp-orderstatustime>getorderstatusdifftime)
		{
            long etradestatustime = nowtimestamp-3000;
			
			String EtradeStatusTime = sdf.format(etradestatustime*1000);
			
		  	
		  	SimpleDateFormat  sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		  	
		  	String updateorderstatustime = sdf2.format(nowtimestamp*1000);
		  	
		  	//获取总数量
		  	//batchNumber='YD20160510010' AND
			String  where =" Ismatch=1 AND logisticsExchangeStatus=1 AND customsTransStatus=1 AND EtradeOutStatus <>'已结关' AND EtradeStatusTime< '"+EtradeStatusTime+"'";
			int count  =DBsql.getTotalNeedEtradeStatusOrder(where);
			
			count = count/1000;
			
			System.out.print(count);
			for(int i=0;i<=count;i++)
			{
				
			//batchNumber='YD20160510010' AND
			 where =" Ismatch=1 AND logisticsExchangeStatus=1 AND customsTransStatus=1 AND EtradeOutStatus <>'已结关' AND EtradeStatusTime< '"+EtradeStatusTime+"' ORDER BY `EtradeStatusTime` DESC  limit "+i*1000+",1000";
			
		  	 OrderStatus.getOrderStatus(where,updateorderstatustime,nowtime);
		
		  	 try {
					DBsql.updateTaskLog("getorderstatustime", nowtime);
				  } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
				  
			}
		 }
		
		
		//物流信息同步
		if(nowtimestamp-logisticstime>logisticstdifftime)
		{
			
			List<Map<String, String>> Order =DBsql.getOrderList("where is_lock=0 AND  Ismatch=1 AND logisticsExchangeStatus=1 AND customsTransStatus=1  AND erpTransStatus=0 AND ( EtradeOutStatus ='放行' OR EtradeOutStatus ='已结关')");
			  
			for(int i=0;i<Order.size();i++)
			{
				 Map<String, String> item = Order.get(i);  
				 
				 String orderNumber = item.get("orderNumber");
				 String tracking_no = item.get("tracking_no");
				//日志记录
				 try {	
					   LogisticsBack.doLogisticsBack(orderNumber,tracking_no);
					   Map<String, String>  StatusLog=new HashMap<String,String>();
					   
					   String isLogExist =DBsql.isLogExist(orderNumber,"物流回传");
						if(isLogExist.equals(""))
						{
							//添加记录
						    StatusLog.put("EtradeOutStatusTime",nowtime);
	                        StatusLog.put("EtradeOutStatus","物流回传");
							StatusLog.put("orderNumber",orderNumber);
							StatusLog.put("CreateTime",nowtime);
							DBsql.addOutstatusLog(StatusLog);
						}
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			   try {
					DBsql.updateTaskLog("sendlogisticstime", nowtime);
				  } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }

		 }
		
		
		//圆通物流信息追踪 每天一次 
		if(nowtimestamp-logisticsinfotime>logisticstinfodifftime)
		{
			
			List<Map<String, String>> Order =DBsql.getOrderList("where is_lock=0 AND  Ismatch=1 AND logisticsExchangeStatus=1 AND customsTransStatus=1  AND erpTransStatus=1 AND LogisticsInfo=0  AND tracking_com='YTO'");
			  
			for(int i=0;i<Order.size();i++)
			{
				 Map<String, String> item = Order.get(i);  
				 
				 String orderNumber = item.get("orderNumber");
				 String tracking_no = item.get("tracking_no");
				//
				 try {	
					   LogisticsInfo.doLogisticsInfoGet(orderNumber,tracking_no);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			   try {
					DBsql.updateTaskLog("sendlogisticsinfotime", nowtime);
				  } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }

		 }
		
		//邮件推送
//		if(nowtimestamp-emailtime>1000)
//		{
//			SendEmail.DoSendEmail();
//			
//			 try {
//					DBsql.updateTaskLog("sendemailtime", nowtime);
//				  } catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				  }
//		}
		
		
	}


	//时间戳转化
	public static Long getTime(String stringtime) { 
		long timestamp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date d; 
		try { 
		d = sdf.parse(stringtime); 
	      timestamp = d.getTime(); 
		//String str = String.valueOf(l); 
		timestamp = (Long)timestamp/1000;
		  //re_time = str.substring(0, 10); 
		} catch (ParseException e) { 
		   // TODO Auto-generated catch block 
		e.printStackTrace(); 
		} 
		    return timestamp; 
		} 
}
