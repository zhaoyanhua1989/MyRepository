package com.example.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.util.AppUtil;
import com.example.test.util.HttpUtils;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.RUtil;
import com.example.test.util.StringUtil;
import com.example.test.util.ToastUtil;

public class LoginActivity extends Activity implements OnLayoutChangeListener {

	private RelativeLayout loginDialog; // 登录框，设置动画用
	private TextView nameTitleTv; // 账号title的TextView，设置动画用
	private TextView pwdTitleTv; // 密码title的TextView，设置动画用
	private EditText nameEt; // 账号输入EditText
	private EditText pwdEt; // 密码输入EditText
	private Button loginBt; // 登录按钮
	private Button registerBt; // 注册按钮
	private Button r_confirmBt; // 注册的确定按钮
	private Button r_cancelBt; // 注册的取消按钮
	private LinearLayout getVerificationCodeLl; // 获取验证码模块
	private Button getVCBt; // 获取验证码按钮
	private EditText vcEt; // 验证码输入EditText
	private String numCode; // 记录验证码
	private long timeFlag; // 记录获取验证码的时间，用于校验验证码是否在5分钟之内使用
	private final String HASMSGACTION = "android.provider.Telephony.SMS_RECEIVED";
	private LinearLayout loginLl; // 登录块
	private LinearLayout registerLl; // 注册块
	private String name;
	private String pwd;
	private int keyHeight; // 软键盘弹起后所占高度阀值，监听软键盘拉起和关闭需要用到
	private MessageStatusReceiver receiver; // 处理发送短信成功失败的广播，用于监听注册验证码

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_activity_login);
		// 阀值设置为屏幕高度的1/3
		keyHeight = getWindowManager().getDefaultDisplay().getHeight() / 3;
		initView();
	}

	private void initView() {
		loginDialog = (RelativeLayout) findViewById(RUtil.getId(this, "loginActivity_loginDialog"));
		nameTitleTv = (TextView) findViewById(RUtil.getId(this, "login_nameTileName"));
		pwdTitleTv = (TextView) findViewById(RUtil.getId(this, "login_pwdTileName"));
		nameEt = (EditText) findViewById(RUtil.getId(this, "login_nameEdittext"));
		pwdEt = (EditText) findViewById(RUtil.getId(this, "login_pwdEdittext"));
		loginBt = (Button) findViewById(RUtil.getId(this, "login_logintBT"));
		registerBt = (Button) findViewById(RUtil.getId(this, "login_registerBT"));
		r_confirmBt = (Button) findViewById(RUtil.getId(this, "login_confirmBT"));
		r_cancelBt = (Button) findViewById(RUtil.getId(this, "login_cancelBT"));
		getVerificationCodeLl = (LinearLayout) findViewById(RUtil.getId(this, "loginActivity_LinearLayout3"));
		getVCBt = (Button) findViewById(RUtil.getId(this, "login_getVerificationCodeBT"));
		vcEt = (EditText) findViewById(RUtil.getId(this, "login_verificationCodeEdittext"));
		loginLl = (LinearLayout) findViewById(RUtil.getId(this, "loginActivity_LinearLayout4"));
		registerLl = (LinearLayout) findViewById(RUtil.getId(this, "loginActivity_LinearLayout5"));
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
						if (verifyRegisterNumValid()) {
							// requestRegisterGet();
							requestRegisterPost();
						}
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
		getVCBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getVerificationCode();
			}
		});
		// 设置登录框内容的动画效果，xml设置动画
		Animation anim = AnimationUtils.loadAnimation(this, RUtil.getAnim(this, "my_login_activity_anim2"));
		nameTitleTv.setAnimation(anim);
		pwdTitleTv.setAnimation(anim);
		// 设置登录框动画效果，代码设置动画
		Animation anim2 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim2.setFillAfter(true);
		anim2.setDuration(1000);
		loginBt.setAnimation(anim2);
		registerBt.setAnimation(anim2);
		// 注册短信发送广播(用于发送验证码监听)
		receiver = new MessageStatusReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(OverallVariable.SEND_OK_OR_NOT);
		filter.addAction(OverallVariable.RECEIVE_OK_OR_NOT);
		filter.addAction(HASMSGACTION);
		this.registerReceiver(receiver, filter);
	}

	/**
	 * 获取验证码，如果需要监听是否已经发送短信，则需要用广播来实现监听。这里已实现。
	 */
	private void getVerificationCode() {
		numCode = AppUtil.sendVerificationCode(this, nameEt.getText().toString().trim());
		if (StringUtil.isEmpty(numCode)) {
			setEditTextFocus(nameEt);
			nameEt.setError("账号不是正确的手机号码");
			setAnim1(nameTitleTv);
		}
		// 验证码5分钟内有效，这里记录一下时间
		timeFlag = System.currentTimeMillis();
	}

	/**
	 * 校验验证码是否有用
	 */
	private boolean verifyRegisterNumValid() {
		String mNumCodeStr = vcEt.getText().toString().trim();
		if (StringUtil.isEmpty(mNumCodeStr)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setEditTextFocus(vcEt);
					vcEt.setError("请输入验证码");
				}
			});
			return false;
		}
		// 判断手机号码是否正确
		if (!AppUtil.isMobileNum(nameEt.getText().toString().trim())) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setEditTextFocus(nameEt);
					nameEt.setError("账号不是正确的手机号码");
					setAnim1(nameTitleTv);
				}
			});
			return false;
		}
		// 判断验证码是否正确
		if (!StringUtil.equals(mNumCodeStr, numCode)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setEditTextFocus(vcEt);
					vcEt.setError("验证码错误");
				}
			});
			return false;
		}
		// 如果时间大于5分钟则失效
		long mTimeFlag = System.currentTimeMillis();
		if (mTimeFlag - timeFlag > 5 * 60 * 1000) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	private void requestLoginGet() {
		// 验证非空
		if (!checkNotNull())
			return;
		String uri = OverallVariable.HTTP_REQUEST_LOGINORREGISTER + "?request=login&name=" + name + "&pwd=" + pwd;
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
		String uri = OverallVariable.HTTP_REQUEST_LOGINORREGISTER;
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
					releaseResource();
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
		String uri = OverallVariable.HTTP_REQUEST_LOGINORREGISTER + "request=register&name=" + name + "&pwd=" + pwd;
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
		String uri = OverallVariable.HTTP_REQUEST_LOGINORREGISTER;
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
				getVerificationCodeLl.setVisibility(View.GONE);
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
				getVerificationCodeLl.setVisibility(View.VISIBLE);
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
					setEditTextFocus(nameEt);
					nameEt.setError("请输入账号");
					setAnim1(nameTitleTv);
				}
			});
			return false;
		}
		if (StringUtil.isEmpty(pwd)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setEditTextFocus(pwdEt);
					pwdEt.setError("请输入密码");
					setAnim1(pwdTitleTv);
				}
			});
			return false;
		}
		return true;
	}

	/**
	 * 设置EditText焦点，并使光标移动到指定EditText上
	 * 
	 * @param editText
	 */
	private void setEditTextFocus(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
	}

	/**
	 * 设置动画-淡入淡出，这里只有TextView用到，多次调用抽出来
	 * 
	 * @param textView
	 */
	private void setAnim1(TextView textView) {
		Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, RUtil.getAnim(LoginActivity.this, "my_login_activity_anim1"));
		textView.setAnimation(anim);
	}

	@Override
	public void onBackPressed() {
		if (registerLl.isShown()) {
			showLoginZone();
		} else {
			// 设置退出时，登录框加载的动画。动画加载完成后才执行finish
			Animation anim = AnimationUtils.loadAnimation(this, RUtil.getAnim(this, "my_login_activity_anim3"));
			loginDialog.startAnimation(anim);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					ToastUtil.showCustomToast(LoginActivity.this, "登录取消");
					releaseResource();
					finish();
				}
			});
		}
	}

	/**
	 * 释放资源
	 */
	protected void releaseResource() {
		loginDialog = null;
		nameTitleTv = null;
		pwdTitleTv = null;
		nameEt = null;
		pwdEt = null;
		loginBt = null;
		registerBt = null;
		r_confirmBt = null;
		r_cancelBt = null;
		getVerificationCodeLl = null;
		getVCBt = null;
		vcEt = null;
		numCode = null;
		loginLl = null;
		registerLl = null;
		name = null;
		pwd = null;
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	/************************* 监听软键盘状态没起到作用 ************************/
	/**
	 * TODO 没能正确监听到 软键盘拉起和关闭的监听，实现了OnLayoutChangeListener的抽象方法。
	 * 需要在AndroidManifest中对应的Activity增加
	 * android:windowSoftInputMode="stateAlwaysHidden|adjustResize"
	 */
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
			// 监听到软键盘弹起
			MyLog.d("软键盘拉起");
		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
			// 监听到软件盘关闭
			MyLog.d("软键盘关闭");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 添加layout大小发生改变监听器
		loginDialog.addOnLayoutChangeListener(this);
	}

	/************************* 监听软键盘状态没起到作用 ************************/

	/**
	 * 用于接收短信状态的广播接收器(这里发送短信就是获取验证码)，
	 * 
	 * @author HKW2962
	 *
	 */
	class MessageStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			// 获取电话号码
			MyLog.d("number:" + intent.getStringExtra("number"));
			// 判断action
			String action = intent.getAction();
			if (action.equals(OverallVariable.SEND_OK_OR_NOT)) {
				// 获取结果码
				int code = getResultCode();
				switch (code) {
				case RESULT_OK: // Activity提供
					if (context instanceof Activity) {
						ToastUtil.showCustomToast((Activity) context, "短信成功发送");
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_NO_SERVICE:
				case SmsManager.RESULT_ERROR_NULL_PDU:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					if (context instanceof Activity) {
						ToastUtil.showCustomToast((Activity) context, "短信发送失败");
					}
					break;
				}
			} else if (action.equals(OverallVariable.RECEIVE_OK_OR_NOT)) {
				// 获取结果码
				int code = getResultCode();
				switch (code) {
				case RESULT_OK: // Activity提供
					if (context instanceof Activity) {
						ToastUtil.showCustomToast((Activity) context, "对方成功接收短消息");
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_NO_SERVICE:
				case SmsManager.RESULT_ERROR_NULL_PDU:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					if (context instanceof Activity) {
						ToastUtil.showCustomToast((Activity) context, "对方接收短信失败");
					}
					break;
				}
			} else if (action.equals(HASMSGACTION)) {
				// 短信来了 赶紧接收
				Object[] pdus = (Object[]) intent.getExtras().get("pdus");
				// 遍历pdus 获取每一个pdu
				for (int i = 0; i < pdus.length; i++) {
					// 获取其中的一个pdu
					byte[] bytes = (byte[]) pdus[i];
					// 把bytes数组转换成一条短消息
					SmsMessage msg = SmsMessage.createFromPdu(bytes);
					@SuppressWarnings("unused")
					String number = msg.getDisplayOriginatingAddress();
					String body = msg.getDisplayMessageBody();
					MyLog.d("收到短信，msg=" + body);
					// 可以拦截广告短信，这里检测到关键词，则拒绝接收
					if (body.indexOf("办证") >= 0) {
						// 拦截短消息 阻止有序广播的继续传播
						this.abortBroadcast();
					}
				}
			}
		}
	}
}