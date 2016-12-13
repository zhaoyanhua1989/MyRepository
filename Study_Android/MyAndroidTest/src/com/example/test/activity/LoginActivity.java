package com.example.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.test.R;
import com.example.test.util.HttpUtils;
import com.example.test.util.MyLog;
import com.example.test.util.RUtil;
import com.example.test.util.StringUtil;
import com.example.test.util.ToastUtil;

public class LoginActivity extends Activity {

	private EditText nameEt; // 账号输入EditText
	private EditText pwdEt; // 密码输入EditText
	private Button loginBt; // 登录按钮
	private Button registerBt; // 注册按钮
	private Button r_confirmBt; // 注册的确定按钮
	private Button r_cancelBt; // 注册的取消按钮
	private LinearLayout loginLl; // 登录块
	private LinearLayout registerLl; // 注册块
	private String name;
	private String pwd;

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
		r_confirmBt = (Button) findViewById(RUtil.getId(this, "login_confirmBT"));
		r_cancelBt = (Button) findViewById(RUtil.getId(this, "login_cancelBT"));
		loginLl = (LinearLayout) findViewById(RUtil.getId(this, "loginActivity_LinearLayout3"));
		registerLl = (LinearLayout) findViewById(RUtil.getId(this, "loginActivity_LinearLayout4"));
		loginBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// requestLoginGet();
						requestLoginPost();
					}
				}).start();
			}
		});
		registerBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showRegisterZone();
			}
		});
		r_confirmBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// requestRegisterGet();
						requestRegisterPost();
					}
				}).start();
			}
		});
		r_cancelBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLoginZone();
			}
		});
	}

	@SuppressWarnings("unused")
	private void requestLoginGet() {
		// 验证非空
		if (!checkNotNull())
			return;
		String uri = "http://10.20.72.80:8080/2.5_web_jsp/requestLogin.jsp?request=login&name=" + name + "&pwd=" + pwd;
		try {
			HttpEntity entity = HttpUtils.get(HttpUtils.GET, uri, null);
			// 获取响应文本 json字符串
			if (entity != null) {
				dealLoginResult(EntityUtils.toString(entity));
			} else {
				ToastUtil.showCustomToast(this, "服务端未启动...");
			}
		} catch (Exception e) {
			MyLog.e("requestLoginGet exception, exception=" + e.getMessage());
			ToastUtil.showCustomToast(this, "服务端未启动或存在异常...");
		}
	}

	private void requestLoginPost() {
		// 验证非空
		if (!checkNotNull())
			return;
		// HttpPost连接对象
		String uri = "http://10.20.72.80:8080/2.5_web_jsp/requestLogin.jsp?";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("pwd", pwd));
		params.add(new BasicNameValuePair("request", "login"));
		try {
			HttpEntity entity = HttpUtils.get(HttpUtils.POST, uri, params);
			if (entity != null) {
				// 取得返回的字符串
				dealLoginResult(EntityUtils.toString(entity));
			} else {
				ToastUtil.showCustomToast(this, "服务端未启动...");
			}
		} catch (Exception e) {
			MyLog.e("requestLoginPost exception, exception=" + e.getMessage());
			ToastUtil.showCustomToast(this, "服务端未启动或存在异常...");
		}
	}

	// 处理登录结果
	private void dealLoginResult(String resutlJson) {
		boolean result = false;
		try {
			MyLog.i("json:" + resutlJson);
			JSONObject obj = new JSONObject(resutlJson);
			result = obj.getBoolean("result");
		} catch (Exception e) {
			MyLog.e(e.getMessage());
		}
		if (result) {
			ToastUtil.showCustomToast(this, "登录成功");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
		} else {
			ToastUtil.showCustomToast(this, "账号或密码错误");
		}
	}

	@SuppressWarnings("unused")
	private void requestRegisterGet() {
		// 验证非空
		if (!checkNotNull())
			return;
		String uri = "http://10.20.72.80:8080/2.5_web_jsp/requestLogin.jsp?request=register&name=" + name + "&pwd=" + pwd;
		try {
			HttpEntity entity = HttpUtils.get(HttpUtils.GET, uri, null);
			if (entity != null) {
				dealRegisterResult(EntityUtils.toString(entity));
			} else {
				ToastUtil.showCustomToast(this, "服务端未启动...");
			}
		} catch (Exception e) {
			MyLog.e("requestRegisterGet exception, exception=" + e.getMessage());
			ToastUtil.showCustomToast(this, "服务端未启动或存在异常...");
		}

	}

	private void requestRegisterPost() {
		// 验证非空
		if (!checkNotNull())
			return;
		// HttpPost连接对象
		String uri = "http://10.20.72.80:8080/2.5_web_jsp/requestLogin.jsp?";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("pwd", pwd));
		params.add(new BasicNameValuePair("request", "register"));
		try {
			HttpEntity entity = HttpUtils.get(HttpUtils.POST, uri, params);
			if (entity != null) {
				dealRegisterResult(EntityUtils.toString(entity));
			} else {
				ToastUtil.showCustomToast(this, "服务端未启动...");
			}
		} catch (Exception e) {
			MyLog.e("requestRegisterPost exception, exception=" + e.getMessage());
			ToastUtil.showCustomToast(this, "服务端未启动或存在异常...");
		}
	}

	// 处理注册的结果
	private void dealRegisterResult(String resutlJson) {
		int result = -1;
		JSONObject obj = null;
		try {
			MyLog.i("json:" + resutlJson);
			obj = new JSONObject(resutlJson);
			result = obj.getInt("result");
		} catch (Exception e) {
			MyLog.e(e.getMessage());
		}
		if (result == -1) {
			try {
				String insertException = obj.getString("exception");
				if (insertException.startsWith("Duplicate entry")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							nameEt.requestFocus(); // 使获取焦点
							nameEt.setError("该账号已被注册");
						}
					});
				}
			} catch (JSONException e) {
				MyLog.e(e.getMessage());
			}
		} else if (result == 0) {
			ToastUtil.showCustomToast(this, "未知错误，请重试");
		} else {
			ToastUtil.showCustomToast(this, "注册成功");
			// 返回登录
			showLoginZone();
			// 直接登录
			requestLoginPost();
		}
	}

	// 显示登录界面
	private void showLoginZone() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				registerLl.setVisibility(View.GONE);
				loginLl.setVisibility(View.VISIBLE);
			}
		});
	}

	// 显示注册界面
	private void showRegisterZone() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				registerLl.setVisibility(View.VISIBLE);
				loginLl.setVisibility(View.GONE);
			}
		});
	}

	private boolean checkNotNull() {
		name = nameEt.getText().toString().trim();
		pwd = pwdEt.getText().toString().trim();
		if (StringUtil.isEmpty(name)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					nameEt.setError("请输入账号");
				}
			});
			return false;
		}
		if (StringUtil.isEmpty(pwd)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pwdEt.setError("请输入密码");
				}
			});
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if (registerLl.isShown()) {
			showLoginZone();
		} else {
			ToastUtil.showCustomToast(this, "登录取消");
			finish();
		}
	}
}
