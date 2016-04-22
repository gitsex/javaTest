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
 * �г����顪����ҵֱ��
 */
@Component("schqHyrcbExploration")
public class SchqHyrcbExploration{
	@Resource(name="schqDbcom")
    private SchqDbcom schqDbcom;
	
	@Resource(name="schqHeaderBean")
	private  HeaderBean schqHeaderBean;
	
	private int days=1;
	
	public List<HashMap<String,String>> getHyrcbUrlList(String account,String childAccountArr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //�洢����
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean);
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d��֮ǰ
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    
		    List<Map<String, Object>> hyrcbList=schqDbcom.getSpiderChildList(childAccountArr);
		    for(int i=0;hyrcbList!=null&&i<hyrcbList.size();i++){
		    	Map<String, Object> hyrcbMap=hyrcbList.get(i);
			    String geturl=StringUtil.objectVerString(hyrcbMap.get("geturl"));
			    String childAccount=StringUtil.objectVerString(hyrcbMap.get("child_account"));
			    for(Map<String,String> mlMap:hymlList){
					    //ѭ���ն���Դ��0,�����նˣ�1��PC�ˣ�2�����߶ˣ�
					    for(int j=0;j<=2;j++){
						       urlMap=new HashMap<String,String>();
							   String finalTargetUrl=geturl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", mlMap.get("id")).replaceAll("##DE##", j+"");
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
							   urlMap.put("log_at", nowDateStr);
							   urlList.add(urlMap);
						    }
				       }
    	       }
    	   }
		return urlList;
	}
	
}
