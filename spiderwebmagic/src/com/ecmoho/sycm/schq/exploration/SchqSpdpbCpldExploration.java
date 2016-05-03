package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;
import com.ecmoho.base.Util.StringUtil;

/**
 * @author gusy
 * 市场行情——商品店铺榜--产品粒度
 */
@Component("schqSpdpbCpldExploration")
public class SchqSpdpbCpldExploration extends SchqExploration{
	
	//获取商品店铺榜_产品粒度URL集合
	@Override
	public List<HashMap<String,String>> getUrlList(String account,String childAccountArr,int days) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //存储日期
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 //获取行业目录列表
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean).subList(0, 1);
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d天之前
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    //获取品牌分析页面多个URL
		    List<Map<String, Object>> spphbCpldList=schqDbcom.getSpiderChildList(childAccountArr);	
		    System.out.println("ppfxPpphList.size():"+spphbCpldList.size());
		    //遍历品牌列表
		    for(Map<String, Object> cpfxCpxqMap:spphbCpldList){
		        String childAccount=StringUtil.objectVerString(cpfxCpxqMap.get("child_account"));
			    String geturl=StringUtil.objectVerString(cpfxCpxqMap.get("geturl"));
			    //遍历行业目录
			    for(Map<String,String> mlMap:hymlList){
			    	 String cateId=mlMap.get("id");
		    	    
			    	 //获取产品详情——品牌列表
			    	 Map<String,String> pplbMap=SchqUrlUtil.getPplb(schqDbcom, schqHeaderBean, cateId, yesterdayday);
			    	 //遍历品牌
			    	 for(Entry<String, String> pplbEntry:pplbMap.entrySet()){
			    		 String brandId=pplbEntry.getKey();//品牌ID
			    	     String brandName=pplbEntry.getValue();//品牌名称
			    	     System.out.println(brandName);
			    	     //获取产品详情--品牌列表--产品列表
			    	     List<HashMap<String,String>> cplbList=SchqUrlUtil.getCpfxCpxqModels(schqDbcom, schqHeaderBean, cateId, brandId,yesterdayday);
			    	     //遍历产品列表
			    	     for(int i=0;cplbList!=null&&i<cplbList.size();i++){
			    	    	 String modelId=cplbList.get(i).get("modelId");
			    	    	 String modelName=cplbList.get(i).get("modelName");
			    	    	 String spuId=cplbList.get(i).get("spuId");
						     //循环终端来源（0,所有终端，1，PC端，2，无线端）
						     for(int j=0;j<=2;j++){
							    //循环支付金额分段类型（0,分时段趋势图，1，时段累计图）
							    for(int k=-1;k<=1;k++){
							       //品牌分析_品牌详情_品牌概况
								       urlMap=new HashMap<String,String>();
									   String finalTargetUrl=geturl.replaceAll("##BID##", brandId).replaceAll("##CID##", cateId).replaceAll("##MID##", modelId).replaceAll("##D##", yesterdayday).replaceAll("##DE##", j+"").replaceAll("##SID##", spuId).replaceAll("##S##", k+"");
//									   System.out.println("finalTargetUrl："+finalTargetUrl);
									   urlMap.put("childAccount",childAccount);
									   urlMap.put("accountid",accountid);
									   urlMap.put("create_at", yesterdayday);
									   urlMap.put("level", mlMap.get("level"));
									   urlMap.put("item1", mlMap.get("item1"));
									   urlMap.put("item2", mlMap.get("item2"));
									   urlMap.put("item3", mlMap.get("item3"));
									   urlMap.put("brandId", brandId);
									   urlMap.put("brandName", brandName);
									   urlMap.put("modelId",modelId);
									   urlMap.put("modelName", modelName);
									   urlMap.put("spuId", spuId);
									   urlMap.put("device", j+"");
									   urlMap.put("seller", k+"");
									   urlMap.put("log_at", nowDateStr);
									   urlMap.put("targetUrl", finalTargetUrl);
									   urlList.add(urlMap);
							     }
						     }
			    	     }
			        }   
		         }		
    	     }
	     }
		return urlList;
	}

}
