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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hnblc.utils.DBsql;
import com.hnblc.utils.EncreptAndDecreptUtil;
import com.hnblc.utils.RSAUtil;
import com.hnblc.utils.XML;

public class OrderClient {

	public static  Map<String, String> account;
	
	public static void main(String[] args) {
		try {
			System.out.println(doSend("1879804667015935", "view"));
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String doSend(String orderNumber,String act) throws Exception {
		
		Map<String, String> item = new HashMap<String, String>();  
		String batchNumber ="";
		 List<Map<String, String>> Order =DBsql.getOrderList("where orderNumber='"+orderNumber+"'  Limit 1");
		   if(Order.size()>0)
		   {

			item = Order.get(0);  
			account = DBsql.getAccount(item.get("shopName"));
		    batchNumber = item.get("batchNumber");
		   }
		   
	  // 准备订单信息
		String dataInfo = getOrderXmlStr(orderNumber,act);
		if(dataInfo.equals(""))
		{
			
			System.out.print("商品无法匹配");
			
			return "商品无法匹配";
		}else{
			
			if(act.equals("view"))
			{
				return dataInfo; //预览报文
				
			}

		}
		return "";
	}


	/**
	 * 生成请求报文数据
	 * @param encryptDataInfo
	 * @return
	 */
	public static String generateXml(String encryptDataInfo, String signature,String IsSend) {
		StringBuffer xmlSb = new StringBuffer();
		xmlSb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlSb.append("<Root>\n");
		xmlSb.append("\t<PubInfo>\n");
		xmlSb.append("\t\t<Version>1.0</Version>\n");
		xmlSb.append("\t\t<CompanyCode>"+account.get("companyCode")+"</CompanyCode>\n");  //companyCode为全局变量
		xmlSb.append("\t\t<Key>"+account.get("publicKey")+"</Key>\n");  //companyKey为全局变量
		xmlSb.append("\t\t<MsgType>O</MsgType>\n");
		xmlSb.append("\t\t<OptType>"+IsSend+"</OptType>\n");
		xmlSb.append("\t\t<Signature>").append(signature).append("</Signature>\n");//该字段为对订单报文加密后字符的签名
//		xmlSb.append("\t\t<CreatTime>2015-04-21 12:12:56</CreatTime>\n");
		xmlSb.append("\t\t<CreatTime>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"</CreatTime>\n");
		xmlSb.append("\t</PubInfo>\n");
		xmlSb.append("\t<DataInfo>").append(encryptDataInfo).append("</DataInfo>\n");//该字段为对订单进行RSA加密之后的密文
		xmlSb.append("</Root>\n");
		System.out.println("接口请求报文:\n"+xmlSb);
		return xmlSb.toString();
	}
	
	

	public static String getOrderXmlStr(String orderNumber,String act) {
		StringBuffer orderXmlSb = new StringBuffer();
		orderXmlSb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		orderXmlSb.append("<Order>\n");
		orderXmlSb.append("<OrderHead>\n");
		orderXmlSb.append("<ecCode>").append(account.get("ecCode"))
				.append("</ecCode>\n");
		orderXmlSb.append("<cbeCode>").append(account.get("cbeCode"))
				.append("</cbeCode>\n");
		orderXmlSb.append("<ecName>").append(account.get("shipper"))
				.append("</ecName>\n");
		orderXmlSb.append("<cbeName>").append(account.get("shipper"))
				.append("</cbeName>\n");
		orderXmlSb.append("<ecpCodeCiq>").append(account.get("ecpCodeCiq"))
				.append("</ecpCodeCiq>\n");
		orderXmlSb.append("<ecpCodeCus>").append(account.get("ecpCodeCus"))
				.append("</ecpCodeCus>\n");
		orderXmlSb.append("<ecpNameCiq>").append(account.get("shipper"))
				.append("</ecpNameCiq>\n");
		orderXmlSb.append("<ecpNameCus>").append(account.get("shipper"))
				.append("</ecpNameCus>\n");
		
		String  batchNumber = "";
		//订单信息
		List<Map<String, String>> Order =DBsql.getOrderList("where orderNumber='"+orderNumber+"' Limit 1");
		   if(Order.size()>0)
		   {
			   Map<String, String> item = Order.get(0);  
			   
			   List<Map<String, String>> ordergoods =DBsql.getOrderGoodsList("where orderNumber='"+item.get("orderNumber")+"'");
			   DecimalFormat df = new DecimalFormat("0.00");
		        //计算商品小计总和 平摊金额
		        Double  totalgoodsprice=0.00;
		        Double  totalgoodspricereal=0.00;
		     
		        //优惠券总额
		        Double  BonusAmount=Double.valueOf(item.get("BonusAmount"));
		        
		        //计算商品总价
		        for(int i = 0; i < ordergoods.size(); i++)  
		        {  
		        	Map<String, String> goodsitem = ordergoods.get(i);  
		        	totalgoodsprice += Double.valueOf(goodsitem.get("price"));//商品小计
		        	
		        	
		        }
		        //优惠平摊
		        String[] priceList=null;
		        Double  perDiscountTotal=0.00;
		        Double  totalgoodvaluesreal=0.00;
		        Double  totalamount=0.00;
		        priceList=new String[20];
		        Double charge =0.00;
		        Double tax=0.00;
		        	for(int i = 0; i < ordergoods.size(); i++)  
		            {  
		        		Map<String, String> goodsitem = ordergoods.get(i);  
		        		
		        		System.out.println("price:"+goodsitem.get("price"));
		        		System.out.println("totalgoodsprice*BonusAmount："+totalgoodsprice*BonusAmount);
		        		Double  perDiscount =Double.valueOf(goodsitem.get("price"))/totalgoodsprice*BonusAmount;
		        		System.out.println(perDiscount);
		        		perDiscount = Double.valueOf(df.format(perDiscount));
		        		perDiscountTotal +=perDiscount;
		        		
//		        		priceList[i]=String.valueOf(df.format(Double.valueOf(goodsitem.get("price"))+perDiscount)); 
		        		totalgoodspricereal +=Double.valueOf(df.format(Double.valueOf(goodsitem.get("price"))+perDiscount));
		        	    if("APPLE（保税）".equalsIgnoreCase(account.get("shopName"))||"易恒健康（海外）".equalsIgnoreCase(account.get("shopName"))){
		        	    	
//		        	    	totalamount +=Double.valueOf(goodsitem.get("discount"));
//		        	    	totalgoodvaluesreal +=Double.valueOf(df.format(Double.valueOf(goodsitem.get("price"))+perDiscount-Double.valueOf(goodsitem.get("TaxFee"))-Double.valueOf(goodsitem.get("discount"))));
//			        		priceList[i]=String.valueOf(df.format(Double.valueOf(goodsitem.get("price"))+perDiscount-Double.valueOf(goodsitem.get("TaxFee"))-Double.valueOf(goodsitem.get("discount")))); 
//			        		charge = totalgoodspricereal;
			        		totalamount +=Double.valueOf(goodsitem.get("discount"));
		        	    	totalgoodvaluesreal +=Double.valueOf(df.format((Double.valueOf(goodsitem.get("price"))+perDiscount)/1.119));
			        		priceList[i]=String.valueOf(df.format((Double.valueOf(goodsitem.get("price"))+perDiscount)/1.119)); 
			        		charge = totalgoodspricereal;
		        	        tax=totalgoodspricereal-totalgoodvaluesreal;
		        	    }else{
		        	    	totalgoodvaluesreal +=Double.valueOf(df.format((Double.valueOf(goodsitem.get("price"))+perDiscount)/1.119));
			        		priceList[i]=String.valueOf(df.format((Double.valueOf(goodsitem.get("price"))+perDiscount)/1.119)); 
			        		charge = totalgoodspricereal+Double.valueOf(item.get("deliveryCost"));
			        		totalamount=perDiscountTotal;
			        		tax=totalgoodspricereal-totalgoodvaluesreal;
		        	    }
		            }
		        	//平摊之后的误差
		         Double perDiscountDiff = perDiscountTotal - BonusAmount;
		        	//补差在最后一个商品上
		         priceList[ordergoods.size()-1] = String.valueOf(Double.valueOf(priceList[ordergoods.size()-1])+perDiscountDiff);  	
		      
			    batchNumber = item.get("batchNumber");

			    orderXmlSb.append("<orderNo>"+item.get("orderNumber")+"</orderNo>\n");
			   //货值+运费+其它费用+进口行邮税=总费用，必填
				orderXmlSb.append("<charge>"+df.format(charge)+"</charge>\n");
				orderXmlSb.append("<goodsValue>"+df.format(totalgoodvaluesreal)+"</goodsValue>\n");
				orderXmlSb.append("<freight></freight>\n");//"+df.format(Double.valueOf(item.get("deliveryCost")))+"
				orderXmlSb.append("<other>"+(totalamount==0.00?"":df.format(totalamount))+"</other>\n");
				orderXmlSb.append("<tax>"+(tax==0.00?"":df.format(tax))+"</tax>\n");
				orderXmlSb.append("<customer>"+item.get("cosignee")+"</customer>\n");
			 
				orderXmlSb.append("<shipper>"+account.get("shipper")+"</shipper>\n");
				orderXmlSb.append("<shipperAddress>"+account.get("shipperAddress")+"</shipperAddress>\n");
				orderXmlSb.append("<shipperTelephone>"+account.get("shipperTelephone")+"</shipperTelephone>\n");
				orderXmlSb.append("<shipperCountryCiq>"+account.get("shipperCountryCiq")+"</shipperCountryCiq>\n");
				orderXmlSb.append("<shipperCountryCus>"+account.get("shipperCountryCus")+"</shipperCountryCus>\n");
				
				orderXmlSb.append("<consignee>"+item.get("cosignee")+"</consignee>\n");
				orderXmlSb.append("<consigneeProvince>"+item.get("province")+"</consigneeProvince>\n");
				orderXmlSb.append("<consigneeCity>"+item.get("city")+"</consigneeCity>\n");
				String area =item.get("area");
				if(area.equals(""))
				{
					area=item.get("city");
				}
				orderXmlSb.append("<consigneeZone>"+area+"</consigneeZone>\n");
				
//				String consigneeAddress = item.get("address").replace(item.get("province"), "");
//				
//				consigneeAddress = consigneeAddress.replace(item.get("city"), "");
//				
//				consigneeAddress = consigneeAddress.replace(area, "");
				
				orderXmlSb.append("<consigneeAddress>"+item.get("address")+"</consigneeAddress>\n");
				
				String consigneeTelephone =item.get("mobilePhone").equals("") ? item.get("telephone") : item.get("mobilePhone");

				orderXmlSb.append("<consigneeTelephone>"+consigneeTelephone+"</consigneeTelephone>\n");
				
				orderXmlSb.append("<consigneeCountryCiq>"+account.get("consigneeCountryCiq")+"</consigneeCountryCiq>\n");
				orderXmlSb.append("<consigneeCountryCus>"+account.get("consigneeCountryCus")+"</consigneeCountryCus>\n");
				
				orderXmlSb.append("<idType>1</idType>\n");
				orderXmlSb.append("<idNumber>"+item.get("idCardName")+"</idNumber>\n");
				orderXmlSb.append("<ieType>"+account.get("ieType")+"</ieType>\n");
				orderXmlSb.append("<stockFlag>"+account.get("stockFlag")+"</stockFlag>\n");
				orderXmlSb.append("<batchNumbers>"+batchNumber+"</batchNumbers>\n");
				orderXmlSb.append("<totalLogisticsNo></totalLogisticsNo>\n");
				
				orderXmlSb.append("<tradeCountryCiq>"+account.get("tradeCountryCiq")+"</tradeCountryCiq>\n");
				orderXmlSb.append("<tradeCountryCus>"+account.get("tradeCountryCus")+"</tradeCountryCus>\n");
				orderXmlSb.append("<agentCodeCiq>"+account.get("agentCodeCiq")+"</agentCodeCiq>\n");
				orderXmlSb.append("<agentCodeCus>"+account.get("agentCodeCus")+"</agentCodeCus>\n");
				orderXmlSb.append("<agentNameCiq>"+account.get("agentNameCiq")+"</agentNameCiq>\n");
				orderXmlSb.append("<agentNameCus>"+account.get("agentNameCus")+"</agentNameCus>\n");
				orderXmlSb.append("<packageTypeCiq>"+account.get("packageTypeCiq")+"</packageTypeCiq>\n");
				orderXmlSb.append("<packageTypeCus>"+account.get("packageTypeCus")+"</packageTypeCus>\n");
				
				orderXmlSb.append("<modifyMark>1</modifyMark>\n");
				orderXmlSb.append("<note></note>\n");
				orderXmlSb.append("</OrderHead>\n");
				
				
				
				//商品信息部分
				int quantity=0;
				for(int i = 0; i < ordergoods.size(); i++)  
		        {  
					orderXmlSb.append("<OrderList>\n");
		        	Map<String, String> goodsitem = ordergoods.get(i); 
		        	
		        	Double price = Double.valueOf(priceList[i])/Double.valueOf(goodsitem.get("orderCount"));
		        	quantity = quantity+Integer.parseInt(goodsitem.get("orderCount"));
		        	orderXmlSb.append("<itemNoCiq></itemNoCiq>\n");
					orderXmlSb.append("<itemNoCus></itemNoCus>\n");
					
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
					
					orderXmlSb.append("<goodsNo>"+goodsNo+"</goodsNo>\n");
					
					String shelfGoodsName = goodsitem.get("productName");//.replace("/", " ")
					
					orderXmlSb.append("<shelfGoodsName>"+shelfGoodsName+"</shelfGoodsName>\n");
					
					orderXmlSb.append("<goodsName>"+shelfGoodsName+"</goodsName>\n");
					
					orderXmlSb.append("<describe></describe>\n");
					orderXmlSb.append("<codeTs></codeTs>\n");
					orderXmlSb.append("<ciqCode></ciqCode>\n");
					orderXmlSb.append("<goodsModel></goodsModel>\n");
					orderXmlSb.append("<taxCode></taxCode>\n");
					
					//如果有赠品则 price=0
					Double giftCount = Double.valueOf(goodsitem.get("giftCount"));
					if(giftCount==0)
					{
						orderXmlSb.append("<price>"+df.format(price)+"</price>\n");
					}else{
						orderXmlSb.append("<price></price>\n");
					}
					
					
					orderXmlSb.append("<currencyCiq>"+account.get("currencyCiq")+"</currencyCiq>\n");
					orderXmlSb.append("<currencyCus>"+account.get("currencyCus")+"</currencyCus>\n");
					
					//正常商品数量
					orderXmlSb.append("<quantity>"+goodsitem.get("orderCount")+"</quantity>\n");
					
					orderXmlSb.append("<priceTotal>"+df.format(Double.valueOf(priceList[i]))+"</priceTotal>\n");
					
					orderXmlSb.append("<unitCiq>"+account.get("unitCiq")+"</unitCiq>\n");
					orderXmlSb.append("<unitCus>"+account.get("unitCus")+"</unitCus>\n");
					
					//商品折扣 不需要填
					orderXmlSb.append("<discount></discount>\n");
					
					//含有赠品 Y 不含有赠品N
					if(giftCount==0)
					{
						orderXmlSb.append("<giftsFlag>N</giftsFlag>\n");
					}else{
						orderXmlSb.append("<giftsFlag>Y</giftsFlag>\n");
					}
					
					
					orderXmlSb.append("<originCountryCiq>"+account.get("originCountryCiq")+"</originCountryCiq>\n");
					orderXmlSb.append("<originCountryCus>"+account.get("originCountryCus")+"</originCountryCus>\n");
					
					orderXmlSb.append("<usage></usage>\n");
					orderXmlSb.append("<wasteMaterials>1</wasteMaterials>\n");
					orderXmlSb.append("<wrapTypeCiq>4M</wrapTypeCiq>\n");
					orderXmlSb.append("<wrapTypeCus>2</wrapTypeCus>\n");
					orderXmlSb.append("<packNum>1</packNum>\n");
					orderXmlSb.append("</OrderList>\n");
		        }
				

				
				
				orderXmlSb.append("<OrderPaymentLogistics>\n");
				orderXmlSb.append("<paymentCode>"+item.get("pay_account")+"</paymentCode>\n");//P0001支付宝
				switch (item.get("pay_account")) {
					case "P0001":
						orderXmlSb.append("<paymentName>支付宝（中国）网络技术有限公司</paymentName>\n");
						break;
					case "P0015":
						orderXmlSb.append("<paymentName>财付通支付科技有限公司</paymentName>\n");
						break;
					default:
						orderXmlSb.append("<paymentName></paymentName>\n");
						break;
				}

				orderXmlSb.append("<paymentType></paymentType>\n");
				orderXmlSb.append("<paymentNo>"+item.get("pay_id")+"</paymentNo>\n");
				
				
				orderXmlSb.append("<logisticsCodeCiq>"+account.get("logisticsCodeCiq")+"</logisticsCodeCiq>\n");
				orderXmlSb.append("<logisticsCodeCus>"+account.get("logisticsCodeCus")+"</logisticsCodeCus>\n");
				orderXmlSb.append("<logisticsNameCiq>"+account.get("logisticsNameCiq")+"</logisticsNameCiq>	\n");
				orderXmlSb.append("<logisticsNameCus>"+account.get("logisticsNameCus")+"</logisticsNameCus>\n");
				
				//运单号
				orderXmlSb.append("<totalLogisticsNo>35656458412</totalLogisticsNo>\n");
				orderXmlSb.append("<subLogisticsNo></subLogisticsNo>\n");
				orderXmlSb.append("<logisticsNo></logisticsNo>\n");
				orderXmlSb.append("<trackNo></trackNo>\n");
				orderXmlSb.append("<trackStatus></trackStatus>\n");
				orderXmlSb.append("<crossFreight></crossFreight>\n");
				orderXmlSb.append("<supportValue></supportValue>\n");
				//预估重量
				orderXmlSb.append("<weight>1</weight>\n");
				orderXmlSb.append("<netWeight></netWeight>\n");
				orderXmlSb.append("<quantity>"+quantity+"</quantity>\n");
				orderXmlSb.append("<deliveryWay></deliveryWay>\n");
				
				orderXmlSb.append("<transportationWay>"+account.get("transportationWay")+"</transportationWay>\n");
				orderXmlSb.append("<shipCode>"+account.get("shipCode")+"</shipCode>\n");
				orderXmlSb.append("<shipName>"+account.get("shipName")+"</shipName>\n");
				
				orderXmlSb.append("<destinationPort></destinationPort>\n");
				orderXmlSb.append("</OrderPaymentLogistics>\n");
				orderXmlSb.append("</Order>\n");
		   }
		   
		   if(!act.equals("view"))
			{
			   FileWriter fwriter = null;
			   //System.out.print(orderXmlSb.toString());
			   new File("D:/www/api/etrade/log/CUS/"+batchNumber).mkdirs();
		        
		        try {
					  fwriter = new FileWriter("D:/www/api/etrade/log/CUS/"+batchNumber+"/Send"+orderNumber+".xml");
					  fwriter.write(orderXmlSb.toString());
					  
					  fwriter = new FileWriter("D:/www/api/etrade/log/CUS/"+batchNumber+"/Send2"+orderNumber+".xml");
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
		   
		return orderXmlSb.toString();
	}

	
	
	
	public static String sendHttpPOSTRequest(String httpUrl,
			HashMap<String, String> params,String orderNumber, String batchNumber, String shopName) {
		String result = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();// 创建httpClient对象
		try {
			HttpPost httpPost = new HttpPost(httpUrl);
			List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			if (null != params) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					String value = params.get(key);
					httpParams.add(new BasicNameValuePair(key, value));
				}
				UrlEncodedFormEntity FormEntity = new UrlEncodedFormEntity(
						httpParams, "UTF-8");
				httpPost.setEntity(FormEntity);
			}

			HttpResponse response = httpClient.execute(httpPost);// 获得response对象
			int resStatu = response.getStatusLine().getStatusCode();// 返回码
			
			if (resStatu == HttpStatus.SC_OK) {// 200正常 其它就不合错误
				System.out.println("200");
				HttpEntity entity = response.getEntity(); // 获得响应实体
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");// 获得http相应body
					System.out.print(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		   FileWriter fwriter = null;
		   
		   new File("D:/www/api/etrade/log/CUS/"+batchNumber).mkdirs();
		   System.out.print(result.toString());
	        try {
				  fwriter = new FileWriter("D:/www/api/etrade/log/CUS/"+batchNumber+"/Return"+orderNumber+".xml");
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
				 String Status = XML.parseXml("Root","Status","CUS/"+batchNumber+"/Return"+orderNumber+".xml");	 
				 String Detail = XML.parseXml("Root","Detail","CUS/"+batchNumber+"/Return"+orderNumber+".xml");	 
				 
				 if(!Status.equals("0") || Status.equals("2") || Status.equals("4") || Status.equals("5"))
				 {
					 //状态报错
					 Map<String, String> update= new HashMap<String,String>();
					 update.put("customsTransStatus","2");
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
					 Error.put("type","海关报错");
					 Error.put("errordesc",Detail);
					 Error.put("creatTime",creatTime);
					 Error.put("shopName",shopName);
					 Error.put("readTime", "00-00-00 00:00:00");
					 Error.put("reSendTime", "00-00-00 00:00:00");
					 Error.put("toUser", account.get("responsible"));
					 Error.put("orderNumber", orderNumber);
					 try {
						DBsql.addError(Error);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }else{
					 Map<String, String> update= new HashMap<String,String>();
					 update.put("customsTransStatus","1");
					 update.put("IsSend","2");//更新为已推送
					 try {
						DBsql.updateOrder(update,orderNumber);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
		 
				 
				 
		return result;
	}
}
