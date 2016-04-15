package com.hnblc.utils;

import java.io.*;     
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.*;   
import javax.xml.parsers.*;   
  
public class XML {   
	
	//传入xml文件地址 解析文件
	public static String parseXml(String Name,String Node,String fileName){   
	 
		String ret="";
		
		try{   
			File f=new File("D:/www/api/etrade/log/"+fileName);   
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();   
			DocumentBuilder builder=factory.newDocumentBuilder();   
			Document doc = builder.parse(f);
			
			NodeList nl = doc.getElementsByTagName(Name);  
			
			for (int i=0;i<nl.getLength();i++){   
				  ret = doc.getElementsByTagName(Node).item(i).getFirstChild().getNodeValue();  
				 
			}   
			
		}catch(Exception e){   
			e.printStackTrace();   
		}   
		return ret;
    }
	
	 public static  Map<String, String> getStatus(String xml) throws DocumentException {
	        
	        long start = System.currentTimeMillis();
	 
	        Map<String, String>  Status=new HashMap<String,String>();
	    
	        SAXReader reader = new SAXReader();
			try {
				
				 org.dom4j.Document doc = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
				 org.dom4j.Element root = doc.getRootElement();
			     org.dom4j.Element element;
		
					for (Iterator<?> k = root.elementIterator("Result"); k.hasNext();) {
						
						         element=  (org.dom4j.Element) k.next();
						                   Status.put("Status",element.elementText("Status"));
						                   Status.put("Remark",element.elementText("Remark"));
						                   System.out.println(element.elementText("Remark"));
						                   Status.put("logisticsNo",element.elementText("logisticsNo"));
					            
					  } 
					
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           
	        long end = System.currentTimeMillis();
	        System.out.println("解析xml耗时：" + (end - start) + "ms");
	        
	       return Status;
	    }
	
}