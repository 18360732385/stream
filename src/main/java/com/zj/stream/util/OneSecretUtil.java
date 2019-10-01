package com.zj.stream.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.util.UUID;

/**
 * 一次一密工具类
 * @author he_peng
 *
 */
public class OneSecretUtil {
	/**
	 * 通过rsa私钥对aes的密钥进行解密
	 * @param key
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptKeyByPrivateKey(String key,byte[] privateKey) throws Exception{
		return RsaUtil.decryptByPrivateKey(key, privateKey, RsaUtil.TRANSFORMATION);
	}
	
	/**
	 * 通过rsa公钥对aes的密码进行解密
	 * @param key
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptKeyByPublicKey(String key,byte[] publicKey) throws Exception{
		return RsaUtil.decryptByPublicKey(key, publicKey, RsaUtil.TRANSFORMATION);
	}
	
	/**
	 * 通过rsa私钥对aes的密钥进行加密
	 * @param key
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptKeyByPrivateKey(String key,byte[] privateKey) throws Exception{
		return RsaUtil.encryptByPrivateKey(key, privateKey, RsaUtil.TRANSFORMATION);
	}
	
	/**
	 * 通过rsa公钥对aes的密钥进行加密
	 * @param key
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptKeyByPublicKey(String key,byte[] publicKey) throws Exception{
		return RsaUtil.encryptByPublicKey(key, publicKey, RsaUtil.TRANSFORMATION);
	}
	
	
	/**
	 * 对加密数据进行解密
	 * @param key aes的解密密钥
	 * @param content 解密数据
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String key,String content) throws Exception{
		byte[] contents = Base64.decodeBase64(content);
		Key aesKey = AesEcbUtil.toKey(key.getBytes());
	    byte[] data = AesEcbUtil.decrypt(contents, aesKey);
	    return new String(data, "utf-8");  
	}
	
	/**
	 * 对数据进行加密
	 * @param key aes的加密密钥
	 * @param content 加密数据
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String key,String content) throws Exception{
		Key aesKey = AesEcbUtil.toKey(key.getBytes());
		byte[] encrypt = AesEcbUtil.encrypt(content.getBytes(), aesKey);
		return Base64.encodeBase64String(encrypt);
	}
	
	/**
	 * 进行数据签名
	 * @param content 签名原文
	 * @param salt 盐
	 * @return
	 */
	public static String digestWithSha256(String content, String salt){
        return Hashing.sha256().hashBytes((content + salt).getBytes()).toString();
    }
	
	/**
	 * 进行数据签名验证
	 * @param content 签名明文
	 * @param sign 签名结果
	 * @param salt 签名盐
	 * @return true:验签成功    false:验签失败
	 */
	public static boolean verify(String content,String sign,String salt){
		String hash = digestWithSha256(content, salt);
		if(sign.equals(hash)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		//私钥
		String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKPBz9/dtVRmXdvPM+HqNQNCHNZOQX7wAdJLoH8SevIh9whSHQBKfY5U6KUhxQHAV/5WRMOjU3NnHUji3ihj4Zd7JzEpPn51DfVX54MrvITF05FqMFnGl70p3DtUBYtq+00MZixwdoJV2z46PGfp9VabTvmEWX8J0/kK4MVx2S5pAgMBAAECgYEAjOiz//VeF97x+rfD9Cy9Ky2TdNDnyNurJZLP6ygyxPF3CSDnNZ3jhjsH6TgytnwU+5SuHQ4IO/BJobMd6PFxZqoE6NPkzZ7XMNmYWqwVfaT8IEMb/8tyEDlqtqJ29p2jjqxpnmfIbwAJV5SuJhQVHxXdnvPZzD+SrQZhsPD56mUCQQD3Fr5oFBtCIMA+J8gyuFVC15SZBsBxgis/gGf2Z8HkXH7wKGFMl0V3nv2scfm2akEmpU9l6gaoCAX6V/NeN16TAkEAqam0cWqEAzdwnFxMhwuPIGB3HdWr+8KAezihT8Nu+hMOvFwa5N44zZUpUNItE1tZdoxKL8FJVjZIGqZgVIagkwJABXh4cL95NwaYmwewdAcGxDsXrCetHRY/tOE37AmyDkdJ/DE4rkRvIk3f2fCQEczacmuYjW4YBaJf14IY/k4FhwJAIA3+F4eGVY0962zM0J7wda+EEdDy42gGGkHCyG0mJH1BRXkpY6BQnxbVbX2oIoYZyvoKcidUkcNDsDVK8e1VnQJAU6AY8TBbuCyUTM1kpdvrrXQKSAzSihoZMSAk3GpfGkRj05kiov3+y5yoP5QrVQaM4ykpZuu4e32UHLaLcK+snA==";
		//公钥
		String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjwc/f3bVUZl3bzzPh6jUDQhzWTkF+8AHSS6B/EnryIfcIUh0ASn2OVOilIcUBwFf+VkTDo1NzZx1I4t4oY+GXeycxKT5+dQ31V+eDK7yExdORajBZxpe9Kdw7VAWLavtNDGYscHaCVds+Ojxn6fVWm075hFl/CdP5CuDFcdkuaQIDAQAB";
		//aes加密密钥
		String aesKey = UUID.randomUUID().toString().replace("-","").substring(0, 16);
		
		JSONObject dataJSONObject = new JSONObject();
		dataJSONObject.put("deviceNo", "asdfafdafdasdf");
		dataJSONObject.put("loginType", "1");
		//加密数据
		String data = dataJSONObject.toJSONString();
		//签名盐
		String salt = "1234567890123456";
		
		byte[] privateKey = Base64.decodeBase64(privateKeyBase64);
		byte[] publicKey = Base64.decodeBase64(publicKeyBase64);
		//通过私钥对key进行加密
		System.out.println("aesKey：【"+aesKey+"】");
		JSONObject jsonObject = testEncryptPrivateKey(aesKey, data, salt, privateKey);
		System.out.println("---------------"+jsonObject);
		String decryptString = testDecryptPublicKey(jsonObject, salt, publicKey);
		System.out.println("---------------"+decryptString);
		
		jsonObject = testEncryptPublicKey(aesKey, data, salt, publicKey);
		System.out.println("---------------"+jsonObject);
		decryptString = testDecryptPrivateKey(jsonObject, salt, privateKey);
		System.out.println("---------------"+decryptString);
		
	}

	/**
	 * 公钥解密测试
	 * @param jsonObject
	 * @param salt
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	private static String testDecryptPublicKey(JSONObject jsonObject,String salt,byte[] publicKey) throws Exception{
		String key = jsonObject.getString("key");
		String content = jsonObject.getString("content");
		String sign = jsonObject.getString("sign");
		
		String aesKey = decryptKeyByPublicKey(key, publicKey);
		String result = "";
		if(!verify(content, sign, salt)){
			System.out.println("=====签名验证失败");
		}else{
			result = decrypt(aesKey, content);
		}
		return result;
	}
	/**
	 * 私钥解密测试
	 * @param jsonObject
	 * @param salt
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	private static String testDecryptPrivateKey(JSONObject jsonObject,String salt,byte[] privateKey) throws Exception{
		String key = jsonObject.getString("key");
		String content = jsonObject.getString("content");
		String sign = jsonObject.getString("sign");
		
		String aesKey = decryptKeyByPrivateKey(key, privateKey);
		String result = "";
		if(!verify(content, sign, salt)){
			System.out.println("=====签名验证失败");
		}else{
			result = decrypt(aesKey, content);
		}
		return result;
	}
	
	/**
	 * 私钥加密测试
	 * @param aesKey aes密钥明文
	 * @param data 待加密数据
	 * @param salt 签名盐
	 * @param privateKey 私钥
	 * @throws Exception
	 */
	private static JSONObject testEncryptPrivateKey(String aesKey, String data, String salt, byte[] privateKey) throws Exception {
		String key = encryptKeyByPrivateKey(aesKey, privateKey);
		System.out.println("key：【"+key+"】");
		String content = encrypt(aesKey, data);
		System.out.println("content：【"+content+"】");
		String sign = digestWithSha256(content, salt);
		System.out.println("sign：【"+sign+"】");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key",  key);
		jsonObject.put("content",  content);
		jsonObject.put("sign",  sign);
		return jsonObject;
	}
	
	/**
	 * 公钥加密测试
	 * @param aesKey aes密钥明文
	 * @param data 待加密数据
	 * @param salt 签名盐
	 * @param publicKey 私钥
	 * @throws Exception
	 */
	private static JSONObject testEncryptPublicKey(String aesKey, String data, String salt, byte[] publicKey) throws Exception {
		String key = encryptKeyByPublicKey(aesKey, publicKey);
		System.out.println("key：【"+key+"】");
		String content = encrypt(aesKey, data);
		System.out.println("content：【"+content+"】");
		String sign = digestWithSha256(content, salt);
		System.out.println("sign：【"+sign+"】");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key",  key);
		jsonObject.put("content",  content);
		jsonObject.put("sign",  sign);
		return jsonObject;
	}
}
