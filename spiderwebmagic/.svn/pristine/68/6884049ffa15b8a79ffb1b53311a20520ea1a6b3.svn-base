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
 * 市场行情——人群画像--卖家人群
 */
@Component("schqRqhxSellerExploration")
public class SchqRqhxSellerExploration extends SchqExploration{
	//获取人群画像--卖家人群--链接Url
	@Override
	public List<HashMap<String,String>> getUrlList(String account,String childAccountArr,int days) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //存储日期
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean);
		 
    	 for (int d = 1; d <=days ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d天之前
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    List<Map<String, Object>> rqhxBuyerList=schqDbcom.getSpiderChildList(childAccountArr);	
		    for(int i=0;rqhxBuyerList!=null&&i<rqhxBuyerList.size();i++){
			    Map<String, Object> hydpDpssMap=rqhxBuyerList.get(i);	
			    String geturl=StringUtil.objectVerString(hydpDpssMap.get("geturl"));
			    String childAccount=StringUtil.objectVerString(hydpDpssMap.get("child_account"));
			    for(Map<String,String> mlMap:hymlList){
			    	//职业分布只要全网的即可
			    	int length="rqhx-seller-xjfb".equalsIgnoreCase(childAccount)?-1:1;
			    	//遍历数据来源类型（-1，全网；0，淘宝；1，天猫）
			    	for(int k=-1;k<=length;k++){
				       urlMap=new HashMap<String,String>();
					   String finalTargetUrl=geturl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", mlMap.get("id")).replaceAll("##S##", k+"");
//				       System.out.println("finalTargetUrl："+finalTargetUrl);
					   urlMap.put("childAccount", childAccount);
					   urlMap.put("targetUrl", finalTargetUrl);
					   urlMap.put("accountid",accountid);
					   urlMap.put("create_at", yesterdayday);
					   urlMap.put("level", mlMap.get("level"));
					   urlMap.put("item1", mlMap.get("item1"));
					   urlMap.put("item2", mlMap.get("item2"));
					   urlMap.put("item3", mlMap.get("item3"));
					   urlMap.put("seller", k+"");
					   urlMap.put("log_at", nowDateStr);
					   urlList.add(urlMap);
			         }
	    	     }
		     }
	     }
		return urlList;
	}
	
}
