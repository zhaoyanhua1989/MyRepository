package com.example.test.util;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {

	public static final int GET = 0;
	public static final int POST = 1;

	/**
	 * 发送http请求的工具方法
	 * 
	 * @param method
	 *            请求方式
	 * @param uri
	 *            请求资源路径
	 * @param pair
	 *            请求中携带的参数 可以是null
	 * @return 响应实体
	 * @throws Exception
	 */
	public static HttpEntity get(int method, String uri, List<BasicNameValuePair> pairs) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		switch (method) {
		case GET:
			HttpGet get = new HttpGet(uri);
			httpResponse = client.execute(get);
			break;
		case POST:
			HttpPost post = new HttpPost(uri);
			if (pairs != null) {
				HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
				// 请求httpRequest的实体
				post.setEntity(entity);
			}
			// 设置post的消息头
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpResponse = client.execute(post);
			break;
		}
		// HttpStatus.SC_OK表示连接成功
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return httpResponse.getEntity();
		} else {
			return null;
		}
	}
}
