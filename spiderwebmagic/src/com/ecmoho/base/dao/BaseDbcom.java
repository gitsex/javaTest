package com.ecmoho.base.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDbcom {
	      //单条数据插入
	      public static void add(Map<String, String> dataMap,String tableName,JdbcTemplate jdbcTemplate) {
	    	  StringBuffer keysb=new StringBuffer("");
	    	  StringBuffer valuesb=new StringBuffer("");
	    	  if(dataMap!=null&&dataMap.size()>0){
		    	  for(Entry<String, String> entry:dataMap.entrySet()){
		    		  keysb.append(entry.getKey()+",");
		    	      valuesb.append("'"+entry.getValue()+"',");
		    	  }
	    	  }else{
	    		  return;
	    	  }
	    	  String sql="insert into "+tableName+" ("+keysb.substring(0, keysb.length()-1)+") values ("+valuesb.substring(0, valuesb.length()-1)+")";
//	    	  System.out.println(sql);
	    	  jdbcTemplate.update(sql);
	  	}
	      //批量数据插入
	      public static int[] batchAdd(final List<Map<String, String>> dataList,String tableName,JdbcTemplate jdbcTemplate) {  
	    	  StringBuffer keysb=new StringBuffer("");
	    	  StringBuffer valuesb=new StringBuffer("");
	    	  if(dataList!=null&&dataList.size()>0){
	    		  Map<String,String> dataMap=dataList.get(0);
	    		  for(Entry<String, String> entry:dataMap.entrySet()){
	    			  keysb.append(entry.getKey()+",");
		    		  valuesb.append("?,");
//	    			  valuesb.append("'"+entry.getValue()+"',");
		    	  }
	    	  }else{
	    		  return null;
	    	  }
	    	  String keyStr=keysb.substring(0, keysb.length()-1);
	    	  String valueStr=valuesb.substring(0, valuesb.length()-1);
	    	  final String[] keyArr=keyStr.split(",");
	    	  String insertSql="insert into "+tableName+" ("+keyStr+") values ("+valueStr+")";
//	         System.out.println(insertSql);
	          int[] updateCounts = jdbcTemplate.batchUpdate(  
	        		  insertSql,
	                  new BatchPreparedStatementSetter() {  
	                        
	                          @Override  
	                          public void setValues(PreparedStatement ps, int i) throws SQLException {  
	                        	  for(int j=0;j<keyArr.length;j++){
	                        		  ps.setString(j+1, dataList.get(i).get(keyArr[j]));
	                        	  }
	                          }  
	                          @Override  
	                          public int getBatchSize() {  
	                              return dataList.size();  
	                          }  
	                  }   
	          );  
	            
	          return updateCounts;  
	      } 
}
