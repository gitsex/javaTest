package com.ecmoho.base.Util;

import com.sun.jna.*;

	public interface SMS extends Library {  
		SMS instanceDll  = (SMS)Native.loadLibrary("mCom",SMS.class);  
	    
		public int ComOpen(int iPort,int iBund); 
		public int ComClose();
		public int SMSRead(byte[] buff/*,int iPort,int iBund*/);  
	    
	} 
	
