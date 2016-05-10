package com.ecmoho.sycm.schq.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//��ؿ�����
@Controller
@RequestMapping(value="/monitor")
public class MonitorController {
	//��ȡץȡUrl�б�
   @RequestMapping(value="/init",method=RequestMethod.GET)
   public String init(HttpServletRequest request){
	   return "monitor";
   }
	//��ȡץȡUrl�б�
   @RequestMapping(value="/getMonitor",method=RequestMethod.GET)
   @ResponseBody
   public String getMonitorList(){
	   return "heheheregrh";
   }
   //��ȡץȡUrl����
   @RequestMapping(value="/getMonitorDetail/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String getMonitorDetail(@PathVariable(value="key") String key){   
	   return key;
   }
   //��ʼץȡ����
   @RequestMapping(value="/startDataGrap/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String startDataGrap(@PathVariable(value="key") String key){
	   //��ʼץȡ����
	   return "monitor";
   }
   
}
