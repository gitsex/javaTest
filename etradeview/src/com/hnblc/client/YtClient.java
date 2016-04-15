package com.hnblc.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hnblc.utils.DBsql;

public class YtClient {
	//测试密钥
	public static  Map<String, String> account;
	
	public static String Key = "INTERFACESTANDARDENCRYPTKEY2014";
	//测试公司编码
	public static String CompanyCode = "QHKZH";

	public static String xmlUrl = "";
	
	public static void main(String[] args)  {
		try {
			doGet("51860414133744");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
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
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			    data = getXml(Order,batchNumber);
			    return data;
		   }

		   return "订单号有误";
		
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
		 
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat  sdf2 = new SimpleDateFormat("yyyyMMdd");
		Long  nowtimestamp=new Date().getTime();
		String nowtime = sdf.format(nowtimestamp);
		String nowtime2 = sdf2.format(nowtimestamp);
    	nowtimestamp = (Long)nowtimestamp/1000;
    	
    	//12位随机数
    	
		 StringBuilder xml = new StringBuilder();
	        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
	    		  xml.append("\n  <orderNo>"+item.get("orderNumber")+"</orderNo>"); 
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
	    	        	
	    	        	
	    	        	totalgoodsprice += Double.valueOf(goodsitem.get("price"))-Double.valueOf(goodsitem.get("TaxFee"))-Double.valueOf(goodsitem.get("discount"));//商品小计
	    	        	//商品对照表
	    	        	String goodsNo = DBsql.getGoodsMatch(goodsitem.get("productNumber"),account.get("shipper"));
	    				
	    	        	goodsname =goodsitem.get("productName");//goodsname+ 取其中一个
	    	        	
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
	    
        
        return xml.toString();
	}
	
	
	   
}

