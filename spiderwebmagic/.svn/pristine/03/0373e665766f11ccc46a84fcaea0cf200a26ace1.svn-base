package com.ecmoho.base.selenium;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.ecmoho.sycm.schq.dao.SchqDbcom;

public class SeleniumSpider {

	public static String login(Map<String, String> spider) {
		
//		   Map<String, String> spider = SchqDbcom.getSpider(account);
		
		    //传入账号模拟登录
		    String login_url = spider.get("login_url");
		    String red_url = spider.get("red_url");
		    
		    System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
		  //System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		    WebDriver webDriver = new ChromeDriver();
		    webDriver.manage().window().maximize();
	        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        
	        String cookieStr = "";
	        //尝试次数
	        int i=0;
	        int times=5;
	        try {
	        	 //尝试登录
		        loginPage(webDriver,spider);
		        
		    //不停的检测，一旦当前页面URL不是登录页面URL，就说明浏览器已经进行了跳转
	            while (true) {
	            	
	                Thread.sleep(500L);
	                if (!webDriver.getCurrentUrl().startsWith(login_url)) {
	                	  webDriver.get(red_url);
	           	       try {
	           			   Thread.sleep(500L);
	           		    } catch (InterruptedException e) {
	           			// TODO Auto-generated catch block
	           			   e.printStackTrace();
	           		    }
	           	        webDriver.get(red_url);
	           	     try {
	           			   Thread.sleep(500L);
	           		    } catch (InterruptedException e) {
	           			// TODO Auto-generated catch block
	           			   e.printStackTrace();
	           		    }
	           		  //获取cookie，上面一跳出循环我认为就登录成功了，当然上面的判断不太严格，可以再进行修改
	           	        Set<Cookie> cookies = webDriver.manage().getCookies();
	           	      
	           	        for (Cookie cookie : cookies) {
	           	            cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
	           	        }
//	           	     SchqDbcom.updateSpiderZxfxFlag(account, "1");
	           	        break;
	                }
	                else{
	                	 //尝试登录
	                	if(i<=times){
//	                		SchqDbcom.updateSpiderZxfxFlag(account, "0");
	                		i++;
	                		loginPage(webDriver,spider);
	                	}else{
	                		break;
	                	}
	                }
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	            return "";
	        }

	        
	    //跳转至采集页面
	        
	        System.out.println(cookieStr);  
		//退出，关闭浏览器
	        webDriver.quit();
	        return cookieStr;
	  }
	public static void loginPage(WebDriver webDriver,Map<String, String> spider ){
		String account=spider.get("account");
		String login_name = spider.get("login_name");
	    String password = spider.get("password");
	    String login_element =spider.get("login_element");
	    String[] login_elements = login_element.split("#");
	    String login_name_id = login_elements[0];
	    String password_id = login_elements[1];
	    String login_button = login_elements[2];
	    String login_url = spider.get("login_url");
		webDriver.get(login_url);

			//因为淘宝的登录其实是嵌入在一个iframe里面的，所以得先切换到iframe中再操作
		        
		        //webDriver.switchTo().frame(0);
		        //保证登录页面完全打开
		        try {
	    			   Thread.sleep(5040L);
	    		    } catch (InterruptedException e) {
	    		
	    			   e.printStackTrace();
	    		    }
		       webDriver.switchTo().frame("J_loginIframe");
		       Actions action=new Actions(webDriver);
			   //输入用户名
		       WebElement  userElement=webDriver.findElement(By.id(login_name_id));
		       action.moveToElement(userElement);
		       action.click(userElement).build().perform();
		       action.release();
		       userElement.clear();
		       userElement.sendKeys(login_name);
		       try {
    			   Thread.sleep(3040L);
    		    } catch (InterruptedException e) {
    		
    			   e.printStackTrace();
    		    }
    		    
			    //输入密码 
		        WebElement  passElement=webDriver.findElement(By.id(password_id));
		        action.moveToElement(passElement);
		        action.click(passElement).build().perform();
		        passElement.clear();
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
	
	//元素存在与否判断(且判断元素是否隐藏)
	public static boolean doesWebElementExist(WebDriver driver, By selector)
	 { 
	        try { 
	        	WebElement webElement= driver.findElement(selector); 
	            return webElement.isDisplayed(); 
	        } catch (NoSuchElementException e) { 
	            return false; 
	        } 
	    }
	
}
