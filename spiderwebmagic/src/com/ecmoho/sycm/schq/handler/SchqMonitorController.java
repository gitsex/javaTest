package com.ecmoho.sycm.schq.handler;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//监控控制器
@Controller
@RequestMapping(value="/schqmonitor")
public class SchqMonitorController {
	@Resource(name="redisTemplate")
   private StringRedisTemplate stringRedisTemplate;
	
   //进入监控页面
   @RequestMapping(value="/init",method=RequestMethod.GET)
   public String init(){
	   return "monitor";
   }
	//获取抓取列表，返回json数据
   @RequestMapping(value="/list",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorList(){
	   Set<String> set=stringRedisTemplate.keys("*Processor*");
	   Iterator<String> it=set.iterator();
	   JSONArray jsonArray=new JSONArray();
	   JSONObject jsonObject=null;
	   while(it.hasNext()){
		   String key=it.next();
		   jsonObject=new JSONObject();
		   jsonObject.put("key", key);
		   jsonObject.put("faildcount", stringRedisTemplate.boundListOps(key).size());
		   jsonArray.add(jsonObject);
	   }
	   return jsonArray.toString();
   }
   //获取抓取失败url详情,返回json数据
   @RequestMapping(value="/detail",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String getMonitorDetail(@RequestParam(value="key") String key){
	   System.out.println("get："+key);
	   BoundListOperations<String, String> bo=stringRedisTemplate.boundListOps(key);
	   System.out.println(bo.size());
	   List<String> list=bo.range(0, bo.size());
	   JSONArray jsonArray=new JSONArray();
	   JSONObject jsonObject=null;
	   for(String urlStr:list){
		   jsonObject=JSONObject.parseObject(urlStr);
		   jsonArray.add(jsonObject);
	   }
	   return jsonArray.toJSONString();
   }
   //开始抓取数据
   @RequestMapping(value="/startDataGrap",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String startDataGrap(@RequestParam(value="key") String key,HttpServletRequest request){
	   //开始抓取数据
	   System.out.println("start："+key);
	   
	   String keyArr[]=key.split(",");
	   //获取客户端ip,限同一网段
	   String clientIp=request.getRemoteAddr();
	   System.out.println("clientIp："+clientIp);
	   //获取xml配置文件
	   String processor=keyArr[2];
	   WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		try {
			Class<? extends Object> c=wac.getBean(processor).getClass();
			Method m = c.getDeclaredMethod("manual", String.class,String.class);
			m.invoke(wac.getBean(processor),key,clientIp);
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	   return "success";
   }
   //数据抓取忽略，删除在redis中相对于传参key对应内存
   @RequestMapping(value="/deleDataGrap",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
   @ResponseBody
   public String deleDataGrap(@RequestParam(value="key") String key){
	   System.out.println("dele："+key);
	   //开始抓取数据
	   stringRedisTemplate.delete(key);
	   return "success";
   }
}
