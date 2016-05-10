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
 * 市场行情——品牌分析——品牌排行
 */
@Component("schqPpfxPpphProcessor")
public class SchqPpfxPpphProcessor extends  SchqProcessor{
	

	@Resource(name="schqPpfxPpphProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqPpfxPpphExploration")
	private SchqExploration schqExploration;
	@Value("9,11,12,13,23,26")
	private String accountIdArr;
    //"'ppfx-ppph-rxpp','ppfx-ppph-bspp','ppfx-ppph-gllpp','ppfx-ppph-gsspp'"
	@Value("ppfx-ppph-rxpp,ppfx-ppph-bspp,ppfx-ppph-gllpp,ppfx-ppph-gsspp")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public void run() {
		super.start(schqProcessor, schqExploration, accountIdArr,childAccountArr, days);
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
	   //品牌分析——品牌排行——热销品牌排行
	   if("ppfx-ppph-rxpp".equalsIgnoreCase(urlMap.get("childAccount"))){
			   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
			   for(int i=0;i<jsonArray.size();i++){
				   dataMap=getDataMap(dataMap,urlMap);
				   
				   dataJsonObject=jsonArray.getJSONObject(i);
				   for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	       }
				   dataList.add(dataMap);
			       }
			      schqDbcom.addList(dataList, "brand_hot_ranking");  
		  //品牌分析——品牌排行——飙升品牌排行	      
          }else if("ppfx-ppph-bspp".equalsIgnoreCase(urlMap.get("childAccount"))){
        	   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
			   for(int i=0;i<jsonArray.size();i++){
				   dataMap=getDataMap(dataMap,urlMap);
				   dataJsonObject=jsonArray.getJSONObject(i);
				   for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	       }
				   dataList.add(dataMap);
			    }
			      schqDbcom.addList(dataList, "brand_soaring_ranking");  
		   //品牌分析——品牌排行——高流量品牌排行	      
          }else if("ppfx-ppph-gllpp".equalsIgnoreCase(urlMap.get("childAccount"))){
        	   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
			   for(int i=0;i<jsonArray.size();i++){
				   dataMap=getDataMap(dataMap,urlMap);
				   
				   dataJsonObject=jsonArray.getJSONObject(i);
				   for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	       }
				   dataList.add(dataMap);
			       }
			      schqDbcom.addList(dataList, "brand_flow_ranking");  
		   //品牌分析——品牌排行——高搜索品牌排行	      
          }else if("ppfx-ppph-gsspp".equalsIgnoreCase(urlMap.get("childAccount"))){
        	   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
			   for(int i=0;i<jsonArray.size();i++){
				   dataMap=getDataMap(dataMap,urlMap);
				   
				   dataJsonObject=jsonArray.getJSONObject(i);
				   for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	       }
				   dataList.add(dataMap);
			       }
			      schqDbcom.addList(dataList, "brand_serach_ranking");  
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
		   dataMap.put("device", urlMap.get("device"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
    }
}
