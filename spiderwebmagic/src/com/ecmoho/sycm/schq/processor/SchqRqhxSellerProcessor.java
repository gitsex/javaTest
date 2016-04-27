package com.ecmoho.sycm.schq.processor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;



import com.ecmoho.sycm.schq.exploration.SchqRqhxSellerExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * 市场行情--人群画像--卖家人群
 */
@Component("schqRqhxSellerProcessor")
public class SchqRqhxSellerProcessor extends  SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqRqhxSellerExploration")
	private SchqRqhxSellerExploration schqRqhxSellerExploration;
//	@Resource(name="schqRqhxBuyerProcessor")
//    private SchqRqhxBuyerProcessor schqRqhxBuyerProcessor;
	public  void start(SchqRqhxSellerProcessor schqRqhxSellerProcessor){
		
		
		 //获取店铺列表
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(id.equals("11")){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 //星级分布，省份分布，卖家分布
				 //'rqhx-seller-xjfb','rqhx-seller-sffb','rqhx-seller-mjfb'
				 List<HashMap<String,String>> urlHyzbList=schqRqhxSellerExploration.getRqhxSellerUrlList(account,"'rqhx-seller-mjfb'");
				 System.out.println(urlHyzbList.size());
			     for(int j=0;j<urlHyzbList.size();j++){
			    	 Map<String,String> map=urlHyzbList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
//			    	 System.out.println(map.get("targetUrl"));
			    	 Spider.create(schqRqhxSellerProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
		 }
	}
	public static void main(String[] args) {
		ApplicationContext ac =new ClassPathXmlApplicationContext("conf/applicationContext.xml");
		SchqRqhxSellerProcessor schqRqhxSellerProcessor=(SchqRqhxSellerProcessor) ac.getBean("schqRqhxSellerProcessor");
		schqRqhxSellerProcessor.start(schqRqhxSellerProcessor);
	}
	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
           
	   String jsonStr=page.getJson().toString();
       System.out.println(jsonStr);
       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
       List<Map<String,String>> dataList= new ArrayList<Map<String,String>>();
	   Map<String,String> dataMap=null;
	   JSONArray jsonArray=null;
	   JSONObject dataJsonObject=null;
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("list");
	   for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
		   dataJsonObject=jsonArray.getJSONObject(i);
		   dataMap=getDataMap(dataMap, urlMap);
		   for(String key:dataJsonObject.keySet()){
			   dataMap.put(key, dataJsonObject.getString(key));
			   dataMap.put(key, dataJsonObject.getString(key));
		   }
	       dataList.add(dataMap);
	   }
	 //'rqhx-seller-xjfb','rqhx-seller-sffb','rqhx-seller-mjfb'
	   switch (urlMap.get("childAccount")) {
		   case "rqhx-seller-xjfb"://人群画像_卖家人群_星级分布
			   schqDbcom.addList(dataList, "crowdportrait_sellerportrait_sellerleveldistribute");
			   break;
		   case "rqhx-seller-sffb"://人群画像_卖家人群_省份分布
			  
			   schqDbcom.addList(dataList, "crowdportrait_sellerportrait_sellerprovincepercent");
			   break;
		   case "rqhx-seller-mjfb"://人群画像_卖家人群_卖家分布
			   schqDbcom.addList(dataList, "crowdportrait_sellerportrait_sellerpercent");	  
			   break;
		   default:
			  break;
		}
	}
    
    public static Map<String,String> getDataMap(Map<String,String> dataMap,Map<String,String> urlMap){
 	   dataMap=new HashMap<String,String>();
		   dataMap.put("accountid",urlMap.get("accountid"));
		   dataMap.put("create_at", urlMap.get("create_at"));
		   dataMap.put("level", urlMap.get("level"));
		   dataMap.put("item1", urlMap.get("item1"));
		   dataMap.put("item2", urlMap.get("item2"));
		   dataMap.put("item3", urlMap.get("item3"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
 }
}
