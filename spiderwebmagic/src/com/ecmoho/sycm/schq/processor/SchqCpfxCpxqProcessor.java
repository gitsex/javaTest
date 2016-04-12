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
import com.ecmoho.sycm.schq.exploration.SchqCpfxCpxqExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * 市场行情——产品分析——产品详情
 */
@Component("schqCpfxCpxqProcessor")
public class SchqCpfxCpxqProcessor extends  SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqCpfxCpxqExploration")
	private SchqCpfxCpxqExploration schqCpfxCpxqExploration;
//	@Resource(name="schqPpfxPpphProcessor")
//    private SchqPpfxPpphProcessor schqPpfxPpphProcessor;
	public  void start(SchqCpfxCpxqProcessor schqCpfxCpxqProcessor){
		 //获取店铺列表
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(id.equals("11")){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 //获取品牌分析URL列表
//				 "'cpfx-cpxq-cpgk','cpfx-cpxq-zfjg','cpfx-cpxq-zyfb','cpfx-cpxq-nlfb'"
				 List<HashMap<String,String>> CpfxCpxqUrlList=schqCpfxCpxqExploration.getCpfxCpxqUrlList(account,"'cpfx-cpxq-cpgk','cpfx-cpxq-zfjg','cpfx-cpxq-zyfb','cpfx-cpxq-nlfb'");
				 System.out.println("CpfxCpxqUrlList.size()："+CpfxCpxqUrlList.size());
			     for(int j=0;j<CpfxCpxqUrlList.size();j++){
			    	 Map<String,String> map=CpfxCpxqUrlList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
//			    	 System.out.println(map.get("targetUrl"));
			    	 Spider.create(schqCpfxCpxqProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
		 }
	}
	
	public static void main(String[] args) {
		 ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext.xml"); 
		 SchqCpfxCpxqProcessor schqCpfxCpphProcessor = (SchqCpfxCpxqProcessor)ac.getBean("schqCpfxCpxqProcessor"); 
		 schqCpfxCpphProcessor.start(schqCpfxCpphProcessor);
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
	   JSONArray dataJsonArray=null;
	   JSONObject dataJsonObject=null;
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   String childAccount=urlMap.get("childAccount");
	   switch (childAccount) {
	       //产品分析-产品详情--产品概况
	       case "cpfx-cpxq-cpgk":
	    	       dataJsonObject=finalTargetJsonObject.getJSONObject("content").getJSONObject("data");
	    	       dataMap=getDataMap(dataMap, urlMap);
	    	       for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	       }
	    	       schqDbcom.add(dataMap, "product_detail_overview");
		           break;
		   //产品分析--产品详情--产品支付价格构成        
	       case "cpfx-cpxq-zfjg":
	    	       dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("priceBand",StringUtil.isNullString(dataJsonObject.getString("priceBand")));
	    	    	   dataMap.put("payItemCnt",StringUtil.isNullString(dataJsonObject.getString("payItemCnt")));
	    	    	   dataMap.put("payItemQty",StringUtil.isNullString(dataJsonObject.getString("payItemQty")));
	    	    	   dataList.add(dataMap);
			       }
			      schqDbcom.addList(dataList, "product_detail_payamtstructure");  
		          break;
		   //产品分析--产品详情--职业分布        
	       case "cpfx-cpxq-zyfb":
	    	      dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	      for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("jobPer",StringUtil.isNullString(dataJsonObject.getString("jobPer")));
	    	    	   dataMap.put("jobName",StringUtil.isNullString(dataJsonObject.getString("jobName")));
	    	    	   dataList.add(dataMap);
			      }
	    	      schqDbcom.addList(dataList, "product_detail_occupational");  
		          break;
		   //产品分析--产品详情--年龄分布        
	       case "cpfx-cpxq-nlfb":
	    	       dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	       
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("ageBandPer",StringUtil.isNullString(dataJsonObject.getString("ageBandPer")));
	    	    	   dataMap.put("ageBand",StringUtil.isNullString(dataJsonObject.getString("ageBand")));
	    	    	   dataList.add(dataMap);
			       }
	    	       schqDbcom.addList(dataList, "product_detail_paybuyerspercent");  
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
