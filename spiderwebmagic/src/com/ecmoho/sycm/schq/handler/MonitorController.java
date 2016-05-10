package com.ecmoho.sycm.schq.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//监控控制器
@Controller
@RequestMapping(value="/monitor")
public class MonitorController {
	//获取抓取Url列表
   @RequestMapping(value="/init",method=RequestMethod.GET)
   public String init(HttpServletRequest request){
	   return "monitor";
   }
	//获取抓取Url列表
   @RequestMapping(value="/getMonitor",method=RequestMethod.GET)
   @ResponseBody
   public String getMonitorList(){
	   return "heheheregrh";
   }
   //获取抓取Url详情
   @RequestMapping(value="/getMonitorDetail/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String getMonitorDetail(@PathVariable(value="key") String key){   
	   return key;
   }
   //开始抓取数据
   @RequestMapping(value="/startDataGrap/{key}",method=RequestMethod.GET)
   @ResponseBody
   public String startDataGrap(@PathVariable(value="key") String key){
	   //开始抓取数据
	   return "monitor";
   }
   
}
