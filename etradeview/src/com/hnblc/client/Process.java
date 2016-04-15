package com.hnblc.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnblc.utils.Curl;
import com.hnblc.utils.DBsql;

public class Process extends HttpServlet{
	
	/**
	 * @param args
	 */
	
	public void doGet(HttpServletRequest request,  
            HttpServletResponse response)  
            throws ServletException,  
            IOException {  
		      response.addHeader("Access-Control-Allow-Origin","*");
		      response.setContentType("text/html;charset=utf-8;pageEncoding=utf-8");
		      String batchNumber = request.getParameter("batchNumber");		      
		      String orderNumber = request.getParameter("orderNumber");
              String act = request.getParameter("act");
		      String viewtype = request.getParameter("viewtype");
		  
		      //预览
		      if(act.equals("view") && !orderNumber.equals("") && !viewtype.equals(""))
		      {
		    	  String xmlview = view(viewtype,orderNumber);
		    	  response.getWriter().println(xmlview);	
		    	  
		      }
		      else if(act.equals("search") && !orderNumber.equals(""))
		      {
		    	  String ret= getStatus(orderNumber);
		    	  
		    	  response.getWriter().println(ret);	
		      }
		       
		      //推送
		      if(act.equals("do") && !batchNumber.equals(""))
		      {
		    	  String xmlview = Run(batchNumber);
		    	  response.getWriter().println(xmlview);
		    	  
		      }else if(act.equals("do") && batchNumber.equals("")){
		    	  
		    	  response.getWriter().println("推送批次不能为空");	
		      }	
		      
		      //状态跟踪
		      
	}
	
	//预览报文
	public static String view(String viewtype,String orderNumber) {

	 //申通报文预览
	   if(viewtype.equals("exp"))
	   {
		   try {
				    String view = StoClient.doGet(orderNumber,"view");
				    
				    return view;
			    } catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
				}
		}
	   
	   
	 //圆通报文预览
	   if(viewtype.equals("yt"))
	   {
		   try {
				    String view = YtClient.doGet(orderNumber);
				    
				    return view;
			    } catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
				}
		}

	   
	 //海关报文预览
		if(viewtype.equals("cus"))
		{
			
			 try {	
				 
				 String view =  OrderClient.doSend(orderNumber,"view");
				 
				  return view;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return "执行完成";
	}

	public static void main(String[] args) {
		String ret =  OrderStatus.getOrderStatus("51860219444107");
		System.out.println(ret);
	}
	
	//自助查询状态
	public static String getStatus(String orderNumber)
	{
		
		String ret =  OrderStatus.getOrderStatus(orderNumber);
		
		return ret;
	}
	
	//执行推送
	public static String Run(String batchNumber) {

		if(!batchNumber.equals(""))
		   {
			
			  try {
				  DBsql.updateBatchNumber(batchNumber);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			Runtime runtime=Runtime.getRuntime();
			try{
				 System.out.println("执行cmd");
				runtime.exec("cmd /c start java  -Dfile.encoding=utf-8 -jar  D://www//api//Etrade//etradehandler.jar");
			}catch(Exception e){
				System.out.println("Error!");
			}
		  //汇总推送状态
		   }
		   System.out.println("执行成功");
		   return "执行成功,请至订单管理查看推送情况";
	}

}
