package com.hnblc.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XmlLForStatus {

	   public static  Map<String, String> getStatus(String xml) {
	        
	        long start = System.currentTimeMillis();
	 
	        Map<String, String>  Status=new HashMap<String,String>();
	        Map<String, String>  StatusLog=new HashMap<String,String>();
	        Map<String, String>  DisStatusLog=new HashMap<String,String>();
	        
	        SAXReader reader = new SAXReader();
	        try {
	            org.dom4j.Document doc = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
	            org.dom4j.Element root = doc.getRootElement();
	            org.dom4j.Element element;
	            org.dom4j.Element element2;
	            org.dom4j.Element element3;
	            
	            for (Iterator<?> i = root.elementIterator("orders"); i.hasNext();) {
	            	
	                element = (org.dom4j.Element) i.next();
//	                System.out.println("name:[" + element.elementText("name") + "]");
//	                System.out.println("age:[" + element.elementText("age") + "]");
	                for (Iterator<?> j = element.elementIterator("order"); j.hasNext();) {
	                	

		            	  //Status.put("EtradeStatusTime","00-00-00 00:00:00");
		                  //Status.put("EtradeStatus","");
	                	 SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Long  nowtimestamp=new Date().getTime();
							String nowtime = sdf.format(nowtimestamp);
							
	                	  Status.put("EtradeStatusTime", nowtime);
		                  Status.put("EtradeOutStatusTime","0000-00-00 00:00:00");
		                  Status.put("EtradeOutStatus","无状态");
		                  Status.put("EtradeOutDisStatusTime","0000-00-00 00:00:00");
		                  Status.put("EtradeOutDisStatus","无状态");

	                    element2 = (org.dom4j.Element) j.next();
	                   
	                    String orderNumber = element2.elementText("orderNo");
	                    
//	                    for (Iterator<?> k = element2.elementIterator("in"); k.hasNext();)
//	                    {
//	                        element3 = (org.dom4j.Element) k.next();
//	                        if(!element3.elementText("statusTime").equals(""))
//							{
//	                          Status.put("EtradeOutStatusTime",element3.elementText("statusTime"));
//	                          Status.put("EtradeInStatus",element3.elementText("status"));
//	                          StatusLog.put("EtradeInStatusTime",element3.elementText("statusTime"));
//	                          StatusLog.put("EtradeInStatus",element3.elementText("status"));
//							}
//	                    }
	                    
	                    for (Iterator<?> k = element2.elementIterator("out"); k.hasNext();) 
	                       {
	                    	     String distStatusTime="";
			                    element3 = (org.dom4j.Element) k.next();
			                    if(!element3.elementText("statusTime").equals(""))
								{
			                      if(element3.elementText("distStatusTime").equals(""))
			                      {
			                    	  distStatusTime="0000-00-00 00:00:00";
			                      }else{
			                    	  distStatusTime = element3.elementText("distStatusTime");
			                      }
			                      //申报状态
			                      Status.put("EtradeOutStatusTime",element3.elementText("statusTime"));
		                          Status.put("EtradeOutStatus",element3.elementText("status"));
		                         
		                          //申报日志
		                          StatusLog.put("EtradeOutStatusTime",element3.elementText("statusTime"));
		                          StatusLog.put("EtradeOutStatus",element3.elementText("status"));
		                          
		                          //核放状态
		                          Status.put("EtradeOutDisStatusTime",distStatusTime);
		                          Status.put("EtradeOutDisStatus",element3.elementText("distStatus"));
		                          
		                          //核放日志
		                          DisStatusLog.put("EtradeOutStatusTime",distStatusTime);
		                          DisStatusLog.put("EtradeOutStatus",element3.elementText("distStatus"));
								}
			                   
			                }
	                    
	                    //状态与日志处理
	                    System.out.println(Status.get("EtradeOutStatus"));
	                    if(!Status.get("EtradeOutStatus").equals(""))
						{
							 try {
								    
								    Status.put("logisticsExchangeStatus","1");
								    Status.put("customsTransStatus","1");
								    DBsql.updateOrder(Status,orderNumber);
									
									//如果没有此状态则添加记录，记录首条记录 不重复记录
									
									//申报日志
									String isLogExist =DBsql.isLogExist(orderNumber,StatusLog.get("EtradeOutStatus"));
									if(isLogExist.equals(""))
									{
										StatusLog.put("orderNumber",orderNumber);
										StatusLog.put("CreateTime",nowtime);
										DBsql.addOutstatusLog(StatusLog);
									}
									
									//核放日志
									 isLogExist =DBsql.isLogExist(orderNumber,DisStatusLog.get("EtradeOutStatus"));
									if(isLogExist.equals(""))
									{
										DisStatusLog.put("orderNumber",orderNumber);
										DisStatusLog.put("CreateTime",nowtime);
										DBsql.addOutstatusLog(DisStatusLog);
									}
									
								 } catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}else{
						   System.out.println("无状态");
						   DBsql.updateOrder(Status,orderNumber);
						}
	                }
	            }
	        } catch (DocumentException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        long end = System.currentTimeMillis();
	        System.out.println("耗时：" + (end - start) + "ms");
	        
	       return Status;
	    }
	}
