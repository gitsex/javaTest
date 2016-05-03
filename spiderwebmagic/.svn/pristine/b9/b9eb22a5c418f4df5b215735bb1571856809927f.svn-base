package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.sycm.schq.dao.SchqDbcom;


/**
 * @author gusy
 * 市场行情——人群画像--买家人群
 */
@Component("schqRqhxBuyerExploration")
public class SchqRqhxBuyerExploration{
	@Resource(name="schqDbcom")
    private SchqDbcom schqDbcom;
	
	@Resource(name="schqHeaderBean")
	private  HeaderBean schqHeaderBean;
	
	private int days=1;
	//获取人群画像--买家人群URL链接集合
	public List<HashMap<String,String>> getRqhxBuyerUrlList(String account,String childAccountArr) {
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
			    String childAccount=StringUtil.objectVerString(hydpDpssMap.get("child_account"));
			    String geturl=StringUtil.objectVerString(hydpDpssMap.get("geturl"));
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
