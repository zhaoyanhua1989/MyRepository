package com.example.test.util;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

/**
 * 专门处理签名
 * 
 * @author HKW2962
 *
 */
public class SignUtils {

	/**
	 * MD5签名
	 * @param str
	 *            需要签名的String
	 * @return
	 */
	public final static String MD5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = str.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char strs[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strs[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void addParam(List<NameValuePair> params, String name, String value) {
		if (!TextUtils.isEmpty(value)) {
			params.add(new BasicNameValuePair(name, value));
		}
	}

	/**
	 * 获取包含了传入params&签名返回的String，签名方式： HMAC-SHA1 签名
	 * 
	 * @param requestParams
	 *            List<NameValuePair>
	 * @param key
	 *            签名密钥
	 * @return params&sign=sign
	 * @throws Exception
	 */
	public static String generateSignRequestContent(List<NameValuePair> requestParams, String key) throws Exception {
		String requestContent = URLEncodedUtils.format(requestParams, HTTP.UTF_8);
		String sign = getSign(requestParams, key);
		return requestContent + "&sign=" + sign;
	}

	/**
	 * 获取 HMAC-SHA1 签名
	 * 
	 * @param requestParams
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getSign(List<NameValuePair> requestParams, String key) throws Exception {
		Collections.sort(requestParams, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair lhs, NameValuePair rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});

		StringBuilder strSign = new StringBuilder();
		for (int i = 0; i < requestParams.size(); i++) {
			NameValuePair nvPair = requestParams.get(i);
			strSign.append(nvPair.getName()).append("=").append(nvPair.getValue());
			if (i < requestParams.size() - 1) {
				strSign.append("&");
			}
		}
		return SHA1Util.HmacSHA1EncryptByte(strSign.toString(), key);
	}

	public static class SHA1Util {

		private static final String MAC_NAME = "HmacSHA1";
		private static final String ENCODING = "UTF-8";

		/**
		 * 使用 HMAC-SHA1 签名方法对对 encryptText 进行签名
		 * 
		 * @param encryptData
		 *            被签名的字符串
		 * @param encryptKey
		 *            密钥
		 * @return 返回被加密后的字符串
		 * @throws Exception
		 */
		private static byte[] HmacSHA1Encrypt(byte[] encryptData, String encryptKey) throws Exception {
			byte[] data = encryptKey.getBytes(ENCODING);
			SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(secretKey);
			byte[] digest = mac.doFinal(encryptData);
			return digest;
		}

		/**
		 * 使用 HMAC-SHA1 签名方法对对 encryptText 进行签名
		 * 
		 * @param encryptText
		 *            被签名的字符串
		 * @param encryptKey
		 *            密钥
		 * @return 返回被加密后的字符串
		 * @throws Exception
		 */
		public static String HmacSHA1EncryptByte(String encryptText, String encryptKey) throws Exception {
			byte[] text = encryptText.getBytes(ENCODING);
			byte[] digest = HmacSHA1Encrypt(text, encryptKey);
			StringBuilder sBuilder = bytesToHexString(digest);
			return sBuilder.toString();
		}

		/**
		 * 转换成Hex
		 * 
		 * @param bytesArray
		 */
		private static StringBuilder bytesToHexString(byte[] bytesArray) {
			if (bytesArray == null) {
				return null;
			}
			StringBuilder sBuilder = new StringBuilder();
			for (byte b : bytesArray) {
				String hv = String.format("%02x", b);
				sBuilder.append(hv);
			}
			return sBuilder;
		}
	}
}
