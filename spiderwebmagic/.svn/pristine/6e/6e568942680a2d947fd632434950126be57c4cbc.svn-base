package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;


/**
 * @author gusy
 * 市场行情--品牌分析——品牌详情
 */
@Component("schqPpfxPpxqExploration")
public class SchqPpfxPpxqExploration{
	@Resource(name="schqDbcom")
    private SchqDbcom schqDbcom;
	
	@Resource(name="schqHeaderBean")
	private  HeaderBean schqHeaderBean;
	
	private int days=1;
	
	public List<HashMap<String,String>> getPpfxPpxqUrlList(String account,String childAccountArr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //存储日期
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 //获取行业目录列表
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean);
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d天之前
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    //获取品牌分析页面多个URL
		    List<Map<String, Object>> ppfxPpxqList=schqDbcom.getSpiderChildList(childAccountArr);	
		    System.out.println("ppfxPpphList.size():"+ppfxPpxqList.size());
		    //遍历品牌排行-品牌详情列表
		    for(Map<String, Object> ppfxPpxqMap:ppfxPpxqList){
		        String childAccount=StringUtil.objectVerString(ppfxPpxqMap.get("child_account"));
			    String geturl=StringUtil.objectVerString(ppfxPpxqMap.get("geturl"));
			    //遍历行业目录
			    for(Map<String,String> mlMap:hymlList){
			    	 String cateId=mlMap.get("id");
			    	 Map<String,String> pplbMap=SchqUrlUtil.getPplb(schqDbcom, schqHeaderBean, cateId, yesterdayday);
			    	 //遍历品牌
			    	 for(Entry<String, String> entry:pplbMap.entrySet()){
			    		 String brandId=entry.getKey();//品牌ID
			    	     String brandName=entry.getValue();//品牌名称
					     //循环终端来源（0,所有终端，1，PC端，2，无线端）
					     for(int j=0;j<=2;j++){
						    //循环支付金额分段类型（0,分时段趋势图，1，时段累计图）
						    for(int k=-1;k<=1;k++){
						       //品牌分析_品牌详情_品牌概况
							       urlMap=new HashMap<String,String>();
								   String finalTargetUrl=geturl.replaceAll("##BID##", brandId).replaceAll("##CID##", cateId).replaceAll("##D##", yesterdayday).replaceAll("##DE##", j+"").replaceAll("##S##", k+"");
								   System.out.println("finalTargetUrl："+finalTargetUrl);
								   urlMap.put("childAccount",childAccount);
								   urlMap.put("accountid",accountid);
								   urlMap.put("create_at", yesterdayday);
								   urlMap.put("level", mlMap.get("level"));
								   urlMap.put("item1", mlMap.get("item1"));
								   urlMap.put("item2", mlMap.get("item2"));
								   urlMap.put("item3", mlMap.get("item3"));
								   urlMap.put("brandId", brandId);
								   urlMap.put("brandName", brandName);
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
		return urlList;
	}
	
}
