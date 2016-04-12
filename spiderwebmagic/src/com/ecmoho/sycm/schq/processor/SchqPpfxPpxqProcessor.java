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
import com.ecmoho.sycm.schq.exploration.SchqPpfxPpxqExploration;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
/**
 * 
 * @author gusy
 * �г����顪��Ʒ�Ʒ�������Ʒ������
 */
@Component("schqPpfxPpxqProcessor")
public class SchqPpfxPpxqProcessor extends  SchqProcessor{
//	@Resource(name="schqDbcom")
//	private  SchqDbcom schqDbcom;
//	@Resource(name="schqHeaderBean")
//	private  HeaderBean schqHeaderBean;
	@Resource(name="schqPpfxPpxqExploration")
	private SchqPpfxPpxqExploration schqPpfxPpxqExploration;
//	@Resource(name="schqPpfxPpphProcessor")
//    private SchqPpfxPpphProcessor schqPpfxPpphProcessor;
	public  void start(SchqPpfxPpxqProcessor schqPpfxPpxqProcessor){
		 //��ȡ�����б�
		 List<Map<String, Object>> taskList=schqDbcom.getSpidersTaskList("sycm");
		 for(int i=0;taskList!=null&&i<taskList.size();i++){
			 Map<String, Object> taskMap=taskList.get(i);
			 String id=StringUtil.objectVerString(taskMap.get("id"));
			 if(id.equals("9")){
				 String account=StringUtil.objectVerString(taskMap.get("account"));
				 String refer_cookie=StringUtil.objectVerString(taskMap.get("reffer_cookie"));
				 schqHeaderBean.setCookie(refer_cookie);
				 //��ȡƷ�Ʒ���URL�б�
//				 "'ppfx-ppph-rxpp','ppfx-ppph-bspp','ppfx-ppph-gllpp','ppfx-ppph-gsspp'"
				 List<HashMap<String,String>> ppphUrlList=schqPpfxPpxqExploration.getPpfxPpxqUrlList(account,"'ppfx-ppxq-ngm'");
				 System.out.println("ppphUrlList.size()��"+ppphUrlList.size());
			     for(int j=0;j<ppphUrlList.size();j++){
			    	 Map<String,String> map=ppphUrlList.get(j);
			    	 schqHeaderBean.setUrlMap(map);
//			    	 System.out.println(map.get("targetUrl"));
			    	 Spider.create(schqPpfxPpxqProcessor).addUrl(map.get("targetUrl")).run();
			     }
			 }
		 }
	}
	
	public static void main(String[] args) {
		 ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext.xml"); 
		 SchqPpfxPpxqProcessor schqPpfxPpphProcessor = (SchqPpfxPpxqProcessor)ac.getBean("schqPpfxPpxqProcessor"); 
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
	   JSONArray dataJsonArray=null;
	   JSONObject dataJsonObject=null;
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   String childAccount=urlMap.get("childAccount");
	   switch (childAccount) {
	       //Ʒ�Ʒ���-Ʒ������--Ʒ�Ƹſ�
	       case "ppfx-ppxq-ppgk":
	    	       dataJsonObject=finalTargetJsonObject.getJSONObject("content").getJSONObject("data");
	    	       dataMap=getDataMap(dataMap, urlMap);
	    	       for(String key:dataJsonObject.keySet()){
	    	    	   dataMap.put(key, dataJsonObject.getString(key));
	    	    	   schqDbcom.add(dataMap, "brand_detail_overview");
	    	       }
		           break;
		   //Ʒ�Ʒ���--Ʒ������--֧���۸�        
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
		   //Ʒ�Ʒ���--Ʒ������--ְҵ�ֲ�        
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
		   //Ʒ�Ʒ���--Ʒ������--����ֲ�        
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
		   //Ʒ�Ʒ���--Ʒ������--��90��֧�����        
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
		   //Ʒ�Ʒ���--Ʒ������--��90�칺�����        
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
