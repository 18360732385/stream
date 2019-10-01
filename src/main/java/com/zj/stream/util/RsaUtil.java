package com.zj.stream.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {
	//算法
	private static final String ALGORITHM = "RSA";
	//摘要算法
	private static final String MD5WITHRSA = "MD5withRSA";
	//摘要算法
	private static final String SHA1WITHRSA = "SHA1withRSA";
	//私钥key
	private static final String PRIVATEKEY = "privateKey";
	//公钥key
	private static final String PUBLICKEY = "publicKey";
	//加解密填充方式
	public static final String TRANSFORMATION = "RSA";
	
	static{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * 生成密钥对
	 * @param keySize 密钥对，目前可选值：512、1024、2048、3072、4096等
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> genKeyPair(int keySize) throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(PRIVATEKEY, privateKey);
		result.put(PUBLICKEY, publicKey);
		return result;
	}
	
	/**
	 * 将Base64转换的私钥密钥字符串转换为私钥对象
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception{
		byte[] privateKeys = Base64.decodeBase64(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeys);
		return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	}
	
	/**
	 * 将Base64转换的公钥字符串转换为公钥对象
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception{
		byte[] publicKeys = Base64.decodeBase64(publicKey);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeys);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return keyFactory.generatePublic(x509EncodedKeySpec);
	}
	
	/**
	 * 产生SHA1withRSA签名信息
	 * @param plainText
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] signSha1WithRsa(byte[] plainText,PrivateKey privateKey) throws Exception{
		return sign(plainText, SHA1WITHRSA, privateKey);
	}
	
	/**
	 * 验证SHA1withRSA签名信息
	 * @param signnature
	 * @param plainText
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean verfySha1WithRsa(byte[] signnature,byte[] plainText,PublicKey publicKey) throws Exception{
		return verfy(signnature, plainText, SHA1WITHRSA, publicKey);
	}
	
	
	/**
	 * 产生MD5withRSA签名信息
	 * @param plainText
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] signMd5WithRsa(byte[] plainText,PrivateKey privateKey) throws Exception{
		return sign(plainText, MD5WITHRSA, privateKey);
	}
	
	/**
	 * 验证MD5withRSA签名信息
	 * @param signnature
	 * @param plainText
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean verfyMd5WithRsa(byte[] signnature,byte[] plainText,PublicKey publicKey) throws Exception{
		return verfy(signnature, plainText, MD5WITHRSA, publicKey);
	}
	
	/**
	 * 产生RSA签名信息（指定签名方法）
	 * @param plainText
	 * @param signMethod
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	private static byte[] sign(byte[] plainText,String signMethod,PrivateKey privateKey) throws Exception{
		Signature signature = Signature.getInstance(signMethod,new BouncyCastleProvider());
		signature.initSign(privateKey);
		signature.update(plainText);
		return signature.sign();
	}
	
	/**
	 * 验证RSA签名信息（指定签名方法）
	 * @param signatures
	 * @param plainText
	 * @param signMethod
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	private static boolean verfy(byte[] signatures,byte[] plainText,String signMethod,PublicKey publicKey) throws Exception{
		Signature signature = Signature.getInstance(signMethod);
		signature.initVerify(publicKey);
		signature.update(plainText);
		return signature.verify(signatures);
	}
	
	/**
	 * 获取私钥Key
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	private static Key getPrivateKey(byte[] privateKey) throws Exception{
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
		return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	}
	
	/**
	 * 获取公钥Key
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	private static Key getPublicKey(byte[] publicKey) throws Exception{
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
		return keyFactory.generatePublic(x509EncodedKeySpec);
	}
	
	/**
	 * cipher
	 * @param key 加解密密钥
	 * @param model  加解密模式
	 * @param transformation
	 * @param data  加解密数据
	 * @return
	 * @throws Exception
	 */
	private static byte[] cipher(Key key,int model,String transformation,byte[] data) throws Exception{
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(model, key);
		return cipher.doFinal(data);
	}
	
	/**
	 * rsa私钥解密
	 * @param encryptedData base64字符串
	 * @param privateKey
	 * @param transformation
	 * @return 返回解密明文
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encryptedData,byte[] privateKey,String transformation) throws Exception{
		Key key = getPrivateKey(privateKey);
		return new String(cipher(key, Cipher.DECRYPT_MODE, transformation, Base64.decodeBase64(encryptedData)));
    }
	
	/**
	 * rsa公钥解密
	 * @param encryptedData  base64字符串
	 * @param publicKey
	 * @param transformation
	 * @return 返回解密明文
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String encryptedData,byte[] publicKey,String transformation) throws Exception{
		Key key = getPublicKey(publicKey);
		return new String(cipher(key, Cipher.DECRYPT_MODE, transformation, Base64.decodeBase64(encryptedData)));
	}
	
	/**
	 * 私钥加密
	 * @param decryptedData 明文字符串
	 * @param privateKey
	 * @param transformation
	 * @return  返回加密base64字符串
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String decryptedData,byte[] privateKey,String transformation) throws Exception{
		Key key = getPrivateKey(privateKey);
		return Base64.encodeBase64String(cipher(key, Cipher.ENCRYPT_MODE, transformation, decryptedData.getBytes()));
	}
	
	/**
	 * 公钥加密
	 * @param decryptedData 明文字符串
	 * @param publicKey
	 * @param transformation
	 * @return 返回加密base64字符串
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String decryptedData,byte[] publicKey,String transformation) throws Exception{
		Key key = getPublicKey(publicKey);
		return Base64.encodeBase64String(cipher(key, Cipher.ENCRYPT_MODE, transformation, decryptedData.getBytes()));
	}

	
	public static void main(String[] args) throws Exception {
		//秘钥对可以在linux服务器上生成
		String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI3NEl9g6ibmCS1ORpNmyEg9Kbk37NEZJWvp5Z0tpO54W7BIds7/l6BPod2q0hdNPEPgnVClSyLs6ryPdYLsJqPGtmfId2YTWHwex6Uy6Ivk3UUPeTVVL+/GNg+1glbsVrKmCOGCKMXBaX6zPIG2+WLfC9LWsFgVG57095MvtGxJAgMBAAECgYBdGUr6vBJ/v4A+8ql7lXvhkeaW6JTfI/dhxosuiw1CVFs+fhUjCsRuSFopw0F0cw0iM5KVpDCUmZ/0dOveLVWgLemebb5sMPTEwvVTEsamY4ku/1FcwOVw3wxjXguRcbgLzsri6iHlbiOblNoZaazw64obEZ6jX+zB9oWomssCUQJBANRjDxbNqSoEeqaB1MzcvQQPPybwpDh1PCX3cEG/l6VG2gNTO44DwqMle13zpVc7lGxQikzpHBLzHNKZw70TuvMCQQCq62SMpsBp2594KkRkRlhrk3zThGzs1XDMBrrOUfgl8wqAERh3PtYLMmps4fd1PcFbLUlHD5+W+B1mUDPsEtLTAkEAwm4c9ic4YfrPvXbFtPWvI/RBQAi0jerlMWygG9ClpuyB0OF1d8EBghFiKtRN3NnyOmZQ9a/Bv6dID5QsmP9i+QJAdE+XrzdSvTbdgHKS9AIC7cICMhZt4YUmK1FxEjIpwflwbdI0agFyu0/lqI7lTP1ndVqOATOakKvrpdJyYvY0TQJAGjp9n8rpaB0nN8e3MMq5I2msJhKvkQR5fzw+MvARXJxxpdA6Ms/d+IGLu2B6JjEYXhXnbtiQHHdd3HkTN8p/1w==";
		String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNzRJfYOom5gktTkaTZshIPSm5N+zRGSVr6eWdLaTueFuwSHbO/5egT6HdqtIXTTxD4J1QpUsi7Oq8j3WC7CajxrZnyHdmE1h8HselMuiL5N1FD3k1VS/vxjYPtYJW7FaypgjhgijFwWl+szyBtvli3wvS1rBYFRue9PeTL7RsSQIDAQAB";

		int keySize = 4096;
		Map<String,Object> keys = genKeyPair(keySize);
		PrivateKey privateKeyTemp = (PrivateKey) keys.get(PRIVATEKEY);
		PublicKey publicKeyTemp = (PublicKey) keys.get(PUBLICKEY);
		
		privateKeyBase64 = Base64.encodeBase64String(privateKeyTemp.getEncoded());
		publicKeyBase64 = Base64.encodeBase64String(publicKeyTemp.getEncoded());
		System.out.println("私钥："+privateKeyBase64);
		System.out.println("公钥："+publicKeyBase64);


		//sha签名校验算法
		PrivateKey privateKey = getPrivateKey(privateKeyBase64);
		PublicKey publicKey = getPublicKey(publicKeyBase64);

		JSONObject signData = new JSONObject();
		signData.put("cardNo", "728437827348274382");
		signData.put("name", "李四");
		signData.put("mobileNo", "19999999999");
		signData.put("userId", "1234123132");
		String oldData = signData.toJSONString();

		oldData = "{\"agreemtSts\": \"00\"}";
		System.out.println("原始数据："+oldData);

		byte[] plainText = oldData.getBytes();
		System.out.println("原始数据base64："+Base64.encodeBase64String(plainText));

		byte[] signnature = signSha1WithRsa(plainText, privateKey);
		System.out.println("签名数据base64："+Base64.encodeBase64String(signnature));

		boolean bool = verfySha1WithRsa(signnature, plainText, publicKey);
		System.out.println("签名验证结果："+bool);

		System.out.println("=================================");

		//RSA加解密算法
		String transformation = "RSA";
		String decryptedData = "小明";

		System.out.println("数据原文："+decryptedData);

		String privateEncryptResult = encryptByPrivateKey(decryptedData, Base64.decodeBase64(privateKeyBase64), transformation);
		String publicEncryptResult = encryptByPublicKey(decryptedData, Base64.decodeBase64(publicKeyBase64), transformation);

		System.out.println("私钥加密结果="+privateEncryptResult);
		System.out.println("公钥加密结果="+publicEncryptResult);

		String privateDecryptResult = decryptByPrivateKey(publicEncryptResult, Base64.decodeBase64(privateKeyBase64), transformation);
		String publicDecryptResult = decryptByPublicKey(privateEncryptResult, Base64.decodeBase64(publicKeyBase64), transformation);
		System.out.println("私钥解密="+privateDecryptResult);
		System.out.println("公钥解密="+publicDecryptResult);
	}
}