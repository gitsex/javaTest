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
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;
import com.ecmoho.sycm.schq.exploration.SchqPpfxPpphExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * 市场行情——品牌分析——品牌排行
 */
@Component("schqPpfxPpphProcessor")
public class SchqPpfxPpphProcessor extends  SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqPpfxPpphExploration")
	private SchqPpfxPpphExploration schqPpfxPpphExploration;
//	@Resource(name="schqPpfxPpphProcessor")
//    private SchqPpfxPpphProcessor schqPpfxPpphProcessor;
	public  void start(SchqPpfxPpphProcessor schqPpfxPpphProcessor){
		 //获取店铺列表
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(id.equals("9")){
				 
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 //获取品牌分析URL列表
//				 "'ppfx-ppph-rxpp','ppfx-ppph-bspp','ppfx-ppph-gllpp','ppfx-ppph-gsspp'"
				 List<HashMap<String,String>> ppphUrlList=schqPpfxPpphExploration.getPpfxPpphUrlList(account,"'ppfx-ppph-rxpp','ppfx-ppph-bspp','ppfx-ppph-gllpp','ppfx-ppph-gsspp'");
				 System.out.println("ppphUrlList.size()："+ppphUrlList.size());
			     for(int j=0;j<ppphUrlList.size();j++){
			    	 Map<String,String> map=ppphUrlList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
//			    	 System.out.println(map.get("targetUrl"));
			    	 Spider.create(schqPpfxPpphProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
		 }
	}
	
	public static void main(String[] args) {
		 ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext.xml"); 
		 SchqPpfxPpphProcessor schqPpfxPpphProcessor = (SchqPpfxPpphProcessor)ac.getBean("schqPpfxPpphProcessor"); 
		 schqPpfxPpphProcessor.start(schqPpfxPpphProcessor);
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
