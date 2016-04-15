package com.hnblc.client;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnblc.utils.Curl;
import com.hnblc.utils.DBsql;
import com.hnblc.utils.MD5;
import com.hnblc.utils.RSA;
import com.hnblc.utils.RSAUtil;
import com.hnblc.utils.XML;

public class StoClient {
	
	/**
	 * 
	 */
	public static  Map<String, String> account;
	
	private static final long serialVersionUID = 1L;
	
	public  static String doGet(String orderNumber,String act) throws UnsupportedEncodingException  
	{
		 
		 List<Map<String, String>> Order =DBsql.getOrderList("where orderNumber='"+orderNumber+"' Limit 1");
		   if(Order.size()>0)
		   {
		    Map<String, String> item = Order.get(0);  
		    account = DBsql.getAccount(item.get("shopName"));
		    String batchNumber = item.get("batchNumber");
		    DBsql.Lock(orderNumber);
		    String Remark=null;
		   try {
				String xml =StoClient.setXml(Order,batchNumber,act);
				if(xml.equals(""))
				{
					
					System.out.print("商品无法匹配");
					return  "";
				}
				
					if(act.equals("view"))
					{
							return xml; //预览报文
					}
				
		         } catch (Exception e) {
				    e.printStackTrace();
			     }
	
		       DBsql.unLock(item.get("orderNumber"));
    
		   }
		   
		   return "";
	}
	
	 static String getEncryptXmlStr(String xml) {
				String enStr = null;
				String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu7jiT96WeZoQ5abi1jGzmeMtypS8Pnbxc16whLeO23cUmgoan4hWjszl/f837bsajaj0iCoFLM3jEYdigq67UK7w+MmSXUiZToOEUzWkZAemX3zUQC4b0g8pOGwd6mXGoraXCfvxIbSIu+Tbxa0saZk0kksXvi1S2A0W3VQlcuwIDAQAB";
				try {
				    byte[] data = xml.getBytes("GBK");
				    byte[] encodedData = RSA.encryptByPublicKey(data, publicKey);
				    String base64str = MD5.encode(encodedData);
				    enStr = java.net.URLEncoder.encode(base64str, "GBK");
				} catch (Exception e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				return enStr;
		    }

	 static String getDataDigest(String xml) {
			String enStr = null;
			try {
			    byte[] data = xml.getBytes("GBK");
			    String encodedData = MD5.MD5(data);
			    String base64str = MD5.encode(encodedData.getBytes());
			    enStr = java.net.URLEncoder.encode(base64str, "GBK");
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			return enStr;
	    }
	 
	public static String setXml(List<Map<String, String>> Order,String batchNumber,String act) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        FileWriter fwriter = null;
 
        Map<String, String> item = Order.get(0);  

        String  CheckData=MD5.MD5_32(account.get("StoCustomerID")+item.get("orderNumber")+"@Sto.2014");
        xml.append("\n  <rows>");
        xml.append("\n  <row id=\""+1+"\">");
        xml.append("\n  <CheckData>"+CheckData+"</CheckData>");
        xml.append("\n  <declareDate>2015-03-19</declareDate>");
        xml.append("\n  <StoCustomerID>"+account.get("StoCustomerID")+"</StoCustomerID>");
        xml.append("\n  <ecpCode>"+account.get("stCompanyCode")+"</ecpCode>");
        xml.append("\n  <ecpName>"+account.get("companyName")+"</ecpName>");
        xml.append("\n  <orderNo>"+item.get("orderNumber")+"</orderNo>");
        xml.append("\n  <shipper>"+account.get("shipper")+"</shipper>");
        xml.append("\n  <shipperAddress>"+account.get("shipperAddress")+"</shipperAddress>");
        xml.append("\n  <shipperTelephone>"+account.get("shipperTelephone")+"</shipperTelephone>");
        xml.append("\n  <shipperCountryCiq>"+account.get("shipperCountryCiq")+"</shipperCountryCiq>");
        xml.append("\n  <Consignor>"+account.get("companyName")+"</Consignor>");
        xml.append("\n  <TelephoneNum>021-61131703</TelephoneNum>");
        xml.append("\n  <ConsignorAdd>中国.河南.郑州 经济技术开发区航海东路1508号</ConsignorAdd>");
        
        xml.append("\n  <consignee>"+item.get("cosignee")+"</consignee>");
        xml.append("\n  <idType>1</idType>");
        xml.append("\n  <customerId>"+item.get("idCardName")+"</customerId>");
        
        String consigneeTelephone=item.get("mobilePhone").equals("") ? item.get("telephone") : item.get("mobilePhone");

        //省市区
        String str = item.get("address");// 原始字符串  
        String[] arrayStr = new String[] {};// 字符数组  
        @SuppressWarnings("unused")
		List<String> list = new ArrayList<String>();// list  
        arrayStr = str.split(" ");// 字符串转字符数组  
        list = java.util.Arrays.asList(arrayStr);// 字符数组转list 
        
        xml.append("\n  <consigneeTelephone>"+consigneeTelephone+"</consigneeTelephone>");
        xml.append("\n  <Province>"+item.get("province")+"</Province>");//"+list.get(0)+"
        xml.append("\n  <City>"+item.get("city")+"</City>");
        
        //Zone 必须为中文
        String Zone = item.get("area").replace("(", "");
        Zone =Zone.replace(")", "");
        
        xml.append("\n  <Zone>"+Zone+"</Zone>");
        xml.append("\n  <consigneeAddress>"+item.get("address")+"</consigneeAddress>");
        xml.append("\n  <consigneeCountryCiq>"+account.get("consigneeCountryCiq")+"</consigneeCountryCiq>");
        xml.append("\n  <consigneeCountryCus>"+account.get("consigneeCountryCus")+"</consigneeCountryCus>");
        xml.append("\n  <weight>1</weight>");
        
        xml.append("\n  <quantity>1</quantity>");
        
        xml.append("\n  <ieType>"+account.get("ieType")+"</ieType>");
        xml.append("\n  <stockFlag>"+account.get("stockFlag")+"</stockFlag>");
        xml.append("\n  <batchNumbers>"+item.get("batchNumber")+"</batchNumbers>");
        xml.append("\n  <billType>3</billType>");
        xml.append("\n  <totalLogisticsNo></totalLogisticsNo>");
        xml.append("\n  <subLogisticsNo></subLogisticsNo>");
        xml.append("\n  <agentCode>"+account.get("agentCode")+"</agentCode>");
        xml.append("\n  <agentName>"+account.get("agentName")+"</agentName>");
        xml.append("\n  <packageTypeCiq>4M</packageTypeCiq>");
        xml.append("\n  <transportationMethod>8</transportationMethod>");
        xml.append("\n  <shipCode>"+account.get("shipCode")+"</shipCode>");
        xml.append("\n  <tradeCountryCiq>"+account.get("tradeCountryCiq")+"</tradeCountryCiq>");
        xml.append("\n  <goodsName>保健品</goodsName>");
        xml.append("\n  <goodsItem>");
        int k=0;
        List<Map<String, String>> ordergoods =DBsql.getOrderGoodsList("where orderNumber="+item.get("orderNumber"));
        for(int i = 0; i < ordergoods.size(); i++)  
        {  
        	Map<String, String> goodsitem = ordergoods.get(i);  
            //System.out.println(list.get(i)); 
        	k++;
        	xml.append("\n  <item id=\""+k+"\">");
        	
        	//商品对照表
        	String goodsNo = DBsql.getGoodsMatch(goodsitem.get("productNumber"),account.get("shipper"));
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
				
				   return "";
				
			}
			
            xml.append("\n  <itemNo>"+goodsNo+"</itemNo>");
            xml.append("\n  <itemName>"+goodsitem.get("productName")+"</itemName>");
            xml.append("\n  <itemModel>瓶</itemModel>");
            xml.append("\n  <itemQuantity>"+goodsitem.get("orderCount")+"</itemQuantity>");
            xml.append("\n  <itemDescribe>"+goodsitem.get("productName")+"</itemDescribe>");
            xml.append("\n  </item>");
            
        }  
        
        xml.append("\n  </goodsItem>");
        xml.append("\n  <note></note>");
        xml.append("\n  </row>");
        xml.append("\n  </rows>");
        if(!act.equals("view"))
		{
	        new File("D:/www/api/etrade/log/ST/"+batchNumber).mkdirs();
	        System.out.print(xml.toString());
	        try {
				  fwriter = new FileWriter("D:/www/api/etrade/log/ST/"+batchNumber+"/Send"+item.get("orderNumber")+".xml");
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
		}
        return xml.toString();
	}
	
	
}
