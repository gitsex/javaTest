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
import com.ecmoho.sycm.schq.exploration.SchqExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * �г�����--��Ʒ���̰�--Ʒ������
 */
@Component("schqSpdpbPpldProcessor")
public class SchqSpdpbPpldProcessor extends  SchqProcessor{

	@Resource(name="schqSpdpbPpldProcessor")
	private SchqProcessor schqProcessor;
	@Resource(name="schqSpdpbPpldExploration")
	private SchqExploration schqExploration;
	@Value("9,11,12,13,23,26")
	private String accountIdArr;
//  'spdpb-ppld-rxsp','spdpb-ppld-llsp','spdpb-ppld-rxdp','spdpb-ppld-lldp'
	@Value("spdpb-ppld-rxsp,spdpb-ppld-llsp,spdpb-ppld-rxdp,spdpb-ppld-lldp")
	private String childAccountArr;
	@Value("1")
	private int days;
    
	public void run() {
		super.start(schqProcessor, schqExploration, accountIdArr,childAccountArr, days);
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
