package com.hnblc.utils;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.hnblc.utils.DBcon;
import com.mysql.jdbc.ResultSetMetaData;

public class DBsql {

	public static List<Map<String, String>>  getOrderList(String where) {

	
		Connection con = null;

		PreparedStatement pre = null;

		ResultSet res = null;
		try {
			String sql = "select * from order_info "+where+"";
			System.out.println(sql);
	
			con = DBcon.getConnection();
	
			pre = con.prepareStatement(sql);
	
			res = pre.executeQuery();
		
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while (res.next()) {  
		    
	            Map<String, String> ret = new HashMap<String, String>();  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = res.getString(columnName);  
		            ret.put(columnName, value);  
		        }  
				list.add(ret);
		    }   
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
	
	
	public static List<Map<String, String>>  getOrderNumberList(String where) {

		
		Connection con = null;

		PreparedStatement pre = null;

		ResultSet res = null;
		try {
			String sql = "select orderNumber from order_info "+where;
			System.out.println(sql);
	
			con = DBcon.getConnection();
	
			pre = con.prepareStatement(sql);
	
			res = pre.executeQuery();
		
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while (res.next()) {  
		    
	            Map<String, String> ret = new HashMap<String, String>();  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = res.getString(columnName);  
		            ret.put(columnName, value);  
		        }  
				list.add(ret);
		    }   
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
	
	//获取订单列表
	public static List<Map<String, String>>  getOrderGoodsList(String where) {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		try {
			String sql = "select * from order_goods "+where;
			System.out.println("OrderGoodsList,Sql"+sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while (res.next()) {  
		        // ����ÿһ��  
	            Map<String, String> ret = new HashMap<String, String>();  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = res.getString(columnName);  
		            ret.put(columnName, value);  
		        }  
				list.add(ret);
		    }   
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
	
	
	//����������
	public static boolean updateOrder(Map<String, String> orderFeild,String orderNumber) throws UnsupportedEncodingException {

		Connection con = null;

		PreparedStatement pre = null;
		
		String updatasql="";
		
		Iterator<String> iter = orderFeild.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = orderFeild.get(key);
			    
			    updatasql +=key+"='"+value+"',";
			}
		    @SuppressWarnings("null")
			byte[] n = updatasql.getBytes("utf-8"); 
		    
		    updatasql=new String(n, 0, n.length-1, "utf-8");
		  
		try {
			String sql = "update order_info  set "+ updatasql+" where orderNumber='"+orderNumber+"'";
			// ��ȡ����
			System.out.println(sql);
			con = DBcon.getConnection();
			// ����ʵ��Ԥ����
			pre = con.prepareStatement(sql);
		
			int i = pre.executeUpdate();
			if(i>0){
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();// ��ӡ�쳣��Ϣ
		} finally {
			// �ر�
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	

	public static boolean updateOrderForbatchNumber(Map<String, String> orderFeild,String batchNumber) throws UnsupportedEncodingException {

		Connection con = null;

		PreparedStatement pre = null;
		
		String updatasql="";
		
		Iterator<String> iter = orderFeild.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = orderFeild.get(key);
			    
			    updatasql +=key+"='"+value+"',";
			}
		    @SuppressWarnings("null")
			byte[] n = updatasql.getBytes("utf-8"); 
		    
		    updatasql=new String(n, 0, n.length-1, "utf-8");
		  
		try {
			String sql = "update order_info  set "+ updatasql+" where batchNumber='"+batchNumber+"'";
			// ��ȡ����
			//System.out.println(sql);
			con = DBcon.getConnection();
			// ����ʵ��Ԥ����
			pre = con.prepareStatement(sql);
		
			int i = pre.executeUpdate();
			if(i>0){
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();// ��ӡ�쳣��Ϣ
		} finally {
			// �ر�
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	
	public static boolean updateOrderGoods(Map<String, String> orderFeild,String orderNumber) throws UnsupportedEncodingException {

		Connection con = null;

		PreparedStatement pre = null;
		
		String updatasql="";
		
		Iterator<String> iter = orderFeild.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = orderFeild.get(key);
			    
			    updatasql +=key+"='"+value+"',";
			}
		    @SuppressWarnings("null")
			byte[] n = updatasql.getBytes("utf-8"); 
		    
		    updatasql=new String(n, 0, n.length-1, "utf-8");
		  
		try {
			String sql = "update order_goods  set "+ updatasql+" where orderNumber='"+orderNumber+"'";
			// ��ȡ����
			System.out.println(sql);
			con = DBcon.getConnection();
			// ����ʵ��Ԥ����
			pre = con.prepareStatement(sql);
		
			int i = pre.executeUpdate();
			if(i>0){
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();// ��ӡ�쳣��Ϣ
		} finally {
			// �ر�
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	public static boolean addOrder(Map<String, String> orderInfo) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
	    String insertkey="";
	    String insertvalue="";
	    
		Iterator<String> iter = orderInfo.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = orderInfo.get(key);
			    if(key !=null)
			    {
			     insertkey +=key+",";
			     insertvalue +="\""+value+"\",";
			    }
			}
		    @SuppressWarnings("null")
			byte[] n = insertkey.getBytes("utf-8"); 
		    insertkey=new String(n, 0, n.length-1, "utf-8");
		  
		    @SuppressWarnings("null")
			byte[] n2 = insertvalue.getBytes("utf-8"); 
		    insertvalue=new String(n2, 0, n2.length-1, "utf-8");
		    
		try {
			String sql = "insert into order_info ("+insertkey+")"
					+ "  values("+insertvalue+")";

			//System.out.println(sql);
			con = DBcon.getConnection();
			pre = con.prepareStatement(sql);

			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	//是否存在状态
	public static String isLogExist(String orderNumber,String EtradeOutStatus) {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		
		String isexit="";
			try {
			String sql = "select id from outstatus_log where orderNumber='"+orderNumber+"' AND EtradeOutStatus='"+EtradeOutStatus+"' LIMIT 1";
			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			while (res.next()) {  
		        // ����ÿһ��  
	            String columnName =metaData.getColumnLabel(1);  
	            isexit = res.getString(columnName);  

		    }   
			return isexit;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return "";
	}
	//报关日志
	public static boolean addOutstatusLog(Map<String, String> logInfo) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
	    String insertkey="";
	    String insertvalue="";
	    
		Iterator<String> iter = logInfo.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = logInfo.get(key);
			    if(key !=null)
			    {
			     insertkey +=key+",";
			     insertvalue +="\""+value+"\",";
			    }
			}
		    @SuppressWarnings("null")
			byte[] n = insertkey.getBytes("utf-8"); 
		    insertkey=new String(n, 0, n.length-1, "utf-8");
		  
		    @SuppressWarnings("null")
			byte[] n2 = insertvalue.getBytes("utf-8"); 
		    insertvalue=new String(n2, 0, n2.length-1, "utf-8");
		    
		try {
			String sql = "insert into outstatus_log ("+insertkey+")"
					+ "  values("+insertvalue+")";

			System.out.println(sql);
			con = DBcon.getConnection();
			pre = con.prepareStatement(sql);

			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	
	//添加商品
	public static boolean addOrderGoods(Map<String, String> OrderGoods) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
	    String insertkey="";
	    String insertvalue="";
	    
		Iterator<String> iter = OrderGoods.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = OrderGoods.get(key);
			    if(key !=null)
			    {
			     insertkey +=key+",";
			     insertvalue +="\""+value+"\",";
			    }
			}
		    @SuppressWarnings("null")
			byte[] n = insertkey.getBytes("utf-8"); 
		    insertkey=new String(n, 0, n.length-1, "utf-8");
		  
		    @SuppressWarnings("null")
			byte[] n2 = insertvalue.getBytes("utf-8"); 
		    insertvalue=new String(n2, 0, n2.length-1, "utf-8");
		    
		try {
			String sql = "insert into order_goods ("+insertkey+")"
					+ "  values("+insertvalue+")";

			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);

			// ִ��sql
			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	//�����ֶ�
	public static boolean Lock(String orderNumber) throws UnsupportedEncodingException
	{
		Map<String, String> orderInfo = new HashMap<String,String>();
		orderInfo.put("is_lock","1");
		DBsql.updateOrder(orderInfo,orderNumber);
		
		return true;
	}
	
	//�����ֶ�
	public static boolean unLock(String orderNumber)  throws UnsupportedEncodingException
	{
		Map<String, String> orderInfo = new HashMap<String,String>();
		orderInfo.put("is_lock","0");
		DBsql.updateOrder(orderInfo,orderNumber);
		
		return true;
	}
	
	public static String readBatchNumber() {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		
		String cookie="";
			try {
			String sql = "select batchNumber from batch_number LIMIT 1";
			//System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			while (res.next()) {  
		        // ����ÿһ��  
	            String columnName =metaData.getColumnLabel(1);  
	            cookie = res.getString(columnName);  

		    }   
			return cookie;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
	
	//更新海关订单状态获取时间
	public static boolean updateBatchNumber(String batchNumber) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
		try {
			String sql = "update batch_number  set `batchNumber` = '"+batchNumber+"'" ;

			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);

			// ִ��sql
			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	public static String isExist(String orderNumber) {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		
		String cookie="";
			try {
			String sql = "select id from order_info where orderNumber='"+orderNumber+"' LIMIT 1";
			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			while (res.next()) {  
		        // ����ÿһ��  
	            String columnName =metaData.getColumnLabel(1);  
	            cookie = res.getString(columnName);  

		    }   
			return cookie;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return "";
	}
	
	public static String isExistGoods(String orderNumber,String productNumber) {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		
		String cookie="";
			try {
			String sql = "select id from order_goods where orderNumber='"+orderNumber+"' AND productNumber='"+productNumber+"'  LIMIT 1";
			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			while (res.next()) {  
		        // ����ÿһ��  
	            String columnName =metaData.getColumnLabel(1);  
	            cookie = res.getString(columnName);  

		    }   
			return cookie;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return "";
		
	}
	
	//更新海关订单状态获取时间
	public static boolean updateEtradeStatusTime(String where,String nowtime) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
		try {
			String sql = "update order_info  set `EtradeStatusTime` = '"+nowtime+"' where " + where ;

			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);

			// ִ��sql
			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	
	public static Map<String, String> getAccount(String ShopName) {

		
		Connection con = null;

		PreparedStatement pre = null;

		ResultSet res = null;
		try {
			String sql = "select * from account where shopName= '"+ShopName+"' LIMIT 1";
			System.out.println(sql);
	
			con = DBcon.getConnection();
	
			pre = con.prepareStatement(sql);
	
			res = pre.executeQuery();
		
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			Map<String, String> ret = new HashMap<String, String>();  
			while (res.next()) {  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = res.getString(columnName);  
		            ret.put(columnName, value);  
		        }  
		    }   
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
	
	
	//插入错误信息
	public static boolean addError(Map<String, String> Error) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
	    String insertkey="";
	    String insertvalue="";
	    
		Iterator<String> iter = Error.keySet().iterator();
		
		String key,value;
		  while(iter.hasNext())   
			{   
			    key = iter.next();
			    value = Error.get(key);
			    if(key !=null)
			    {
			     insertkey +=key+",";
			     insertvalue +="\""+value+"\",";
			    }
			}
		    @SuppressWarnings("null")
			byte[] n = insertkey.getBytes("utf-8"); 
		    insertkey=new String(n, 0, n.length-1, "utf-8");
		  
		    @SuppressWarnings("null")
			byte[] n2 = insertvalue.getBytes("utf-8"); 
		    insertvalue=new String(n2, 0, n2.length-1, "utf-8");
		    
		try {
			String sql = "insert into error ("+insertkey+")"
					+ "  values("+insertvalue+")";

			System.out.println(sql);
			
			con = DBcon.getConnection();
			
			pre = con.prepareStatement(sql);

			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	//自动任务处理日志
	public static boolean updateTaskLog(String field,String val) throws UnsupportedEncodingException {
		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		
		try {
			String sql = "update task  set `"+field+"` = '"+val+"'" ;
			System.out.println(sql);
			con = DBcon.getConnection();
			pre = con.prepareStatement(sql);
			int i = pre.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return false;
	}
	
	//获取log
   public static Map<String, String> getTaskLog() {

		
		Connection con = null;

		PreparedStatement pre = null;

		ResultSet res = null;
		try {
			String sql = "select * from task LIMIT 1";
			System.out.println(sql);
			con = DBcon.getConnection();
			pre = con.prepareStatement(sql);
			res = pre.executeQuery();
		
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			Map<String, String> ret = new HashMap<String, String>();  
			while (res.next()) {  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = res.getString(columnName);  
		            ret.put(columnName, value);  
		        }  
		    }   
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return null;
	}
   
   //
   public static String getGoodsMatch(String yh_sku,String shopName) {

		
		Connection con = null;

		PreparedStatement pre = null;

		ResultSet res = null;
		try {
			String sql = "select cus_sku from goods_match where yh_sku='"+yh_sku+"' AND shopName='"+shopName+"' LIMIT 1";
			System.out.println(sql);
			con = DBcon.getConnection();
			pre = con.prepareStatement(sql);
			res = pre.executeQuery();
		
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			int columnCount = metaData.getColumnCount();  
			String cus_sku = "";  
			while (res.next()) {  
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            cus_sku = res.getString(columnName);  
		        }  
		    }   
			return cus_sku;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return "";
	}
   
   //获取需要更新申报状态的总订单数
   public static int getTotalNeedEtradeStatusOrder(String where) {

		// ����
		Connection con = null;
		// Ԥ����
		PreparedStatement pre = null;
		// ���
		ResultSet res = null;
		
		int count=0;
			try {
			String sql = "select count(id) as c from order_info where "+where;
			System.out.println(sql);
			// ��ȡ����
			con = DBcon.getConnection();
			// ����ʵ��
			pre = con.prepareStatement(sql);
			// ִ��sql
			res = pre.executeQuery();
			// ��������
			ResultSetMetaData metaData = (ResultSetMetaData) res.getMetaData();  
			while (res.next()) {  
		        // ����ÿһ��  
	            String columnName =metaData.getColumnLabel(1);  
	            count = Integer.parseInt(res.getString(columnName));  

		    }   
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBcon.closeRes(res);
			DBcon.closePre(pre);
			DBcon.closeCon(con);
		}
		return 0;
	}
}
