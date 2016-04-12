package com.ecmoho.sycm.schq.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ecmoho.base.dao.BaseDbcom;

@Component("schqDbcom")
public class SchqDbcom{
	 
      @Resource(name="schqSpiderJdbcTemplate")
      private JdbcTemplate schqSpiderJdbcTemplate;
      
      @Resource(name="schqDataJdbcTemplate")
      private JdbcTemplate schqDataJdbcTemplate;
      
	  
      //获取待爬取店铺列表
      public List<Map<String, Object>>  getSpidersTaskList(String accountLike) {
	  	   return schqSpiderJdbcTemplate.queryForList("select * from spider_account_schq   order by id asc");
	  }
      //获取全部爬取URL任务列表
      public  List<Map<String, Object>>  getChildSpiders() {
  		  return schqSpiderJdbcTemplate.queryForList("select * from spider_account_schq_child ");
  	  }
      //获取单个店铺具体信息
      public Map<String, Object>  getSpider(String account) {
  			return schqSpiderJdbcTemplate.queryForMap("select * from spider_account_schq where account = '"+account+"' LIMIT 1");
  	  }
      //获取单个URL具体信息
      public  Map<String, Object>  getSpiderChild(String account) {
  			return schqSpiderJdbcTemplate.queryForMap("select * from spider_account_schq_child where child_account = '"+account+"' LIMIT 1");
  	  }
      //获取多条URL具体信息
      public  List<Map<String, Object>>  getSpiderChildList(String accountArr) {
//    	  System.out.println("select * from spider_account_schq_child where child_account in ("+accountArr+") LIMIT 1");
  		   return schqSpiderJdbcTemplate.queryForList("select * from spider_account_schq_child where child_account in ("+accountArr+")");
  	  }
      //插入单条数据
      public void add(Map<String,String> dataMap,String tableName){
    	  BaseDbcom.add(dataMap, tableName, schqDataJdbcTemplate);
      }
      //插入多条数据
      public void addList(List<Map<String,String>> dataList,String tableName){
    	  BaseDbcom.batchAdd(dataList, tableName, schqDataJdbcTemplate);
      }
}
