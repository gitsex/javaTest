package com.ecmoho.base.Util;

import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.util.Calendar; 

import com.alibaba.druid.sql.visitor.functions.Length;
import com.ecmoho.base.Util.SMS;

import javax.swing.JOptionPane; 

public class PDU {
		
//	public static void main(String[] args) {
//		String tmpPUD;
//		String tmpStr;
//		//"0891683108200105F0240FA001565761705001F700086140115154542388"
//		tmpPUD="3010963F91CC5DF45DF430116B6357287F519875767B5F55FF0C9A8C8BC17801003300340036003600300037300265705B578BC14E665C064E0B7EBFFF0C544A522B98917E4177ED4FE1FF0C753581117AEF75284E00952E9A8C8BC1FF0100200068007400740070003A002F002F00740062002E0063006E002F006700310059004B0066004A0079";
//		//tmpPUD="SMSC#+8613800210500";
//		PDU a = new PDU();
//		tmpStr=a.unicode2gb(tmpPUD);
//		JOptionPane.showMessageDialog(null, tmpStr, "AA", JOptionPane.PLAIN_MESSAGE); 
//	}
	
	public static String ReadSMS(){
		int iRt=0;
    	byte[] buff = new byte[2000];
    	String str = null;
    	iRt=SMS.instanceDll.ComOpen(2,3);
    	if (iRt != 0) {
    		return str;
    	}
    	iRt=SMS.instanceDll.SMSRead(buff/*, 2, 3*/);
    	if (iRt != 0) {
    		return str;
    	}
    	
    	for (int i = 0; i < buff.length; i++) {
    	if (buff[i] == 0) {
    		str = new String(buff, 0, i);
    	    break;
    	    }
    	}
    	System.out.println(str);
    	System.out.println(String.valueOf(str.length()));
    	iRt=SMS.instanceDll.ComClose();
    	if (iRt != 0) {
    		return str;
    	}
    	//截取字段
    	//str="AT+CMGL=0\r\r\n+CMGL: 1,0,,123\r\n0891683108200105F0240FA001561710818801F10009614002416470236630106DD85B9D7F5130115343725B624B673A72484E0B8F7D94FE63A5FF1A0068007400740070003A002F002F00740062002E0063006E002F006B007500560037004D006600790020FF0C5B8988C5540E62535F005343725BFF0C537353EF79FB52A87BA15E97\r\n\r\nOK\r\n";
    	if (str.length()<=16){
    		str="";
    		return str;
    	}
    	String TmpStr="";
    	if (str.substring(str.length()-4,str.length()-2).compareTo("OK")==0){
    		String StrArr[]=str.split("\\n");
    		String StrLenArr[]=StrArr[1].split(",");
    		//System.out.println(StrArr[2].replace("\\r", "").substring(StrArr[2].replace("\\r", "").length()-2*Integer.parseInt(StrLenArr[3].replace("\\r", "")), StrArr[2].replace("\\r", "").length()));
    		TmpStr= StrArr[2].replaceAll("[\\r]", "").substring(StrArr[2].replaceAll("[\\r]", "").length()-2*Integer.parseInt(StrLenArr[3].replaceAll("[\\r]", "")), StrArr[2].replaceAll("[\\r]", "").length());
    		System.out.println(TmpStr);
    	}
		//"0891683108200105F0 240FA001565761705001F700086140115154542388"
    	//str="3010963F91CC5DF45DF430116B6357287F519875767B5F55FF0C9A8C8BC17801003300340036003600300037300265705B578BC14E665C064E0B7EBFFF0C544A522B98917E4177ED4FE1FF0C753581117AEF75284E00952E9A8C8BC1FF0100200068007400740070003A002F002F00740062002E0063006E002F006700310059004B0066004A0079";
		//tmpPUD="SMSC#+8613800210500";
		PDU a = new PDU();
		String tmpPUD=a.unicode2gb(TmpStr.substring(42));
		//JOptionPane.showMessageDialog(null,tmpPUD , "AA", JOptionPane.PLAIN_MESSAGE); 
    	
		return tmpPUD;
	}
	
	public static String unicode2gb(String hexString) {
		  StringBuffer sb = new StringBuffer();

		  if (hexString == null)
		   return null; 

		  for (int i = 0; i + 4 <= hexString.length(); i = i + 4) {
		   try {
		    int j = Integer.parseInt(hexString.substring(i, i + 4), 16);
		    sb.append((char) j);
		   } catch (NumberFormatException e) {
		    return hexString;
		   }
		  }

		  return sb.toString();
		 }

	
	
	public static String decode7bit(String src) {
		  String result = null;
		        int[] b;
		        String temp=null;
		        byte srcAscii;
		        byte left=0;
		        
		  if (src != null && src.length() %2==0) {
		   result="";
		           b=new int[src.length() /2];
		           temp=src+"0";
		           for(int i=0,j=0,k=0;i<temp.length() -2;i+=2,j++){
		             b[j]=Integer.parseInt(temp.substring(i,i+2),16);
		             
		             k=j % 7;
		             srcAscii=(byte)(((b[j]<<k) & 0x7F)|left);
		             result+=(char)srcAscii;
		             left=(byte)(b[j]>>>(7-k));
		             if(k==6){
		              result+=(char)left;
		              left=0;
		             }
		             if(j==src.length() /2)
		              result+=(char)left;
		           }
		  }
		  return result;
		 }
	
}
