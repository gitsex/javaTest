package com.ecmoho.sycm.schq.processor;





import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.sycm.schq.dao.SchqDbcom;
import com.ecmoho.sycm.schq.exploration.SchqExploration;
import com.ecmoho.sycm.schq.exploration.SchqHydpExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * 行业大盘_行业报表__SchqHydpProcessor
 */
@Component("schqHydpProcessor")
public class SchqHydpProcessor extends SchqProcessor{
	
	@Resource(name="schqHydpProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqHydpExploration")
	private SchqExploration schqExploration;
	@Value("13,23")
	private String accountIdArr;
	@Value("hydp-hybb")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public static void main(String[] args) {
		ApplicationContext ac =new ClassPathXmlApplicationContext("conf/applicationContext.xml");
		SchqProcessor schqProcessor=(SchqHydpProcessor)ac.getBean("schqHydpProcessor");
		SchqExploration schqExploration=(SchqHydpExploration)ac.getBean("schqHydpExploration");
		schqProcessor.start(schqProcessor, schqExploration, "13", "hydp-hybb", 1);
//		String processorKey="{\"accountid\":\"11\",\"accountname\":\"哈药官方旗舰店\",\"executecycle\":\"20h\",\"processor\":\"schqHydpProcessor\",\"processordesc\":\"市场行情_行业大盘\",\"recordcount\":9,\"startdate\":\"2016-05-16 11:29:43\"}";
//		System.out.println(schqProcessor);
//		schqProcessor.manualStart(schqProcessor,processorKey);
	}
	//自动执行抓取操作
	public void run() {
		super.start(schqProcessor, schqExploration,accountIdArr, childAccountArr, days);
	}
	//手动重试抓取失败的url集合
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
//	   
       System.out.println("hydp:"+jsonStr);
       
       Map<String,String> urlMap=schqHeaderBean.getUrlMap();
       Map<String,String> dataMap=new HashMap<String,String>();
       
	   dataMap.put("accountid",urlMap.get("accountid"));
	   dataMap.put("create_at", urlMap.get("create_at"));
	   dataMap.put("level", urlMap.get("level"));
	   dataMap.put("item1", urlMap.get("item1"));
	   dataMap.put("item2", urlMap.get("item2"));
	   dataMap.put("item3", urlMap.get("item3"));
	   dataMap.put("device", urlMap.get("device"));
	   dataMap.put("seller", urlMap.get("seller"));
	   dataMap.put("log_at", urlMap.get("log_at"));
	   //如果请求成功
       if("success".equalsIgnoreCase(isRequestSuccess(jsonStr))){
	       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
		   JSONArray jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	//	   JSONObject.toJSON(dataMap);
		   JSONObject dataJsonObject=null;
		   for(int i=0;i<jsonArray.size();i++){
			   dataJsonObject=jsonArray.getJSONObject(i);
			   String indexCode=dataJsonObject.getString("indexCode");
			   String currentValue=dataJsonObject.getString("currentValue");
			   dataMap.put(indexCode,currentValue);
		   }
		   schqDbcom.add(dataMap, "industry_report");
		   if("manual".equalsIgnoreCase(startType)){
               stringRedisTemplate.opsForList().remove(redisProcessorBean.toString(), 1, redisProcessorBean.getUrlDataStr());
		   }
       }else{
    	   //请求失败填入redis内存
    	   if("auto".equalsIgnoreCase(startType)){
//	    	   JSONObject keyObject=new JSONObject();
		       redisProcessorBean.setProcessor("schqHydpProcessor");
		       redisProcessorBean.setProcessordesc("市场行情_行业大盘");
		       redisProcessorBean.setExecutecycle("20h");
		       redisProcessorBean.setStartdate(startdateStr);
		       redisProcessorBean.setExecuteStatus("active");
	    	   if("frequently".equalsIgnoreCase(isRequestSuccess(jsonStr))){
	    		   urlMap.put("reason", "请求频繁");
	    	   }else if("cookieExpired".equalsIgnoreCase(isRequestSuccess(jsonStr))){
	    		   urlMap.put("reason", "cookie失效");
	    	   }
	    	   stringRedisTemplate.opsForList().rightPush(redisProcessorBean.toString(), JSON.toJSONString(urlMap));
    	    }
        }
	}
}
