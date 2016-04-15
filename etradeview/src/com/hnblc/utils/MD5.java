package com.hnblc.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;

public class MD5 {
	 /**
     * @param args
     */
    public static String MD5(byte[] btInput) {
	char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	try {

	    // ���MD5ժҪ�㷨�� MessageDigest ����
	    MessageDigest mdInst = MessageDigest.getInstance("MD5");
	    // ʹ��ָ�����ֽڸ���ժҪ
	    mdInst.update(btInput);
	    // �������
	    byte[] md = mdInst.digest();
	    // ������ת����ʮ�����Ƶ��ַ�����ʽ
	    int j = md.length;
	    char str[] = new char[j * 2];
	    int k = 0;
	    for (int i = 0; i < j; i++) {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
    public static StringBuffer md5s(String plainText) { 
		StringBuffer buf = new StringBuffer("");
		try {  
			MessageDigest md = MessageDigest.getInstance("MD5");  
			md.update(plainText.getBytes());   
			byte b[] = md.digest();   
			int i;   
			
			for (int offset = 0; offset < b.length; offset++) {  
			   i = b[offset];    
			   if (i < 0)     
				   i += 256;   
			   if (i < 16)     
				   buf.append("0");   
			   	   buf.append(Integer.toHexString(i));   
			   }  
			  
		 } catch (NoSuchAlgorithmException e) {  
			 // TODO Auto-generated catch block   
			 e.printStackTrace(); 
		 } 
		 return buf;
	} 
	// 32
	public static String MD5_32(String text){
		StringBuffer buf = md5s(text);
		return buf.toString();
	}
	

	// 16
	public static String MD5_16(String text){
		StringBuffer buf = md5s(text);
		return buf.toString().substring(8, 24);
	}
	
	 public static String encode(byte[] binaryData) { 
	        try { 
	            return new String(Base64.encodeBase64(binaryData), "UTF-8"); 
	        } catch (UnsupportedEncodingException e) { 
	            return null; 
	        } 
	    } 
	      

	    public static byte[] decode(String base64String) { 
	        try { 
	            return Base64.decodeBase64(base64String.getBytes("UTF-8")); 
	        } catch (UnsupportedEncodingException e) { 
	            return null; 
	        }
	    }
	     
}