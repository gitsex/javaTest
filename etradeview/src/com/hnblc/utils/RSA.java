package com.hnblc.utils;
import java.io.ByteArrayOutputStream;  
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.Key;  
import java.security.KeyFactory;  
import java.security.interfaces.RSAPublicKey;  
 
import java.security.spec.X509EncodedKeySpec;  

  
import javax.crypto.Cipher;  


public class RSA {  
	  
	   

	    public static final String KEY_ALGORITHM = "RSA";  
	      
	    public static final int MAX_ENCRYPT_BLOCK = 117;  
	    
	    public static String encrypt(String txt) throws Exception {  
	        // ��ȡ��Կ������e,n   
	        FileInputStream f = new FileInputStream("D:/java/tomcat7.0/webapps/FTZ/pubkey.txt");
	        //StreamReader reader = new StreamReader(publickey);
	        ObjectInputStream b = new ObjectInputStream(f);  
	        RSAPublicKey pbk = (RSAPublicKey) b.readObject();  
	        BigInteger e = pbk.getPublicExponent();  
	        BigInteger n = pbk.getModulus();  
	        System.out.println("e= " + e);  
	        System.out.println("n= " + n);  
	        // ��ȡ����m   
	        byte ptext[] = txt.getBytes("UTF-8");  
	        BigInteger m = new BigInteger(ptext);  
	        // ��������c   
	        BigInteger c = m.modPow(e, n);  
	        System.out.println("c= " + c);  
	        // ��������   
	        String cs = c.toString();  
	        return  cs;  
	    }  
	   
	 
	    /** *//** 
	     * <p> 
	     * ��Կ���� 
	     * </p> 
	     *  
	     * @param data Դ��� 
	     * @param publicKey ��Կ(BASE64����) 
	     * @return 
	     * @throws Exception 
	     */  
	    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
	    throws Exception {
				byte[] keyBytes = MD5.decode(publicKey);
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
				Key publicK = keyFactory.generatePublic(x509KeySpec);
				// ����ݼ���
				Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
				cipher.init(Cipher.ENCRYPT_MODE, publicK);
				int inputLen = data.length;
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int offSet = 0;
				byte[] cache;
				int i = 0;
				// ����ݷֶμ���
				while (inputLen - offSet > 0) {
				    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				    } else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				    }
				    out.write(cache, 0, cache.length);
				    i++;
				    offSet = i * MAX_ENCRYPT_BLOCK;
				}
				byte[] encryptedData = out.toByteArray();
				out.close();
				return encryptedData;
    }
	   
	 
	  
	}  

