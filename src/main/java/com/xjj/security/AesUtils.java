package com.xjj.security;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

/**
 * Created by XuJijun on 2018-05-04.
 */
public class AesUtils {

    //private static final String AES_TYPE ="AES/ECB/PKCS5Padding";

    private String aseType = "AES/ECB/PKCS5Padding";
    //private String keyStr;
    private Key key;

    public AesUtils(String aseType, String keyStr) {
        this.aseType = aseType;
        this.key = this.generateKey(keyStr);
    }

    public AesUtils(String keyStr) {
        this.key = this.generateKey(keyStr);
    }

    /**
     * 加密，然后用BASE64 编码
     */
    public String encrypt(String plainText) {
		byte[] encrypt = null;
		try{
			//Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance(aseType);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(encrypt!=null) {
			return new String(Base64.getUrlEncoder().encode(encrypt));
		}else {
			return null;
		}
	}

    /**
     * 解密
     */
	public String decrypt(String encryptData) {
		byte[] decrypt = null;
		try{
			//Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance(aseType);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.getUrlDecoder().decode(encryptData));
		}catch(Exception e){
			e.printStackTrace();
		}

		if(decrypt!=null) {
			return new String(decrypt).trim();
		}else {
			return null;
		}

	}

	//生成一个Key对象
	private Key generateKey(String keyStr){
		try{
		    // 构造一个长度为16 的key
            String md5Key = DigestUtils.md5Hex(keyStr);
            md5Key = md5Key.substring(0,16).toUpperCase();

            //System.out.println("generated key: " + md5Key);

            return new SecretKeySpec(md5Key.getBytes(), "AES");
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}

	public static void main(String[] args) {
        String keyStr = "iAmPw";
        String aesType ="AES/ECB/PKCS5Padding";

        AesUtils aesUtils = new AesUtils(aesType, keyStr);

		String plainText = "Hello, \t我是中文, I am English.";//"this is a string will be encrypted";

		String encText = aesUtils.encrypt(plainText);
		String decString = aesUtils.decrypt(encText);

        System.out.println("加密前：" + plainText);
		System.out.println("加密后：" + encText);
		System.out.println("解密后：" + decString);
	}
}