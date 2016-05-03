package com.ecmoho.sycm.schq.processor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.sycm.schq.exploration.SchqExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * 市场行情--人群画像--卖家人群
 */
@Component("schqRqhxSellerProcessor")
public class SchqRqhxSellerProcessor extends  SchqProcessor{
	
//	'rqhx-seller-xjfb','rqhx-seller-sffb','rqhx-seller-mjfb'
	
	@Resource(name="schqRqhxSellerProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqRqhxSellerExploration")
	private SchqExploration schqExploration;
	@Value("9,11,12,13,23,26")
	private String accountIdArr;
    //'rqhx-seller-xjfb','rqhx-seller-sffb','rqhx-seller-mjfb'
	@Value("rqhx-seller-xjfb,rqhx-seller-sffb,rqhx-seller-mjfb")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public void run() {
		super.start(schqProcessor, schqExploration,accountIdArr, childAccountArr, days);
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
