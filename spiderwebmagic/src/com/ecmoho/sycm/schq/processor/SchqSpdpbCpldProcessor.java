package com.ecmoho.sycm.schq.processor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.sycm.schq.exploration.SchqSpdpbCpldExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * �г�����--��Ʒ���̰�--��Ʒ����
 */
@Component("schqSpdpbCpldProcessor")
public class SchqSpdpbCpldProcessor extends  SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqSpdpbCpldExploration")
	private SchqSpdpbCpldExploration schqSpdpbCpldExploration;
//	@Resource(name="schqCpfxCpphProcessor")
//  private SchqCpfxCpphProcessor schqCpfxCpphProcessor;
	
	public  void start(SchqSpdpbCpldProcessor schqSpdpbPpldProcessor){
		
		 //��ȡ�����б�
  		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
  		 for(int i=0;taskList!=null&&i<taskList.size();i++){
  			 Map<String, Object> taskMap=taskList.get(i);
  			 String id=StringUtil.objectVerString(taskMap.get("id"));
  			 if(id.equals("11")){
  				 String account=StringUtil.objectVerString(taskMap.get("account"));
  				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
  				 schqHeaderBean.setCookie(refer_cookie);
//  			 'spdpb-ppld-rxsp','spdpb-ppld-llsp','spdpb-ppld-rxdp','spdpb-ppld-lldp'
  				 //������Ʒ���̰�_��Ʒ����
  				 List<HashMap<String,String>> urlSpdpbCpLdList=schqSpdpbCpldExploration.getSpdpbCpldUrlList(account,"'spdpb-ppld-lldp'");
  				 System.out.println("urlSpdpbCpLdList.size()��"+urlSpdpbCpLdList.size());
  			     for(int j=0;j<urlSpdpbCpLdList.size();j++){
  			    	 Map<String,String> map=urlSpdpbCpLdList.get(j);
  			    	 schqHeaderBean.setUrlMap(map);
  			    	 System.out.println(map.get("targetUrl"));
  			    	 try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
  			    	Spider.create(schqSpdpbPpldProcessor).addUrl(map.get("targetUrl")).run();
  			     }
  			 }
  		 }
	}
	
	public static void main(String[] args) {
		ApplicationContext ac =new ClassPathXmlApplicationContext("conf/applicationContext.xml");
		SchqSpdpbCpldProcessor schqSpdpbPpldProcessor=(SchqSpdpbCpldProcessor) ac.getBean("schqSpdpbPpldProcessor");
		schqSpdpbPpldProcessor.start(schqSpdpbPpldProcessor);
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
		       //��Ʒ���̰�_��ҵ����_������Ʒ��
		       case "spdpb-ppld-rxsp":
		    	       schqDbcom.addList(dataList, "commodityshop_brand_hotcommodityranking");
			           break;
			   //��Ʒ���̰�_��ҵ����_������Ʒ��     
		       case "spdpb-ppld-llsp":
				      schqDbcom.addList(dataList, "commodityshop_brand_flowcommodityranking");  
			          break;
			   //��Ʒ���̰�_��ҵ����_�������̰�     
		       case "spdpb-ppld-rxdp":
		    	      schqDbcom.addList(dataList, "commodityshop_brand_hotshopranking");  
			          break;
			   //��Ʒ���̰�_��ҵ����_�������̰�   
		       case "spdpb-ppld-lldp":
		    	       schqDbcom.addList(dataList, "commodityshop_brand_flowshopranking");  
			           break;
		       default:
			           break;
	        }
 
	}
    //��ȡ���ݸ�����Ϣ
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
