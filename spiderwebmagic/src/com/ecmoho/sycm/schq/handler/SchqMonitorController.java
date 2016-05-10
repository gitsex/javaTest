package com.ecmoho.sycm.schq.handler;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//��ؿ�����
@Controller
@RequestMapping(value="/schqmonitor")
public class SchqMonitorController {
	
	//��ȡץȡUrl�б�
   @RequestMapping(value="/init",method=RequestMethod.GET)
   public String init(){
	   return "monitor";
   }
	//��ȡץȡUrl�б�����json����
   @RequestMapping(value="/list",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorList(){
	   return "�ǺǺǺǺǺǺ�";
   }
   //��ȡץȡUrl����,����json����
   @RequestMapping(value="/detail/{key}",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorDetail(@PathVariable(value="key") String key){   
	   return key;
   }
   //��ʼץȡ����
   @RequestMapping(value="/dataGrap/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String startDataGrap(@PathVariable(value="key") String key){
	   //��ʼץȡ����
	   return "monitor";
   }
   
}
