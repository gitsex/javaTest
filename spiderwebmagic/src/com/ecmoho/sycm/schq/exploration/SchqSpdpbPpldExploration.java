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
 * �г����顪����Ʒ���̰�--Ʒ������
 */
@Component("schqSpdpbPpldExploration")
public class SchqSpdpbPpldExploration{
	@Resource(name="schqDbcom")
    private SchqDbcom schqDbcom;
	
	@Resource(name="schqHeaderBean")
	private  HeaderBean schqHeaderBean;
	
	private int days=1;
	//��ȡ��Ʒ���̰�_Ʒ������URL����
	public List<HashMap<String,String>> getSpdpbPpldUrlList(String account,String childAccountArr) {
		 //��ȡ��ҵĿ¼
		 List<HashMap<String, String>> hymlList=SchqUrlUtil.getHyml(schqDbcom, schqHeaderBean);
		 //����URL������Ϣ
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //�洢����
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 String accountid=StringUtil.objectVerString(taskMap.get("id"));
		 
    	 for (int d = 1; d <=Integer.valueOf(days) ; d++) {
		    Calendar   cal   =   Calendar.getInstance();
		    cal.add(Calendar.DATE,   -d);
		    //d��֮ǰ
		    String yesterdayday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		    //��ȡ��ҪץȡURL�б�
		    List<Map<String, Object>> spdpbList= schqDbcom.getSpiderChildList(childAccountArr);	
		    
		    for(Map<String, Object> spdpbHyldMap:spdpbList){
			    String geturl=StringUtil.objectVerString(spdpbHyldMap.get("geturl"));
			    String childAccount=StringUtil.objectVerString(spdpbHyldMap.get("child_account"));
			    for(Map<String,String> mlMap:hymlList){
			    	    String cateId=mlMap.get("id");
					    String targetUrl=geturl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", cateId);	
					    Map<String, String> pplbMap=SchqUrlUtil.getPplb(schqDbcom, schqHeaderBean, cateId, yesterdayday);
					    //����Ʒ���б�
					    for(Entry<String, String> entry:pplbMap.entrySet()){
					    	String brandId=entry.getKey();//Ʒ��ID
				    	    String brandName=entry.getValue();//Ʒ������
						    //ѭ���ն���Դ��0,�����նˣ�1��PC�ˣ�2�����߶ˣ�
						    for(int j=0;j<=2;j++){
							   //ѭ��֧�����ֶ����ͣ�0,��ʱ������ͼ��1��ʱ���ۼ�ͼ��
							    for(int k=-1;k<=1;k++){
						    	   urlMap=new HashMap<String,String>();
								   String finalTargetUrl=targetUrl.replaceAll("##BID##", brandId).replaceAll("##DE##", j+"").replaceAll("##S##", k+"");
								   System.out.println("finalTargetUrl��"+finalTargetUrl);
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
