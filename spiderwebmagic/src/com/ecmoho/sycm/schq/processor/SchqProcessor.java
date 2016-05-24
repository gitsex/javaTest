package com.ecmoho.sycm.schq.processor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;
import com.ecmoho.sycm.schq.exploration.SchqExploration;
import com.ecmoho.sycm.schq.selenium.SchqSeleniumSpider;
import com.ecmoho.sycm.schq.entities.RedisProcessorBean;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
@Component("schqProcessor")
public class SchqProcessor implements PageProcessor{
	@Resource(name="schqDbcom")
	protected  SchqDbcom schqDbcom;
	
	@Resource(name="schqHeaderBean")
	protected  HeaderBean schqHeaderBean;
	
	@Resource(name="schqSeleniumSpider")
	private SchqSeleniumSpider schqSeleniumSpider;
	
	@Resource(name="redisTemplate")
	protected StringRedisTemplate stringRedisTemplate;
	
	@Resource(name="redisProcessorBean")
	protected RedisProcessorBean redisProcessorBean;
	
	protected  String startdateStr="";
	
	protected  String startType="auto";
	
	private Site site=Site.me().setTimeOut(3000).setRetryTimes(3).setSleepTime(1000);
	/*
	 * SchqProcessor schqProcessor������ʵ��������ץȡ����
	 * SchqExploration schqExploration̽��URL��
	 * String accountArr��ץȡ���̼���
	 * String childAccountArr��ץȡ��URL����
	 * int daysһ��ץȡ����
	 */
	public  void start(SchqProcessor schqProcessor,SchqExploration schqExploration,String accountIdArr,String childAccountArr,int days){
		 
		 //��ȡ�����б�
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 
		 startdateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(Arrays.asList(accountIdArr.split(",")).contains(id)){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String login_name=StringUtil.objectVerString(taskMap.get("login_name"));
				 redisProcessorBean.setAccountid(id);
		    	 redisProcessorBean.setAccountname(login_name);

//				 String refer_cookie=schqSeleniumSpider.getCookie(taskMap);
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 if(!isCookieUsed()){
					  refer_cookie=schqSeleniumSpider.getCookie("auto","127.0.0.1",taskMap);
					  schqDbcom.updateCookie(id,refer_cookie);
					  schqHeaderBean.setCookie(refer_cookie);
				 }
				 List<HashMap<String,String>> urlList=schqExploration.getUrlList(account,childAccountArr,days);
				 System.out.println("urlList.size()��"+urlList.size());
				 redisProcessorBean.setRecordcount(urlList.size()+"");
			     for(int j=0;j<urlList.size();j++){
			    	 Map<String,String> map=urlList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
			    	 Spider.create(schqProcessor).addUrl(map.get("targetUrl")).run();
			     }
			     //ִ����ɣ�����״̬
			     if(redisProcessorBean.getAccountid()!=null){
			    	 String oldKey=redisProcessorBean.toString();
				     redisProcessorBean.setExecuteStatus("done");
				     String newkey=redisProcessorBean.toString();
				     stringRedisTemplate.rename(oldKey, newkey); 
			     }
			 }
		 }
	 }
	

	public  void manualStart(SchqProcessor schqProcessor,String processorKey,String clientIp){
	     
		 startdateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 startType="manual";
		 redisProcessorBean=new RedisProcessorBean(processorKey);
		 redisProcessorBean.setExecuteStatus("active");
		 String newKey=redisProcessorBean.toString();
		 if(!processorKey.equalsIgnoreCase(newKey)){
			 stringRedisTemplate.rename(processorKey, newKey);
		 }
		 
	     BoundListOperations<String, String> bo=stringRedisTemplate.boundListOps(newKey);
	     System.out.println(bo.size());
	     List<String> list=bo.range(0, bo.size());
	     int accountid=Integer.valueOf(redisProcessorBean.getAccountid());
	     Map<String,Object> spider= schqDbcom.getSpiderById(accountid);
	     String refer_cookie=StringUtil.objectVerString(spider.get("reffer_cookie"));
	     //�������жϣ�cookie���ڣ��ֶ���¼���»�ȡcookie
	     schqHeaderBean.setCookie(refer_cookie);
	     if(!isCookieUsed()){
			  refer_cookie=schqSeleniumSpider.getCookie("manual",clientIp,spider);
			  schqDbcom.updateCookie(redisProcessorBean.getAccountid(),refer_cookie);
			  schqHeaderBean.setCookie(refer_cookie);
		 }
	     JSONObject jsonObject=null;
	     for(String urlStr:list){
	    	 redisProcessorBean.setUrlDataStr(urlStr);
		     jsonObject=JSONObject.parseObject(urlStr);
		     Map<String,String> map=new HashMap<String,String>();
		     for(String key:jsonObject.keySet()){
		    	 map.put(key, jsonObject.getString(key));
		     }
		     map.put("log_at", startdateStr);
		     schqHeaderBean.setUrlMap(map);
		     String targetUrl=jsonObject.getString("targetUrl");
//		     System.out.println(UrlUtil.getUrlString(schqHeaderBean, targetUrl));
		     Spider.create(schqProcessor).addUrl(targetUrl).run();
	     } 
	     
	      if(stringRedisTemplate.boundListOps(newKey).size()>0){
	    	  stringRedisTemplate.rename(newKey, processorKey);
	      }
	}
	
	@Override
	public Site getSite() {
//		System.out.println(schqHeaderBean.getCookie());
		if(!"".equals(schqHeaderBean.getUserAgent())&&schqHeaderBean.getUserAgent()!=null){
			site.addHeader("user-agent", schqHeaderBean.getUserAgent());
		}
		if(!"".equals(schqHeaderBean.getAccept())&&schqHeaderBean.getAccept()!=null){
			site.addHeader("accept", schqHeaderBean.getAccept());
		}
		if(!"".equals(schqHeaderBean.getAcceptLanguage())&&schqHeaderBean.getAcceptLanguage()!=null){
			site.addHeader("accept-language", schqHeaderBean.getAcceptLanguage());
		}
		if(!"".equals(schqHeaderBean.getOrgin())&&schqHeaderBean.getOrgin()!=null){
			site.addHeader("origin", schqHeaderBean.getOrgin());
		}
		if(!"".equals(schqHeaderBean.getReferer())&&schqHeaderBean.getReferer()!=null){
			site.addHeader("referer", schqHeaderBean.getReferer());
		}
		if(!"".equals(schqHeaderBean.getCookie())&&schqHeaderBean.getCookie()!=null){
			site.addHeader("cookie", schqHeaderBean.getCookie());
		}
		return site;
	}
	@Override
	public void process(Page arg0) {
		System.out.println("__________");
	}
	//�����󷵻ؽ���ж������Ƿ�ɹ�
	protected String isRequestSuccess(String responseStr){
		   String result="success";
		   JSONObject jsonObject=JSON.parseObject(responseStr);
		   if("sm".equalsIgnoreCase(jsonObject.getString("rgv587_flag0"))){
			   result="frequently";
		   }else if(!"0".equalsIgnoreCase(jsonObject.getJSONObject("content").getString("code"))){
			    	result="cookieExpired";
		   }
		   return result;
	 }
	 //�ж�cookie�Ƿ����
     protected boolean isCookieUsed(){
    	 boolean expired=false;
    	 //��ҵĿ¼������Ϣ
		 Map<String, Object> hymlMap=schqDbcom.getSpiderChild("hyml");
		 String hymlUrlArr[]=StringUtil.objectVerString(hymlMap.get("geturl")).split("####");
		 Document pageHtmlDoc=Jsoup.parse(UrlUtil.getUrlString(schqHeaderBean,hymlUrlArr[0]));
		 String edition=pageHtmlDoc.select("[name=marketVersion]").attr("content");
		 String hymlStr=hymlUrlArr[1].replaceAll("##ED##", edition);
		 String hymlResult=UrlUtil.getUrlString(schqHeaderBean,hymlStr);
//    	 System.out.println("hymlResult:"+hymlResult);
		 JSONObject jsonObject=JSON.parseObject(hymlResult);
    	 if("0".equalsIgnoreCase(jsonObject.getJSONObject("content").getString("code"))){
    		 expired=true;
    	 }
		 return expired;
	 }
	
}
