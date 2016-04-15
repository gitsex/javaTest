/*
 * 项目名: 全球大联盟
 * 文件名: ThreeDes.java
 *
 * 版权声明:
 *     本系统的所有内容，包括源码、页面设计，文字、图像以及其他任何信息，
 *     如未经特殊说明，其版权均属圆通速递所有。
 *
 *     Copyright (c) 2014 圆通速递
 *     版权所有
 */
package com.hnblc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * DES对称加密算法 
 * 
 * String encryStr="teststr";  
   ThreeDes des = new ThreeDes(); // 实例化一个对像  
   String strEnc = des.getEncString(encryStr);// 加密字符串,返回String的密文  
   System.out.println(strEnc);  
   String strDes = des.getDesString(strEnc);// 把String 类型的密文解密  
   System.out.println(strDes);
 * @author wenhuiwang
 * @see 使用3DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.  
 * @see 对称加密算法就是能将数据加解密。加密的时候使用密钥对数据进行加密，解密的时候使用同样的密钥对数据进行解密  
 * @see DES是美国国家标准研究所提出的算法。由于加解密的数据安全性和密钥长度成正比，故DES的56位密钥已经形成安全隐患  
 * @see 后来针对DES算法进行了改进，有了三重DES算法（也称DESede或Triple-DES）。全名是TDEA：Triple Data  
 *      Encryption Algorithm  
 * @see DESede针对DES算法的密钥长度较短以及迭代次数偏少问题做了相应改进，提高了安全强度  
 * @see 不过DESede算法处理速度较慢，密钥计算时间较长，加密效率不高  
 */
public class ThreeDes {

    private static final Logger LOGGER  = Logger.getLogger(ThreeDes.class);
    private Key                 key;                                                 // 密钥
    private String              ENCRYPT_ALGORITHM = "DES";

    /**
     * 初始化默认key
     */
    public ThreeDes() {
        getKey(getDefultKey());
    }

    /**
     * 初始化自定义key
     */
    public ThreeDes(String keyStr) {
        getKey(keyStr);
    }

    /**
     * 获取默认主机host为key
     * @return
     */
    private String getDefultKey() {
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            return netAddress.getHostName();
        } catch (UnknownHostException e) {
            LOGGER.warn("getKey error:", e);
            e.printStackTrace();
        }
        return null;
    }

    /**  
     * 根据参数生成KEY  
     *   
     * @param strKey  
     *            密钥字符串  
     */
    private void getKey(String strKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ENCRYPT_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            generator.init(secureRandom);
            this.key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            LOGGER.warn("getKey error:", e);
            e.printStackTrace();
        }
    }

    /**  
     * 加密String明文输入,String密文输出  
     *   
     * @param strMing  
     *            String明文  
     * @return String密文  
     */
    public String getEncString(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        Base64 base64en = new Base64();
        try {
            byteMing = strMing.getBytes("UTF8");
            byteMi = this.getEncCode(byteMing);
            strMi = base64en.encodeAsString(byteMi);
        } catch (Exception e) {
            LOGGER.warn("getEncString error:", e);
            e.printStackTrace();
        } finally {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**  
     * 解密 以String密文输入,String明文输出  
     *   
     * @param strMi  
     *            String密文  
     * @return String明文  
     */
    public String getDesString(String strMi) {
        Base64 base64De = new Base64();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decode(strMi);
            byteMing = this.getDesCode(byteMi);
            strMing = new String(byteMing, "UTF8");
        } catch (Exception e) {
            LOGGER.warn("getDesString error:", e);
            e.printStackTrace();
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**  
     *  为getEncString方法提供服务  
     *    
     * 加密以byte[]明文输入,byte[]密文输出  
     *   
     * @param byteS  
     *            byte[]明文  
     * @return byte[]密文  
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            LOGGER.warn("getEncCode error:", e);
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**  
     * 为getDesString方法提供服务  
     *   
     * 解密以byte[]密文输入,以byte[]明文输出  
     *   
     * @param byteD  
     *            byte[]密文  
     * @return byte[]明文  
     */
    private byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // byteFina = "Hello".getBytes();
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            LOGGER.warn("getDesCode error:", e);
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

}
