package com.ecmoho.sycm.schq.processor;



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
import com.ecmoho.sycm.schq.exploration.SchqHyzbExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * 行业直播schqhyzbprocessor
 */
@Component("schqHyzbProcessor")
public class SchqHyzbProcessor extends SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqHyzbExploration")
	private SchqHyzbExploration schqHyzbExploration;
	@Resource(name="schqHyzbProcessor")
	private SchqHyzbProcessor schqHyzbProcessor;
	public  void start(){
		
		 //获取店铺列表
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(id.equals("11")){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 List<HashMap<String,String>> urlHyzbList=schqHyzbExploration.getHyzbUrlList(account,"hyzb-dpss");
			     for(int j=0;j<urlHyzbList.size();j++){
			    	 Map<String,String> map=urlHyzbList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
//			    	 System.out.println(map.get("targetUrl"));
			    	 Spider.create(schqHyzbProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
		 }
		
	}
	
	public static void main(String[] args) {
//		 ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml"); 
//		 SchqHyzbProcessor schqHyzbProcessor = (SchqHyzbProcessor)ac.getBean("schqHyzbProcessor"); 
//		 schqHyzbProcessor.start(schqHyzbProcessor);
	}
	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
           
	   String jsonStr=page.getJson().toString();
       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
	   JSONObject jsonObject=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONObject("data");
	   JSONArray payAmtList=jsonObject.getJSONArray("payAmtList");
	   JSONArray paySubOrderCntList=jsonObject.getJSONArray("paySubOrderCntList");
	   String updateTime=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getString("updateTime");
	   Map<String,String> dataMap=new HashMap<String,String>();
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   dataMap.put("accountid",urlMap.get("accountid"));
	   dataMap.put("create_at", urlMap.get("create_at"));
	   dataMap.put("level", urlMap.get("level"));
	   dataMap.put("item1", urlMap.get("item1"));
	   dataMap.put("item2", urlMap.get("item2"));
	   dataMap.put("item3", urlMap.get("item3"));
	   dataMap.put("device", urlMap.get("device"));
	   dataMap.put("timeslotType", urlMap.get("timeslotType"));
	   dataMap.put("updateTime", updateTime);
	   dataMap.put("log_at", urlMap.get("log_at"));
	   for(int a=0;a<payAmtList.size();a++){
		   dataMap.put("timeslot"+a, payAmtList.getString(a));
	   }
	   schqDbcom.add(dataMap, "industry_payamtlist");
	   dataMap.remove("payamt");
	   for(int b=0;b<paySubOrderCntList.size();b++){
		   dataMap.put("timeslot"+b, paySubOrderCntList.getString(b));
	   }
	   schqDbcom.add(dataMap, "industry_paysubordercntlist");
    	 
    	
	}
}
