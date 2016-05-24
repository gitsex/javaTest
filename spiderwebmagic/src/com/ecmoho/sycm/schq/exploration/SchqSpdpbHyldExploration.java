package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.ecmoho.base.Util.StringUtil;


/**
 * @author gusy
 * 市场行情——商品店铺榜--产品粒度
 */
@Component("schqSpdpbHyldExploration")
public class SchqSpdpbHyldExploration extends SchqExploration{
	
	public List<HashMap<String,String>> getUrlList(String account,String childAccountArr,int days) {
		 //获取行业目录
		 List<HashMap<String, String>> hymlList=getHyml(schqDbcom, schqHeaderBean);
		 //返回URL集合信息
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //存储日期
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d天之前
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    //获取需要抓取URL列表
		    List<Map<String, Object>> spdpbList= schqDbcom.getSpiderChildList(childAccountArr);	
		    
		    for(Map<String, Object> spdpbHyldMap:spdpbList){
			    String geturl=StringUtil.objectVerString(spdpbHyldMap.get("geturl"));
			    String childAccount=StringUtil.objectVerString(spdpbHyldMap.get("child_account"));
			    for(Map<String,String> mlMap:hymlList){
					    String targetUrl=geturl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", mlMap.get("id"));	
					    //循环终端来源（0,所有终端，1，PC端，2，无线端）
					    for(int j=0;j<=2;j++){
						   //循环支付金额分段类型（0,分时段趋势图，1，时段累计图）
						    for(int k=-1;k<=1;k++){
						       urlMap=new HashMap<String,String>();
							   String finalTargetUrl=targetUrl.replaceAll("##DE##", j+"").replaceAll("##S##", k+"");
	//						   System.out.println("finalTargetUrl："+finalTargetUrl);
							   urlMap.put("childAccount", childAccount);
							   urlMap.put("targetUrl", finalTargetUrl);
							   urlMap.put("accountid",accountid);
							   urlMap.put("create_at", yesterdayday);
							   urlMap.put("level", mlMap.get("level"));
							   urlMap.put("item1", mlMap.get("item1"));
							   urlMap.put("item2", mlMap.get("item2"));
							   urlMap.put("item3", mlMap.get("item3"));
							   urlMap.put("device", j+"");
							   urlMap.put("seller", k+"");
							   urlMap.put("log_at", nowDateStr);
							   urlList.add(urlMap);
						   }
					  }
	    	      }
    	      }
	     }
		return urlList;
	}
	
}
