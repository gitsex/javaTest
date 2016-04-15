package com.hnblc.utils;
import java.io.*;  
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class Curl  { 
	
	
	private static final long serialVersionUID = 1L;  
	 
    public static String sendGet(String url, String param) {
    	String result = "";
        BufferedReader in = null;
		    try {
		        String urlNameString = url + "?" + param;
		        URL realUrl = new URL(urlNameString);
		        URLConnection connection = realUrl.openConnection();
		        connection.setRequestProperty("accept", "*/*");
		        connection.setRequestProperty("connection", "Keep-Alive");
		        connection.setRequestProperty("user-agent",
		                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		        connection.setRequestProperty("Accept-Charset", "UTF-8");
		        connection.connect();
		        Map<String, List<String>> map = connection.getHeaderFields();
		        for (String key : map.keySet()) {
		            System.out.println(key + "--->" + map.get(key));
		        }
		        in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
		        String line;
		        while ((line = in.readLine()) != null) {
		            result += line;
		        }
		    } catch (Exception e) {
		        System.out.println("" + e);
		        e.printStackTrace();
		    }
		    finally {
		        try {
		            if (in != null) {
		                in.close();
		            }
		        } catch (Exception e2) {
		            e2.printStackTrace();
		        }
		    }
		
		    return result;
    }
    

    public static String sendPost(String url, String param) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type","application/x-www-formurlencoded;charset=GBK");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
           // System.out.print(result);
        } catch (Exception e) {
        	   e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        }

        return result;
    }
   
    
    public static String stPost(Map<String, String> map,String orderNumber,String batchNumber) {
    	String htmlstr = "";
    	HttpClient httpClient = new HttpClient();
    	httpClient.setConnectionTimeout(30000);
    	httpClient.setTimeout(30000);
    	PostMethod post = new PostMethod("http://vip.hnsto.cn:90/hnsto/servlet/eTradeBillinfoInterfaceCQ");
    	post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
    	// post.addRequestHeader("Content-Type","text/html;charset=UTF-8");
    	NameValuePair billinfo_interface = new NameValuePair("billinfo_interface", map.get("billinfo_interface"));
    	NameValuePair data_digest = new NameValuePair("data_digest", map.get("data_digest"));
    	NameValuePair vkey = new NameValuePair("key", "PdXk>Y8kBtIc:*x)Wo");
    	post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0");
    	post.setRequestBody(new NameValuePair[] { billinfo_interface, data_digest, vkey });
    	try {
    	    int status = httpClient.executeMethod(post);
    	    if (status == HttpStatus.SC_OK) {
    		BufferedReader bf = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), "utf-8"));
    		String s = null;
    		while ((s = bf.readLine()) != null) {
    		    String htmlstr0 = new String(s.getBytes(), "utf-8");
    		    htmlstr = htmlstr + htmlstr0 + "\n";
    		}
    	    }
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	} finally {
    	    post.releaseConnection();
    	    httpClient.getHttpConnectionManager().closeIdleConnections(0);
    	}
    	
    	 FileWriter fwriter = null;
    	//保存回传结果
        try {
			  fwriter = new FileWriter("D:/www/api/etrade/log/ST/"+batchNumber+"/Return"+orderNumber+".xml");
			  fwriter.write(htmlstr);
			 } catch (IOException ex) {
			  ex.printStackTrace();
			 } finally {
			  try {
			   fwriter.flush();
			   fwriter.close();
			  } catch (IOException ex) {
			   ex.printStackTrace();
			  }
			 }
    	return htmlstr;
        }

    
    
    
}  