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
 * 市场行情--商品店铺榜--产品粒度
 */
@Component("schqSpdpbCpldProcessor")
public class SchqSpdpbCpldProcessor extends  SchqProcessor{
	
	@Resource(name="schqSpdpbCpldProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqSpdpbCpldExploration")
	private SchqExploration schqExploration;
	@Value("9,11,12,13,23,26")
	private String accountIdArr;
//	 'spdpb-cpld-rxsp','spdpb-cpld-llsp'
	@Value("spdpb-cpld-rxsp,spdpb-cpld-llsp")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public void run() {
		super.start(schqProcessor, schqExploration,accountIdArr ,childAccountArr, days);
	}
	
	@Override
	public Site getSite() {
		return super.getSite().setSleepTime(10000);
	}
    @Override
	public void process(Page page) {
	 
    	   String jsonStr=page.getJson().toString();
           System.out.println(jsonStr);
	       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
	       List<Map<String,String>> dataList= new ArrayList<Map<String,String>>();
		   Map<String,String> dataMap=null;
		   JSONArray dataJsonArray=null;
		   JSONObject dataJsonObject=null;
		   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
		   String childAccount=urlMap.get("childAccount");
		   dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
	       for(int i=0;i<dataJsonArray.size();i++){
	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	   dataMap=getDataMap(dataMap, urlMap);
	    	   for(String key:dataJsonObject.keySet()){
		    	   dataMap.put(key, dataJsonObject.getString(key));
		       }
	    	   dataList.add(dataMap);
	       }
	       
		   switch (childAccount) {
		       //商品店铺榜_产品粒度_热销商品榜
		       case "spdpb-cpld-rxsp":
		    	       schqDbcom.addList(dataList, "commodityshop_product_hotcommodityranking");
			           break;
			   //商品店铺帮_产品粒度_流量商品榜 
		       case "spdpb-cpld-llsp":
				      schqDbcom.addList(dataList, "commodityshop_product_flowcommodityranking");  
			          break;
		       default:
			           break;
	        }
 
	}
    //获取数据附加信息
    public static Map<String,String> getDataMap(Map<String,String> dataMap,Map<String,String> urlMap){
 	       dataMap=new HashMap<String,String>();

		   dataMap.put("accountid",urlMap.get("accountid"));
		   dataMap.put("create_at", urlMap.get("create_at"));
		   dataMap.put("level", urlMap.get("level"));
		   dataMap.put("item1", urlMap.get("item1"));
		   dataMap.put("item2", urlMap.get("item2"));
		   dataMap.put("item3", urlMap.get("item3"));
		   dataMap.put("brandId", urlMap.get("brandId"));
		   dataMap.put("brandName", urlMap.get("brandName"));
		   dataMap.put("modelId", urlMap.get("modelId"));
		   dataMap.put("modelName", urlMap.get("modelName"));
		   dataMap.put("spuId", urlMap.get("spuId"));
		   dataMap.put("device", urlMap.get("device"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
     }
}
