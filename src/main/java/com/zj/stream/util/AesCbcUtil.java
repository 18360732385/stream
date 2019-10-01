package com.zj.stream.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

/**
 * AES CBC加解密工具类
 * @author he_peng
 *
 */
public class AesCbcUtil {
	
	private static final String CHARSET_NAME = "UTF-8";
	private static final String AES_NAME = "AES";
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	
	/**
	 * 加密
	 * @param content 加密内容
	 * @param key 加密key
	 * @param iv 加密的盐
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content,String key,String iv) throws Exception{
		byte[] contentBytes = content.getBytes(CHARSET_NAME);
		return encrypt(contentBytes, key, iv);
	}
	
	/**
	 * 加密
	 * @param content 加密内容
	 * @param key 加密key
	 * @param iv 加密的盐
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(byte[] content,String key,String iv) throws Exception{
		byte[] keyBytes = key.getBytes(CHARSET_NAME);
		byte[] ivBytes = iv.getBytes(CHARSET_NAME);
		return Base64.encodeBase64String(cipher(content, keyBytes, ivBytes, Cipher.ENCRYPT_MODE));
	}
	
	/**
	 * 解密
	 * @param content 经过加密之后的base64字符串
	 * @param key 解密key
	 * @param iv 解密的盐
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content,String key,String iv) throws Exception{
		byte[] contentBytes = Base64.decodeBase64(content);
		byte[] keyBytes = key.getBytes(CHARSET_NAME);
		byte[] ivBytes = iv.getBytes(CHARSET_NAME);
		return new String(cipher(contentBytes, keyBytes, ivBytes, Cipher.DECRYPT_MODE),CHARSET_NAME);
	}
	
	private static byte[] cipher(byte[] data,byte[] keyBytes,byte[] iv,int opmMode) throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		//初始化cipher
		Cipher cipher = Cipher.getInstance(ALGORITHM,"BC");
		//如果密钥不是16的倍数，那么就补足
		int base = 16;
		if(keyBytes.length % base != 0){
			int groups = keyBytes.length/base+(keyBytes.length%base != 0 ?1:0);
			byte[] temp = new byte[groups*base];
			Arrays.fill(temp, (byte)0);
			System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
			keyBytes = temp;
		}
		//转换成java的密钥格式
		Key key = new SecretKeySpec(keyBytes, AES_NAME);
		cipher.init(opmMode, key,new IvParameterSpec(iv));
		return cipher.doFinal(data);
	}
	

	public static void main(String[] args) throws Exception {
		String content = "123456";
		String key = "5172356798789980";
		String iv = "7941025874632551";
		
		System.out.println(content);
		
		String encoding = encrypt(content, key, iv);
		System.out.println(encoding);
		String decoding = decrypt(encoding, key, iv);
		System.out.println(decoding);
	}
}
