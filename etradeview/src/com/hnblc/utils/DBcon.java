package com.hnblc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcon {

	/**
	 * 
	 * @return
	 */
	public static Connection getConnection() {

		try {
			String url = "jdbc:mysql://121.41.95.196:3306/etrade?user=root&password=root&useUnicode=true&characterEncoding=UTF-8&&zeroDateTimeBehavior=convertToNull";
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection(url);
			
			return con;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 锟截憋拷锟斤拷锟斤拷
	 * @param con
	 */
	public static void  closeCon(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 锟截憋拷实锟斤拷
	 * @param pre
	 */
	public static void  closePre(PreparedStatement pre){
		try {
			pre.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 锟截闭斤拷锟�
	 * @param res
	 */
	public static void  closeRes(ResultSet res){
		try {
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
