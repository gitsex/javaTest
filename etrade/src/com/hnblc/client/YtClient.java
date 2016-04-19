package com.hnblc.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hnblc.utils.DBsql;
import com.hnblc.utils.EncryptUtil;
import com.hnblc.utils.XMLforYT;

public class YtClient {
	
	public static  Map<String, String> account;
	
	//密钥
	public static String Key = "INTERFACESTANDARDENCRYPTKEY2014";
	//公司编码
	public static String CompanyCode = "QHKZH";
	
	//清关运单信息测试接口
	//public static String BlcUrl = "http://gea.hengtiansoft.com/globalunion/outcall/blcWayBill";
	
	//清关运单信息正式接口
	public static String BlcUrl = "http://lmtest.yto.net.cn/globalunion/outcall/blcWayBill";
	
	public  static String doGet(String orderNumber) throws UnsupportedEncodingException  
	{
		 String data ="";
		 List<Map<String, String>> Order =DBsql.getOrderList("where orderNumber='"+orderNumber+"' Limit 1");
		   if(Order.size()>0)
		   {
		    Map<String, String> item = Order.get(0);  
		    account = DBsql.getAccount(item.get("shopName"));
		    String batchNumber = item.get("batchNumber");
		    try {
				DBsql.Lock(orderNumber);
			} catch (UnsupportedEncodingException e3) {
				e3.printStackTrace();
			}
		    String Remark=null;
		   try {
			   
				data = getXml(Order,batchNumber);
				System.out.println(data);
				if(data.equals(""))
				{
					
					System.out.print("商品无法匹配");
					//return  "";
				}
				//response.getWriter().println(xml);
				//调用加密方法
				String encryptData = EncryptUtil.buildEncryptStandardSenderData(Key, data, CompanyCode);
				//已加密参数
				//System.out.println("已加密参数: " + encryptData);
				//发送 POST请求，调用接口
				
		        String str=YtClient.sendPost(BlcUrl, encryptData,batchNumber,orderNumber);
		        //接口返回数据
		        //System.out.println(str);
			    //ret= new String(ret.getBytes("GBK"),"UTF-8");   
			    Map<String, String> statusxml= XMLforYT.getStatus(str.toString());

				String status = statusxml.get("Status");
				String logisticsNo = statusxml.get("logisticsNo");
			    Remark = statusxml.get("Remark");

				Map<String, String> update= new HashMap<String,String>();
				 
				 update.put("logisticsExchangeStatus","2");
				 DBsql.updateOrder(update,orderNumber);
				 
							if(status.equals("true"))
							{
								 update.put("tracking_no",logisticsNo);
								 update.put("logisticsExchangeStatus","1");
								 DBsql.updateOrder(update,orderNumber);
								 
							}else{
								
								//回传报错
								 Map<String, String> logisticsExchangeStatus= new HashMap<String,String>();
								 logisticsExchangeStatus.put("logisticsExchangeStatus","2");
								 DBsql.updateOrder(logisticsExchangeStatus,orderNumber);
								 
								 Date nowTime=new Date(); 
								 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
								 String creatTime = time.format(nowTime);
								 
								 Map<String, String> Error= new HashMap<String,String>();
								 Error.put("type","圆通报错");
								 Error.put("errordesc",Remark);
								 Error.put("creatTime",creatTime);
								 Error.put("shopName",account.get("shopName"));
								 Error.put("readTime", "00-00-00 00:00:00");
								 Error.put("reSendTime", "00-00-00 00:00:00");
								 Error.put("toUser", account.get("responsible"));
								 Error.put("orderNumber", orderNumber);
								 try {
										DBsql.addError(Error);
									} catch (UnsupportedEncodingException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
										
									}
							}
		         } catch (Exception e) {
		        	 
		         //接口报错
		    	//response.getWriter().println(ret);
				 Map<String, String> update= new HashMap<String,String>();
				 update.put("logisticsExchangeStatus","2");
				 
				 try {
					DBsql.updateOrder(update,orderNumber);
				} catch (UnsupportedEncodingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				 Date nowTime=new Date(); 
				 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
				 String creatTime = time.format(nowTime);
				 
				 //推送过程申通保存 描述中增加查看回传报文的链接
				 Map<String, String> Error= new HashMap<String,String>();
				 Error.put("type","圆通报错");
				 Error.put("errordesc","报文接口错误");
				 Error.put("creatTime",creatTime);
				 Error.put("shopName",account.get("shopName"));
				 Error.put("readTime", "00-00-00 00:00:00");
				 Error.put("reSendTime", "00-00-00 00:00:00");
				 Error.put("toUser", account.get("responsible"));
				 Error.put("orderNumber", orderNumber);
				 try {
					DBsql.addError(Error);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}
				
				e.printStackTrace();
			}
	
				    try {
						DBsql.unLock(item.get("orderNumber"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 
		   }

		   return "";
		
	}
	
	//生成随机数
	public static String getRandom(int Num) {
        int max=9;
        int min=1;
        Random random = new Random();

        String r = "";
        for(int i=1;i<=Num;i++)
        {
        	 int s = random.nextInt(max)%(max-min+1) + min;
        	 
        	 r = r+String.valueOf(s);
        }
       
        
        return r;
    }
	
	//生成报文
	public static String getXml(List<Map<String, String>> Order,String batchNumber) {
		DecimalFormat df = new DecimalFormat("#.00");
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat  sdf2 = new SimpleDateFormat("yyyyMMdd");
		Long  nowtimestamp=new Date().getTime();
		String nowtime = sdf.format(nowtimestamp);
		String nowtime2 = sdf2.format(nowtimestamp);
    	nowtimestamp = (Long)nowtimestamp/1000;
    	
		 StringBuilder xml = new StringBuilder();
	        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        FileWriter fwriter = null;
	 
	        Map<String, String> item = Order.get(0);  
	        
	        xml.append("\n  <Message>");
	    	xml.append("\n  <Header>");
	    	 xml.append("\n  <seqNo>"+getRandom(12)+nowtime2+"</seqNo>");
	    	       xml.append("\n  <sendTimeStamp>"+nowtime+"</sendTimeStamp>");
	         xml.append("\n  </Header>");
	    	xml.append("\n  <RequestOrder>");
	    		  xml.append("\n  <clientID>QHKZH</clientID>");
	    		  xml.append("\n  <logisticProviderID>YTO</logisticProviderID>");
	               xml.append("\n  <customsID>ZZHG</customsID>");
	    		  xml.append("\n  <dataType>BL</dataType>");
	    		  xml.append("\n  <logisticsCode>L0007</logisticsCode>");
	    		  xml.append("\n  <logisticsName>河南圆通速递有限公司</logisticsName>");
	    		  xml.append("\n  <logisticsCodeCiq>4100300005</logisticsCodeCiq>");
	    		  xml.append("\n  <logisticsNameCiq>河南省圆通速递有限公司</logisticsNameCiq>");
	    		  xml.append("\n  <totalLogisticsNo></totalLogisticsNo>");
	    		  xml.append("\n  <subLogisticsNo></subLogisticsNo>");
	    		  xml.append("\n  <logisticsNo></logisticsNo>");
	    		  if("普丽普莱海外旗舰店".equalsIgnoreCase(account.get("shopName"))){
	    			  xml.append("\n  <orderNo>"+"T2700P"+item.get("orderNumber")+"</orderNo>"); 
			      }else{
			    	  xml.append("\n  <orderNo>"+item.get("orderNumber")+"</orderNo>"); 
			      }
	    		  xml.append("\n  <platformCode>"+account.get("companyCode")+"</platformCode>");
	    		  xml.append("\n  <platformName>"+account.get("companyName")+"</platformName>");
	    		  xml.append("\n  <platformCodeCiq>"+account.get("companyCode")+"</platformCodeCiq>");   
	    		  xml.append("\n  <platformNameCiq>"+account.get("companyName")+"</platformNameCiq>");
	    		  xml.append("\n  <trackNo></trackNo>");
	    		  xml.append("\n  <trackStatus></trackStatus>");	
	    		  
	    		  xml.append("\n  <shipping>"+account.get("shipper")+"</shipping>");
	    		  xml.append("\n  <shippingAddress>"+account.get("shipperAddress")+"</shippingAddress>");
	    		  xml.append("\n  <shippingTelephone>"+account.get("shipperTelephone")+"</shippingTelephone>");
	    		  xml.append("\n  <shippingZipCode></shippingZipCode>");
	    		  xml.append("\n  <shippingCountryCiq>036</shippingCountryCiq>	");	  		      
	    		  xml.append("\n  <shippingCountryCus>601</shippingCountryCus> ");
	    		  
	    		  
	    		  xml.append("\n  <consignee>"+item.get("cosignee")+"</consignee>");
	    		  xml.append("\n  <consigneeAddress>"+item.get("address")+"</consigneeAddress>");

	    	      String consigneeTelephone=item.get("mobilePhone").equals("") ? item.get("telephone") : item.get("mobilePhone");
	    	        
	    		  xml.append("\n  <consigneeTelephone>"+consigneeTelephone+"</consigneeTelephone>");
	    		  xml.append("\n  <consigneeZipCode>100000</consigneeZipCode>");
	    		  xml.append("\n  <consigneeCountryCiq>156</consigneeCountryCiq>");		               	  
	    		  xml.append("\n  <consigneeCountryCus>142</consigneeCountryCus>");
	    		  xml.append("\n  <idType>1</idType>");
	    		  
	    		  xml.append("\n  <idNumber>"+item.get("idCardName")+"</idNumber>");
	    		  xml.append("\n  <declarationDate></declarationDate>");
	    		  xml.append("\n  <internationalFreight></internationalFreight>");
	    		  xml.append("\n  <domesticFreight></domesticFreight>");
	    		  xml.append("\n  <supportValue></supportValue>");
	    		  
	    		  Double  totalgoodsprice=0.00;
	    		  String goodsname = "";
	    		  List<Map<String, String>> ordergoods =DBsql.getOrderGoodsList("where orderNumber='"+item.get("orderNumber")+"'");
	    	        for(int i = 0; i < ordergoods.size(); i++)  
	    	        {  
	    	        	Map<String, String> goodsitem = ordergoods.get(i);  
	    	            //System.out.println(list.get(i)); 
	    	        	
	    	        	if("APPLE（保税）".equalsIgnoreCase(account.get("shopName"))||"易恒健康（海外）".equalsIgnoreCase(account.get("shopName"))){
	    	        	      totalgoodsprice += Double.valueOf(goodsitem.get("price"))-Double.valueOf(goodsitem.get("TaxFee"))-Double.valueOf(goodsitem.get("discount"));//商品小计
	    	        	}else{
	    	        		totalgoodsprice += Double.valueOf(df.format(Double.valueOf(goodsitem.get("price"))/1.119));
	    	        	}
	    	        	//商品对照表
	    	        	String goodsNo = DBsql.getGoodsMatch(goodsitem.get("productNumber"),account.get("shipper"));
	    				
	    	        	//换成其中一个商品
	    	        	goodsname =goodsitem.get("productName");// goodsname+
	    	        	
	    	        	if(goodsNo.equals(""))
	    				{
	    					//商品编码无法匹配
	    					
	    					 Date nowTime=new Date(); 
	    					 SimpleDateFormat time = new SimpleDateFormat("YY-MM-dd HH:mm:ss"); 
	    					 String creatTime = time.format(nowTime);
	    					 
	    					//推送过程申通保存 描述中增加查看回传报文的链接
	    					 Map<String, String> Error= new HashMap<String,String>();
	    					 Error.put("type","商品报错");
	    					 Error.put("errordesc",goodsitem.get("productNumber")+"商品编码无法匹配");
	    					 Error.put("creatTime",creatTime);
	    					 Error.put("shopName",account.get("shopName"));
	    					 Error.put("readTime", "00-00-00 00:00:00");
	    					 Error.put("reSendTime", "00-00-00 00:00:00");
	    					 Error.put("toUser", account.get("responsible"));
	    					 Error.put("orderNumber", item.get("orderNumber"));
	    					 try {
	    						DBsql.addError(Error);
	    					} catch (UnsupportedEncodingException e1) {
	    						// TODO Auto-generated catch block
	    						e1.printStackTrace();
	    						
	    					}
	    					
	    					   //return "";
	    					
	    				}
	    	        }	
	    		  xml.append("\n  <worth>"+totalgoodsprice+"</worth>");
	    		  xml.append("\n  <currCode></currCode>");
	    		  xml.append("\n  <grossWeight>1.00</grossWeight>");
	    		  xml.append("\n  <quantity>"+ordergoods.size()+"</quantity>");
	    		  xml.append("\n  <packageTypeCiq>4M</packageTypeCiq>");
	    		  xml.append("\n  <packageTypeCus></packageTypeCus>");
	    		  xml.append("\n  <packNum>1</packNum>");
	    		  xml.append("\n  <netWeight>0.6</netWeight>");
	    		  
	    		  xml.append("\n  <goodsName>"+goodsname+"</goodsName>");
	    		  
	    		  xml.append("\n  <deliveryMethod></deliveryMethod>");
	    		  xml.append("\n  <transportationMethod>7</transportationMethod>");		      		  
	    		  xml.append("\n  <shipCode>"+account.get("shipCode")+"</shipCode>");
	    		  xml.append("\n  <shipName></shipName>");
	    		  xml.append("\n  <destinationPort></destinationPort>");
	    		  xml.append("\n  <ieType>I</ieType>");
	    		  xml.append("\n  <stockFlag>1</stockFlag>");
	    		  xml.append("\n  <batchNumbers>"+batchNumber+"</batchNumbers>");
	    		  xml.append("\n  <tradeCountryCiq>036</tradeCountryCiq>");
	    		  xml.append("\n  <tradeCountryCus></tradeCountryCus>");
	    		  xml.append("\n  <agentCode>"+account.get("agentCode")+"</agentCode>");
	    		  xml.append("\n  <agentName>"+account.get("agentNameCiq")+"</agentName>");
	    		  xml.append("\n  <agentCodeCiq></agentCodeCiq>");
	    		  xml.append("\n  <agentNameCiq></agentNameCiq>");
	    		  xml.append("\n  <billType>3</billType>");
	    		  xml.append("\n  <modifyMark>"+item.get("IsSend")+"</modifyMark>");
	    		  xml.append("\n  <customsField></customsField>");
	    		  xml.append("\n  <note></note>");
	    		  xml.append("\n  <reserve1></reserve1>");
	    		  xml.append("\n  <reserve2></reserve2>");
	    		  xml.append("\n  <reserve3></reserve3>");
	    		  xml.append("\n  <reserve4></reserve4>");
	    		  xml.append("\n  <reserve5></reserve5>");
	    	xml.append("\n  </RequestOrder>");
	    xml.append("\n  </Message>");
	    
      new File("D:/www/api/etrade/log/YT/"+batchNumber).mkdirs();
        
        try {
			  fwriter = new FileWriter("D:/www/api/etrade/log/YT/"+batchNumber+"/Send"+item.get("orderNumber")+".xml");
			  fwriter.write(xml.toString());
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
        
        return xml.toString();
	}
	
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String batchNumber,String orderNumber) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        FileWriter fwriter = null;
    	//保存回传结果
        try {
			  fwriter = new FileWriter("D:/www/api/etrade/log/YT/"+batchNumber+"/Return"+orderNumber+".xml");
			  fwriter.write(result);
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
        return result;
    }    
}

