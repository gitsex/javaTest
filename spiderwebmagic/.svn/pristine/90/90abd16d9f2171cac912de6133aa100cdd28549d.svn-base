package com.ecmoho.base.Util;



public class StringUtil {
   public static String objectVerString(Object object){
	   return object==null?"":object.toString();
   }
   public static String isNullString(String str){
	   return str==null?"":str;
   }
   
   public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}  
  
  private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}   
   
  public static String toHexString(String s) 
  { 
	  String str=""; 
	  for (int i=0;i<s.length();i++) 
	  { 
	  int ch = (int)s.charAt(i); 
	  String s4 = Integer.toHexString(ch); 
	  str = str + s4; 
	  } 
	  return str; 
  } 
   
   
   
}
