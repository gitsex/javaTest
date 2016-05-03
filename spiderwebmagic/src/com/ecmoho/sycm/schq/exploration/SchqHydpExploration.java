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
 * �г����顪����ҵֱ��
 */
@Component("schqHydpExploration")
public class SchqHydpExploration extends SchqExploration{
	@Override
	public List<HashMap<String,String>> getUrlList(String account,String childAccountArr,int days) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //�洢����
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean).subList(0, 1);
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d��֮ǰ
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    List<Map<String, Object>> hydpList=schqDbcom.getSpiderChildList(childAccountArr);
		    for(int i=0;hydpList!=null&&i<hydpList.size();i++){
			    Map<String, Object> hydpDpssMap=hydpList.get(i);	
			    String geturl=StringUtil.objectVerString(hydpDpssMap.get("geturl"));
			    String childAccount=StringUtil.objectVerString(hydpDpssMap.get("child_account"));
			    for(Map<String,String> mlMap:hymlList){
					    String targetUrl=geturl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", mlMap.get("id"));	
					    //ѭ���ն���Դ��0,�����նˣ�1��PC�ˣ�2�����߶ˣ�
					    for(int j=0;j<=2;j++){
						   //ѭ��֧�����ֶ����ͣ�0,��ʱ������ͼ��1��ʱ���ۼ�ͼ��
						    for(int k=-1;k<=1;k++){
						       urlMap=new HashMap<String,String>();
							   String finalTargetUrl=targetUrl.replaceAll("##DE##", j+"").replaceAll("##S##", k+"").replaceAll("##L##", mlMap.get("level")).replaceAll("\\|", "%7C");
	//						   System.out.println("finalTargetUrl��"+finalTargetUrl);
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
