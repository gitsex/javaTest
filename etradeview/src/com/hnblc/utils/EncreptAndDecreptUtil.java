package com.hnblc.utils;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密解密
 * 
 * @author Administrator
 * 
 */
public class EncreptAndDecreptUtil {
	/** */
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 使用公钥加密字符串
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param pulicKey
	 *            加密时需要的公钥
	 * @return
	 */
	
	public static String encreptData(String content, String pulicKey) {
		/* Create the cipher */
		Cipher rsaCipher = null;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache = null;
		int i = 0;
		try {
			int inputLen = content.getBytes("UTF-8").length;
			rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			// rsaCipher.init(Cipher.ENCRYPT_MODE, pub);
			rsaCipher.init(Cipher.ENCRYPT_MODE,
					RSAUtil.getPublicKeyByString(pulicKey));
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = rsaCipher.doFinal(content.getBytes("UTF-8"),
							offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = rsaCipher.doFinal(content.getBytes("UTF-8"),
							offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return RSAUtil.encryptBASE64(encryptedData);
		}
//		catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//			e.printStackTrace();
//		} 
		catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
