package com.ecmoho.base.selenium;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.ecmoho.base.Util.StringUtil;

//ϵͳ��¼ģ��
public abstract class SeleniumSpider {
	//��ȡwebDriver,����ʵ��
	public abstract WebDriver getWebDriver(String startType,String ip);
	//ҳ���¼�߼�,����ʵ��
	public abstract void loginPage(WebDriver webDriver,Map<String, Object> spider,String startType,String ip);
	
	public final String getCookie(String startType,String ip,Map<String, Object> spider){
		WebDriver webDriver=getWebDriver(startType,ip);
		String cookie=login(webDriver, spider,startType,ip);
		return cookie;
	}
	//��ȡcookie�߼�
	public final String login(WebDriver webDriver,Map<String, Object> spider,String startType,String ip){
		String login_url=StringUtil.objectVerString(spider.get("login_url"));
		String red_url=StringUtil.objectVerString(spider.get("red_url"));
		  //�����˺�ģ���¼
		  String cookieStr = "";
	        //���Դ���
	        int times=3;
	        int i=1;
	        try {
	        	 //���Ե�¼
		        loginPage(webDriver,spider,startType,ip);
		        //��ͣ�ļ�⣬һ����ǰҳ��URL���ǵ�¼ҳ��URL����˵��������Ѿ���������ת
	            while (i<times) {
	                Thread.sleep(500L);
	                if (!webDriver.getCurrentUrl().startsWith(login_url)) {
	                	  webDriver.get(red_url);
	           	       try {
	           			   Thread.sleep(500L);
	           		    } catch (InterruptedException e) {
	           			   e.printStackTrace();
	           		    }
	           	        webDriver.get(red_url);
	           	       try{
	           			   Thread.sleep(500L);
	           		     }catch (InterruptedException e) {
	           			   e.printStackTrace();
	           		     }
	           		  //��ȡcookie������һ����ѭ������Ϊ�͵�¼�ɹ��ˣ���Ȼ������жϲ�̫�ϸ񣬿����ٽ����޸�
	           	        Set<Cookie> cookies = webDriver.manage().getCookies();
	           	      
	           	        for (Cookie cookie : cookies) {
	           	            cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
	           	        }
//	           	     SchqDbcom.updateSpiderZxfxFlag(account, "1");
	           	        break;
	                }
	                else{
	                	 //���Ե�¼
	                	if(i<=times){
//	                		SchqDbcom.updateSpiderZxfxFlag(account, "0");
	                		i++;
	                		loginPage(webDriver,spider,startType,ip);
	                	}else{
	                		break;
	                	}
	                }
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	            return "";
	        }
		    //�˳����ر������
	        webDriver.quit();
	        return cookieStr;
	}
	
}
