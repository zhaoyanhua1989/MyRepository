package com.example.test.activity;

import java.util.ArrayList;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.test.R;
import com.example.test.util.MyLog;
import com.example.test.util.RUtil;
import com.example.test.util.StringUtil;
import com.example.test.util.ToastUtil;

public class LoginActivity extends Activity {

	private EditText nameEt;
	private EditText pwdEt;
	private Button loginBt;
	@SuppressWarnings("unused")
	private Button registerBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		nameEt = (EditText) findViewById(RUtil.getId(this, "login_nameEdittext"));
		pwdEt = (EditText) findViewById(RUtil.getId(this, "login_pwdEdittext"));
		loginBt = (Button) findViewById(RUtil.getId(this, "login_logintBT"));
		registerBt = (Button) findViewById(RUtil.getId(this, "login_registerBT"));
		loginBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.d("点我....");
				new Thread(new Runnable() {
					@Override
					public void run() {
						requestGet();
						// requestPost();
					}
				}).start();
			}
		});
	}

	private void requestGet() {
		String name = nameEt.getText().toString().trim();
		String pwd = pwdEt.getText().toString().trim();
		// 验证非空
		if(!checkNotNull(name, pwd))
			return;
		HttpClient client = new DefaultHttpClient();
		String uri = "http://10.20.72.80:8080/2.5_web_jsp/requestLogin.jsp?name=" + name + "&pwd=" + pwd;
		MyLog.i("uri= " + uri);
		HttpGet get = new HttpGet(uri);
		HttpResponse resp;
		boolean reslut = false;
		try {
			resp = client.execute(get);
			// 获取响应文本 json字符串
			String json = EntityUtils.toString(resp.getEntity());
			MyLog.i("json: " + json);
			JSONObject obj = new JSONObject(json);
			reslut = obj.getBoolean("result");
		} catch (Exception e) {
			MyLog.e(e.getMessage());
		}
		if(reslut) {
			finish();
		} else {
			ToastUtil.showToast(this, "账号或密码错误");
		}
	}

	private boolean checkNotNull(String name, String pwd) {
		if(StringUtil.isEmpty(name)) {
			nameEt.setError("请输入账号");
			return false;
		}
		if(StringUtil.isEmpty(pwd)) {
			pwdEt.setError("请输入密码");
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	private void requestPost() {
		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost("http://localhost:8080/2.5_web_jsp/requestLogin.jsp?");
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("name", "aaa"));
		params.add(new BasicNameValuePair("pwd", "111"));
		HttpEntity httpentity = null;
		try {
			httpentity = new UrlEncodedFormEntity(params, "UTF-8");
			// 请求httpRequest的实体
			httpRequest.setEntity(httpentity);
			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// HttpStatus.SC_OK表示连接成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
