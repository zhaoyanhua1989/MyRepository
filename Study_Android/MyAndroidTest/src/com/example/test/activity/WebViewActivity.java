package com.example.test.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.base.BaseActivity;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.RUtil;

/**
 * 加载WebView，这里案例为加载html5贪吃蛇
 * 
 * @author HKW2962
 *
 */
public class WebViewActivity extends BaseActivity {

	private WebView mWebView;
	private RelativeLayout console; // 手柄充值贪吃蛇方向
	private RelativeLayout touchZone; // 用于手势监听控制贪吃蛇方向
	private TextView upTV;
	private TextView leftTV;
	private TextView rightTV;
	private TextView downTV;
	// 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
	private float x1 = 0;
	private float x2 = 0;
	private float y1 = 0;
	private float y2 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initVariables() {
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.my_activity_webview);
		mWebView = (WebView) findViewById(RUtil.getId(this, "webview_webview"));
		console = (RelativeLayout) findViewById(RUtil.getId(this, "webview_console"));
		touchZone = (RelativeLayout) findViewById(RUtil.getId(this, "webview_touchZone"));
		upTV = (TextView) findViewById(RUtil.getId(this, "webview_up"));
		leftTV = (TextView) findViewById(RUtil.getId(this, "webview_left"));
		rightTV = (TextView) findViewById(RUtil.getId(this, "webview_right"));
		downTV = (TextView) findViewById(RUtil.getId(this, "webview_down"));
		upTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl("javascript:changeDir(0)");
			}
		});
		leftTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl("javascript:changeDir(2)");
			}
		});
		rightTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl("javascript:changeDir(3)");
			}
		});
		downTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl("javascript:changeDir(1)");
			}
		});
		// 设置手势方向滑动监听
		touchZone.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 继承了Activity的onTouchEvent方法，直接监听点击事件
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// 当手指按下的时候
					x1 = event.getX();
					y1 = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// 当手指离开的时候
					x2 = event.getX();
					y2 = event.getY();
					if (y1 - y2 > 50 && ((y1 - y2) > Math.abs(x1 - x2))) {
						mWebView.loadUrl("javascript:changeDir(0)");
					} else if (y2 - y1 > 50 && ((y2 - y1) > Math.abs(x1 - x2))) {
						mWebView.loadUrl("javascript:changeDir(1)");
					} else if (x1 - x2 > 50 && ((x1 - x2) > Math.abs(y1 - y2))) {
						mWebView.loadUrl("javascript:changeDir(2)");
					} else if (x2 - x1 > 50 && ((x2 - x1) > Math.abs(y1 - y2))) {
						mWebView.loadUrl("javascript:changeDir(3)");
					}
				}
				return true;
			}
		});
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void loadData() {
		String url = OverallVariable.WebView.WEB_PAGE_URL_ASSETS_HTML + File.separator + "worm.html";
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 不使用缓存
		mWebView.getSettings().setJavaScriptEnabled(true); // 设置浏览器运行网页的js脚本
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
		mWebView.getSettings().setUseWideViewPort(true); // 将图片调整到适合WebView的大小
		mWebView.getSettings().setLoadWithOverviewMode(true); // 自适应屏幕
		mWebView.getSettings().setSupportZoom(true); // 支持缩放
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局
		mWebView.getSettings().setBuiltInZoomControls(true);
		// 注意如果是跑本地的html，setWebViewClient不起作用，默认在webView中打开网页，不会在外置浏览器打开
		mWebView.setWebViewClient(new WebViewClient() {

			/**
			 * 在每一次请求资源时，都会通过这个函数来回调
			 */
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String request) {
				return super.shouldInterceptRequest(view, request);
			}

			/**
			 * 网页加载时调用
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			/**
			 * 结束加载网页时
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			/**
			 * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			/**
			 * 当接收到https错误时，会回调此函数，在其中可以做错误处理
			 */
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
			}

			/**
			 * 覆盖webView默认通过第三方或者是系统浏览器打开网页的行为,使得网页可以在WebView中打开
			 * 返回值是TRUE的时候控制网页在webview中打开，如果为false的时候则调用系统浏览器或者第三方浏览器打开
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				MyLog.d("url=" + url);
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(url);
		// 使javaScript能调用java的方法，注意第二个参数，已在javaScript那边定义，类似于id作用
		mWebView.addJavascriptInterface(this, "androidFunction");
	}

	@Override
	protected void releaseResource() {
		mWebView = null;
		console = null;
		touchZone = null;
		upTV = null;
		leftTV = null;
		rightTV = null;
		downTV = null;
	}

	/**
	 * javaScript调用的java方法，@JavascriptInterface必不可少
	 */
	@JavascriptInterface
	public void showOrHideConsole() {
		MyLog.d("js call java method, console.getVisibility()=" + console.getVisibility());
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (console.getVisibility() == View.VISIBLE) {
					console.setVisibility(View.GONE);
					touchZone.setVisibility(View.VISIBLE);
				} else {
					console.setVisibility(View.VISIBLE);
					touchZone.setVisibility(View.GONE);
				}
			}
		});
	}
}