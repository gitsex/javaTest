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
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.sycm.schq.exploration.SchqExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * 市场行情——品牌分析——品牌详情
 */
@Component("schqPpfxPpxqProcessor")
public class SchqPpfxPpxqProcessor extends  SchqProcessor{
	

	
	@Resource(name="schqPpfxPpxqProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqPpfxPpxqExploration")
	private SchqExploration schqExploration;
	@Value("9,11,12,13,23,26")
	private String accountIdArr;
//	"'ppfx-ppph-rxpp','ppfx-ppph-bspp','ppfx-ppph-gllpp','ppfx-ppph-gsspp'"
	@Value("ppfx-ppph-rxpp,ppfx-ppph-bspp,ppfx-ppph-gllpp,ppfx-ppph-gsspp")
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
	   JSONArray dataJsonArray=null;
	   JSONObject dataJsonObject=null;
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   String childAccount=urlMap.get("childAccount");
	   switch (childAccount) {
	       //品牌分析-品牌详情--品牌概况
	       case "ppfx-ppxq-ppgk":
	    	       dataJsonObject=finalTargetJsonObject.getJSONObject("content").getJSONObject("data");
	    	       dataMap=getDataMap(dataMap, urlMap);
	    	       for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	    	   schqDbcom.add(dataMap, "brand_detail_overview");
	    	       }
		           break;
		   //品牌分析--品牌详情--支付价格        
	       case "ppfx-ppxq-zfjg":
	    	       dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("priceBand",StringUtil.isNullString(dataJsonObject.getString("priceBand")));
	    	    	   dataMap.put("payItemCnt",StringUtil.isNullString(dataJsonObject.getString("payItemCnt")));
	    	    	   dataMap.put("payItemQty",StringUtil.isNullString(dataJsonObject.getString("payItemQty")));
	    	    	   dataList.add(dataMap);
			      }
			      schqDbcom.addList(dataList, "brand_detail_payamtstructure");  
		          break;
		   //品牌分析--品牌详情--职业分布        
	       case "ppfx-ppxq-zyfb":
	    	      dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	      for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("jobPer",StringUtil.isNullString(dataJsonObject.getString("jobPer")));
	    	    	   dataMap.put("jobName",StringUtil.isNullString(dataJsonObject.getString("jobName")));
	    	    	   dataList.add(dataMap);
			      }
	    	      schqDbcom.addList(dataList, "brand_detail_occupational");  
		          break;
		   //品牌分析--品牌详情--年龄分布        
	       case "ppfx-ppxq-nlfb":
	    	       dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	    	       
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("ageBandPer",StringUtil.isNullString(dataJsonObject.getString("ageBandPer")));
	    	    	   dataMap.put("ageBand",StringUtil.isNullString(dataJsonObject.getString("ageBand")));
	    	    	   dataList.add(dataMap);
			       }
	    	       schqDbcom.addList(dataList, "brand_detail_paybuyerspercent");  
		           break;
		   //品牌分析--品牌详情--近90天支付金额        
	       case "ppfx-ppxq-nzf":
		    	   dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("list");
	    	       
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataMap.put("proportion",StringUtil.isNullString(dataJsonObject.getString("proportion")));
	    	    	   dataMap.put("name",StringUtil.isNullString(dataJsonObject.getString("name")));
	    	    	   dataList.add(dataMap);
			       }
	    	       schqDbcom.addList(dataList, "brand_detail_ninetydaysamtpercent");  
		           break;
		   //品牌分析--品牌详情--近90天购买次数        
	       case "ppfx-ppxq-ngm":
		    	   dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("list");
	    	       
	    	       for(int i=0;i<dataJsonArray.size();i++){
	    	    	   dataMap=getDataMap(dataMap, urlMap);
	    	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	    	   dataMap.put("proportion",StringUtil.isNullString(dataJsonObject.getString("proportion")));
	    	    	   dataMap.put("name",StringUtil.isNullString(dataJsonObject.getString("name")));
	    	    	   dataList.add(dataMap);
			       }
	    	       schqDbcom.addList(dataList, "brand_detail_ninetydaysbuycntpercent");  
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
		   dataMap.put("device", urlMap.get("device"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
    }
}
