package com.ecmoho.sycm.schq.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.selenium.SeleniumSpider;
import com.ecmoho.sycm.schq.dao.SchqDbcom;
@Component("schqSeleniumSpider")
public class SchqSeleniumSpider extends SeleniumSpider {
	
	
    public static void main(String[] args) {
    	@SuppressWarnings("resource")
		ApplicationContext ac= new ClassPathXmlApplicationContext("conf/applicationContext.xml");
    	SchqSeleniumSpider ss=(SchqSeleniumSpider) ac.getBean("schqSeleniumSpider");
    	SchqDbcom sd=(SchqDbcom) ac.getBean("schqDbcom");
    	Map<String, Object> spider=sd.getSpider("sycm-lf");
    	ss.getCookie(spider);
	}
    
    @Override
	public WebDriver getWebDriver() {
//		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
	    //System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		WebDriver webDriver=null;
		try {
			URL url=new URL("http://192.168.2.14:4444/wd/hub");
			webDriver= new RemoteWebDriver(url,DesiredCapabilities.chrome());
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return webDriver;
	}
	@Override
	public void loginPage(WebDriver webDriver, Map<String, Object> spider) {
		String login_name = StringUtil.objectVerString(spider.get("login_name"));
	    String password = StringUtil.objectVerString(spider.get("password"));
	    String login_element =StringUtil.objectVerString(spider.get("login_element"));
	    String[] login_elements = login_element.split("#");
	    String login_name_id = login_elements[0];
	    String password_id = login_elements[1];
	    String login_button = login_elements[2];
	    String login_url = StringUtil.objectVerString(spider.get("login_url"));
		webDriver.get(login_url);

			//因为淘宝的登录其实是嵌入在一个iframe里面的，所以得先切换到iframe中再操作
		        
		        //webDriver.switchTo().frame(0);
		        //保证登录页面完全打开
		        try {
	    			   Thread.sleep(5040L);
	    		    } catch (InterruptedException e) {
	    		
	    			   e.printStackTrace();
	    		    }
//		       webDriver.switchTo().frame("J_loginIframe");
		       Actions action=new Actions(webDriver);
			   //输入用户名
		       WebElement  userElement=webDriver.findElement(By.id(login_name_id));
//		       action.moveToElement(userElement);
//		       action.click(userElement).build().perform();
//		       action.release();
//		       userElement.clear();
		       userElement.sendKeys(login_name);
		       try {
    			   Thread.sleep(3040L);
    		    } catch (InterruptedException e) {
    		
    			   e.printStackTrace();
    		    }
    		    
			    //输入密码 
		        WebElement  passElement=webDriver.findElement(By.id(password_id));
//		        action.moveToElement(passElement);
//		        action.click(passElement).build().perform();
//		        passElement.clear();
		        passElement.sendKeys(password);
		     
			//点击登录按钮
		        try {
					Thread.sleep(14000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        webDriver.findElement(By.id(login_button)).click();
		        webDriver.switchTo().defaultContent();
			        try {
						Thread.sleep(10000L);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}    
	    }


	
   
}
