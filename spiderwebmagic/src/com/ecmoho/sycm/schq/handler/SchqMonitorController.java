package com.ecmoho.sycm.schq.handler;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//监控控制器
@Controller
@RequestMapping(value="/schqmonitor")
public class SchqMonitorController {
	
	//获取抓取Url列表
   @RequestMapping(value="/init",method=RequestMethod.GET)
   public String init(){
	   return "monitor";
   }
	//获取抓取Url列表，返回json数据
   @RequestMapping(value="/list",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorList(){
	   return "呵呵呵呵呵呵呵";
   }
   //获取抓取Url详情,返回json数据
   @RequestMapping(value="/detail/{key}",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorDetail(@PathVariable(value="key") String key){   
	   return key;
   }
   //开始抓取数据
   @RequestMapping(value="/dataGrap/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String startDataGrap(@PathVariable(value="key") String key){
	   //开始抓取数据
	   return "monitor";
   }
   
}
