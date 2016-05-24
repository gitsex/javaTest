package com.ecmoho.sycm.schq.processor;



import java.util.HashMap;
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
 * ��ҵֱ��_����ʵʱ����schqhyzbprocessor
 */
@Component("schqHyzbProcessor")
public class SchqHyzbProcessor extends SchqProcessor{
	
	@Resource(name="schqHyzbProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqHyzbExploration")
	private SchqExploration schqExploration;
	@Value("11")
	private String accountIdArr;
	@Value("hyzb-dpss")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public void run() {
		super.start(schqProcessor, schqExploration, accountIdArr,childAccountArr, days);
	}
	//�ֶ�����ץȡʧ�ܵ�url����
	public void manual(String processorKey,String clientIp){
		System.out.println(processorKey+":"+clientIp);
	     super.manualStart(schqProcessor, processorKey,clientIp);
	}
	
	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
       
		     String jsonStr=page.getJson().toString();
		     System.out.println("hyzb��"+jsonStr);
		     Map<String,String> urlMap=schqHeaderBean.getUrlMap();
		     Map<String,String> dataMap=new HashMap<String,String>();
		     dataMap.put("accountid",urlMap.get("accountid"));
			 dataMap.put("create_at", urlMap.get("create_at"));
			 dataMap.put("level", urlMap.get("level"));
		   	 dataMap.put("item1", urlMap.get("item1"));
		     dataMap.put("item2", urlMap.get("item2"));
			 dataMap.put("item3", urlMap.get("item3"));
			 dataMap.put("device", urlMap.get("device"));
			 dataMap.put("timeslotType", urlMap.get("timeslotType"));
			 dataMap.put("log_at", urlMap.get("log_at"));
			 if("success".equalsIgnoreCase(isRequestSuccess(jsonStr))){
			       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
				   JSONObject jsonObject=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONObject("data");
				   JSONArray payAmtList=jsonObject.getJSONArray("payAmtList");
				   JSONArray paySubOrderCntList=jsonObject.getJSONArray("paySubOrderCntList");
				   String updateTime=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getString("updateTime");
				   dataMap.put("updateTime", updateTime);
				   
				   for(int a=0;a<payAmtList.size();a++){
					   dataMap.put("timeslot"+a, payAmtList.getString(a));
				   }
				   schqDbcom.add(dataMap, "industry_payamtlist");
				   dataMap.remove("payamt");
				   for(int b=0;b<paySubOrderCntList.size();b++){
					   dataMap.put("timeslot"+b, paySubOrderCntList.getString(b));
				   }
				   schqDbcom.add(dataMap, "industry_paysubordercntlist");
				   if("manual".equalsIgnoreCase(startType)){
		               stringRedisTemplate.opsForList().remove(redisProcessorBean.toString(), 1, redisProcessorBean.getUrlDataStr());
				   }
		       }else {
		    	   //����ʧ������redis�ڴ�
		    	   if("auto".equalsIgnoreCase(startType)){
		    		   redisProcessorBean.setProcessor("schqHyzbProcessor");
				       redisProcessorBean.setProcessordesc("�г�����_��ҵֱ��");
				       redisProcessorBean.setExecutecycle("10h");
				       redisProcessorBean.setStartdate(startdateStr);
				       redisProcessorBean.setExecuteStatus("active");
		//	    	   JSONObject keyObject=new JSONObject();
			    	   if("frequently".equalsIgnoreCase(isRequestSuccess(jsonStr))){
			    		   urlMap.put("reason", "����Ƶ��");
			    	   }else if("cookieExpired".equalsIgnoreCase(isRequestSuccess(jsonStr))){
			    		   urlMap.put("reason", "cookieʧЧ");
			    	   }
			    	   stringRedisTemplate.opsForList().rightPush(redisProcessorBean.toString(), JSON.toJSONString(urlMap));
		    	    }
		       } 
    	
	}
}
