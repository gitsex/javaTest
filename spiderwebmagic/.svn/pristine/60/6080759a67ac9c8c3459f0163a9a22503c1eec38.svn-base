package com.ecmoho.sycm.schq.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;
import com.ecmoho.sycm.schq.exploration.SchqExploration;
import com.ecmoho.sycm.schq.selenium.SchqSeleniumSpider;

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
	private Site site=Site.me().setTimeOut(3000).setRetryTimes(3).setSleepTime(2000);
	/*
	 * SchqProcessor schqProcessor本身类实例，创建抓取动作
	 * SchqExploration schqExploration探查URL类
	 * String accountArr待抓取店铺集合
	 * String childAccountArr待抓取子URL集合
	 * int days一次抓取天数
	 */
	public  void start(SchqProcessor schqProcessor,SchqExploration schqExploration,String accountIdArr,String childAccountArr,int days){
		 
		 //获取店铺列表
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 
		 
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(Arrays.asList(accountIdArr.split(",")).contains(id)){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
//				 String refer_cookie=schqSeleniumSpider.getCookie(taskMap);
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 List<HashMap<String,String>> urlHyzbList=schqExploration.getUrlList(account,childAccountArr,days);
				 System.out.println(urlHyzbList.size());
			     for(int j=0;j<urlHyzbList.size();j++){
			    	 Map<String,String> map=urlHyzbList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
			    	 Spider.create(schqProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
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
		
	}

}
