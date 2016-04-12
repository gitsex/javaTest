package com.ecmoho.sycm.schq.exploration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;

public class SchqUrlUtil {
	//��ȡ��ҵĿ¼
    public static List<HashMap<String, String>> getHyml(SchqDbcom schqDbcom,HeaderBean schqHeaderBean){
		
		 //��ҵĿ¼������Ϣ
		 Map<String, Object> hymlMap=schqDbcom.getSpiderChild("hyml");
		 String hymlUrlArr[]=StringUtil.objectVerString(hymlMap.get("geturl")).split("####");
		 Document pageHtmlDoc=Jsoup.parse(UrlUtil.getUrlString(schqHeaderBean,hymlUrlArr[0]));
		 String edition=pageHtmlDoc.select("[name=marketVersion]").attr("content");
		 String hymlStr=hymlUrlArr[1].replaceAll("##ED##", edition);
		 String hymlResult=UrlUtil.getUrlString(schqHeaderBean,hymlStr);
    	
 	    List<HashMap<String, String>> hymlList=new ArrayList<HashMap<String,String>>();
		JSONObject hymlJsonObject=JSON.parseObject(hymlResult);
		JSONArray dataJsonArray=hymlJsonObject.getJSONObject("content").getJSONArray("data");
         //Ŀ¼�б�һ��Ŀ¼
		HashMap<String,String> fHymlMap=null;
		//Ŀ¼�б�һ��Ŀ¼id����
		HashMap<String,String> flevelMap=new HashMap<String,String>();
		//Ŀ¼�б����Ŀ¼
		HashMap<String,String> sHymlMap=null;
		//Ŀ¼�б����Ŀ¼id����
		HashMap<String,String> sLevelMap=new HashMap<String,String>();;
		//Ŀ¼�б�����Ŀ¼
		HashMap<String,String> tHymlMap=null;
		for(int j=0;j<dataJsonArray.size();j++){
			String id=dataJsonArray.getJSONArray(j).getString(0);
			String parentId=dataJsonArray.getJSONArray(j).getString(1);
			String mlname=dataJsonArray.getJSONArray(j).getString(2);
			String fparentId=dataJsonArray.getJSONArray(j).getString(6);
			if(parentId.equalsIgnoreCase("0")){
				fHymlMap=new HashMap<String,String>();
				fHymlMap.put("id", id);
				fHymlMap.put("parentId", parentId);
				fHymlMap.put("level", "1");
				fHymlMap.put("item1", mlname);
				fHymlMap.put("item2", "");
				fHymlMap.put("item3", "");
				fHymlMap.put("fparentId", fparentId);
				fHymlMap.put("level", "1");
				hymlList.add(fHymlMap);
				flevelMap.put(id, mlname);
			}
		}
		for(int k=0;k<dataJsonArray.size();k++){
			String id=dataJsonArray.getJSONArray(k).getString(0);
			String parentId=dataJsonArray.getJSONArray(k).getString(1);
			String mlname=dataJsonArray.getJSONArray(k).getString(2);
			String fparentId=dataJsonArray.getJSONArray(k).getString(6);
			if(flevelMap.containsKey(parentId)){
				sHymlMap=new HashMap<String,String>();
				
				sHymlMap.put("id", id);
				sHymlMap.put("parentId", parentId);
				sHymlMap.put("level", "2");
				sHymlMap.put("item1",flevelMap.get(parentId));
				sHymlMap.put("item2", mlname);
				sHymlMap.put("item3", "");
				sHymlMap.put("fparentId", fparentId);
				hymlList.add(sHymlMap);
				sLevelMap.put(id, mlname);
			}
		}
		for(int k=0;k<dataJsonArray.size();k++){
			String id=dataJsonArray.getJSONArray(k).getString(0);
			String parentId=dataJsonArray.getJSONArray(k).getString(1);
			String mlname=dataJsonArray.getJSONArray(k).getString(2);
			String fparentId=dataJsonArray.getJSONArray(k).getString(6);
			if(sLevelMap.containsKey(parentId)){
				tHymlMap=new HashMap<String,String>();
				tHymlMap.put("id", id);
				tHymlMap.put("parentId", parentId);
				tHymlMap.put("level", "0");
				tHymlMap.put("item1",flevelMap.get(fparentId));
				tHymlMap.put("item2", sLevelMap.get(parentId));
				tHymlMap.put("item3", mlname);
				tHymlMap.put("fparentId", fparentId);
				hymlList.add(tHymlMap);
			}
		}
 	   return hymlList;
    }
    //ͨ����ҵĿ¼��ȡƷ���б�
    public static Map<String,String> getPplb(SchqDbcom schqDbcom,HeaderBean schqHeaderBean,String cateId,String getday){
    	//Ʒ���б�������Ϣ
    	Map<String,String> dataMap=new HashMap<String,String>();
		Map<String, Object> pplbMap=schqDbcom.getSpiderChild("pplb");
		String getUrl=StringUtil.objectVerString(pplbMap.get("geturl"));
		String targetUrl=getUrl.replaceAll("##CID##", cateId).replaceAll("##D##", getday);
		String targetResult=UrlUtil.getUrlString(schqHeaderBean,targetUrl);
		JSONObject jsonObject=JSON.parseObject(targetResult);
		JSONArray jsonArray=jsonObject.getJSONObject("content").getJSONArray("data");
		System.out.println("cateId:"+cateId);
		for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
			JSONObject brandJsonObject=jsonArray.getJSONObject(i);
			String brandId=brandJsonObject.getString("brandId");
			String brandName=brandJsonObject.getString("brandName");
			dataMap.put(brandId,brandName);
		}
    	return dataMap;
    }
}
