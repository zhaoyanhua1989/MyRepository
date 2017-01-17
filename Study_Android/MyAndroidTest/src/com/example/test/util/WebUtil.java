package com.example.test.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.example.test.model.AlertDialogService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebUtil {
	
	private static final String TAG = WebUtil.class.getSimpleName();
	private static final String WEB_PAGE_URL_ERROR_PAGE = "errorPage.html";
	public static int MUI_TYPE = -1;
	private static ArrayList<String> buffer = null;

	public static String getJS(String method, String params) {
		return getJS(method, new String[] { params });
	}

	public static String getJS(String method) {
		return getJS(method, new String[] {});
	}

	public static String getJS(String method, boolean enable) {
		String call = OverallVariable.WebView.CALL_JS_HEAD + method + "(" + enable + ");";
		MyLog.info(TAG, "getJS", call);
		return call;
	}

	public static String getJS(String method, String[] params) {
		String call = OverallVariable.WebView.CALL_JS_HEAD + method + "(";
		if (null != params && params.length > 0) {
			call += getParams(params[0]);
			for (int i = 1; i < params.length; i++) {
				if (TextUtils.isEmpty(params[i]))
					continue;
				call += "," + getParams(params[i]);
			}
		}
		call += ");";
		MyLog.info(TAG, "getJS", call);
		return call;
	}

	/**
	 * 判斷object類型..
	 */
	private static String getParams(String param) {
		if (param.equals("true") || param.equals("false")) {
			return param;
		} else {
			return "\"" + param + "\"";
		}
	}

	/**
	 * 载入Web
	 * 
	 * @param mActivity
	 * @param webWindowPage
	 * @param url
	 *            当无带file:///android_asset 或 http:// 时直接带上头 file:///android_asset 或 http://
	 *            方可直接load
	 * @param mWebViewClient
	 *            事件触发
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	public static void loadWeb(final Activity mActivity, final WebView webWindowPage, String url, final WebPageEvent mWebViewClient) {
		url = getCompleteUrl(url);
		url = url + "#" + getCssUiType();
		final String aUrl = url;
		webWindowPage.post(new Runnable() {
			public void run() {
				WebSettings webSettings = webWindowPage.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
				webSettings.setDatabaseEnabled(true);
				webSettings.setDomStorageEnabled(true);
				webSettings.setAppCacheEnabled(true);
				webSettings.setBuiltInZoomControls(false);
				webSettings.setSupportZoom(false);
				webSettings.setSaveFormData(true);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					webSettings.setDisplayZoomControls(false);
				}

				// webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
				webWindowPage.setBackgroundColor(Color.TRANSPARENT);
				webWindowPage.setScrollbarFadingEnabled(true);
				webWindowPage.loadUrl(aUrl);
				webWindowPage.setWebViewClient(new WebPageEvent(mWebViewClient.getTimeOut()) {
					int count = 0;
					@Override
					public void onTimeOut() {
						MyLog.debug(TAG, "loadWeb", "web is time out.");
						AlertDialogService.hideLoading(mActivity);
						count++;
						webWindowPage.loadUrl(getCompleteUrl(WEB_PAGE_URL_ERROR_PAGE));
						mWebViewClient.onTimeOut();
					}

					@Override
					public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
						AlertDialogService.hideLoading(mActivity);
						if (count > OverallVariable.WebView.MAX_RETRY_LOAD_WEB) {
							ToastUtil.showCustomToast(mActivity, "响应超时...");
							mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
							return;
						}
						count++;
						webWindowPage.loadUrl(getCompleteUrl(WEB_PAGE_URL_ERROR_PAGE));
					}

					@Override
					public void onPageFinished(WebView webView, String url) {
						super.onPageFinished(webView, aUrl);
						AlertDialogService.hideLoading(mActivity);
						mWebViewClient.onPageFinished(webView, url);
					}

					@Override
					public boolean shouldOverrideUrlLoading(WebView webView, String url) {
						MyLog.debug(TAG, "loadWeb", url);
						// 如果包含header，则判断里面的参数是否正常，然后再加载后面的操作
						if (url.indexOf(OverallVariable.WebView.WEB_INTERFACE_HEADER) > -1 && WebUtil.getMethodName(url).equals(OverallVariable.WebView.WEB_GLOBAL_INTERFACE_RELOAD)) {
							// if (url.indexOf(OverallVariable.WebView.WEB_GLOBAL_INTERFACE_IS_IN_SDK) > -1) {
							// loadOnThread(webView, getJS(OverallVariable.WebView.JS_METHOD_SET_IN_SDK_TAG, true));
							// return true;
							// }
							AlertDialogService.showLoading(mActivity, "WAITING......", true);
							count = 0;
							webView.loadUrl(aUrl);
							return true;
						}
						mWebViewClient.shouldOverrideUrlLoading(webView, url);
						return true;
					}

				});

				webWindowPage.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						if (newProgress >= 100) {
							WebUtil.runBufferJS(view);
						}
						super.onProgressChanged(view, newProgress);
					}

					@Override
					public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
						builder.setMessage(message).setPositiveButton("确定", null);
						// 不需要绑定按键事件
						// 屏蔽keycode等于84之类的按键
						builder.setOnKeyListener(new OnKeyListener() {
							public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
								return true;
							}
						});
						// 禁止响应按back键的事件
						builder.setCancelable(false);
						AlertDialog dialog = builder.create();
						dialog.show();
						result.confirm();
						return true;
					}

					@Override
					public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
						return super.onJsConfirm(view, url, message, result);
					}
				});
			}
		});
	}

	/**
	 * 设置UI的背景样式(颜色)
	 * 
	 * @param type
	 *            like {UI_TYPE_CSS_WHITE_CODE}
	 */
	public static void setCssUiType(int type) {
		MUI_TYPE = type;
	}

	/**
	 * 获取UI的背景样式(颜色)
	 * 
	 * @return
	 */
	private static String getCssUiType() {
		String uiType = "";
		switch (MUI_TYPE) {
		case OverallVariable.WebView.UI_TYPE_CSS_WHITE_CODE:
			uiType = OverallVariable.WebView.UI_TYPE_CSS_WHITE;
			break;
		case OverallVariable.WebView.UI_TYPE_CSS_BLACK_CODE:
			uiType = OverallVariable.WebView.UI_TYPE_CSS_BLACK;
			break;
		case OverallVariable.WebView.UI_TYPE_CSS_BLUE_CODE:
			uiType = OverallVariable.WebView.UI_TYPE_CSS_BLUE;
			break;
		case OverallVariable.WebView.UI_TYPE_CSS_RED_CODE:
			uiType = OverallVariable.WebView.UI_TYPE_CSS_RED;
			break;
		default:
			uiType = OverallVariable.WebView.UI_TYPE_CSS_WHITE;
			break;
		}
		return uiType;
	}

	abstract static public class WebPageEvent extends WebViewClient {
		private int outTimes = 20000;
		private Handler mHandler;
		private Runnable run = null;

		/**
		 * @param outTimes
		 *            多少ms超时, 预设为20000ms
		 */
		public WebPageEvent(int outTimes) {
			if (outTimes > 0)
				this.outTimes = outTimes;
		}

		public WebPageEvent() {

		}

		public int getTimeOut() {
			return outTimes;
		}

		public void onTimeOut() {

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mHandler = new Handler();
			run = new Runnable() {
				@Override
				public void run() {
					onTimeOut();
				}
			};
			mHandler.postDelayed(run, outTimes);
		}

		@Override
		public void onPageFinished(WebView webView, String url) {
			super.onPageFinished(webView, url);
			if (mHandler != null) {
				mHandler.removeCallbacks(run);
			}
		}

		abstract public boolean shouldOverrideUrlLoading(WebView webView, String url);
	}

	public static void putJStoBuffer(String js) {
		if (null == buffer) {
			buffer = new ArrayList<String>();
		}
		buffer.add(js);
	}

	public static void runBufferJS(final WebView webView) {
		if (null == buffer)
			return;

		for (int i = 0; i < buffer.size(); i++) {
			loadOnThread(webView, buffer.get(i));
		}

		buffer.clear();
	}

	public static void clearJS() {
		if (null != buffer)
			buffer.clear();
	}

	public static void loadOnThread(final WebView webView, final String text) {
		if (null == webView || null == text) {
			return;
		}
		webView.post(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(text);
			}
		});
	}

	public static void loadOnThread(boolean isLoaded, WebView webView, String text) {
		if (isLoaded) {
			loadOnThread(webView, text);
		} else {
			putJStoBuffer(text);
		}
	}

	private static String getCompleteUrl(String url) {
		if (TextUtils.isEmpty(url))
			return "";
		// 有http 返回原url
		if (url.indexOf("http://") != -1 || url.indexOf("https://") != -1) {
			return url;
		}
		if (url.indexOf(OverallVariable.WebView.WEB_PAGE_URL_ASSETS_HTML) == -1) {
			url = OverallVariable.WebView.WEB_PAGE_URL_ASSETS_HTML + File.separator + url;
		}
		return url;
	}

	/**
	 * 解析方法名
	 * 
	 * @param url
	 *            header://login/?account=testAccount&pwd=textPwd
	 * @return login
	 */
	public static String getMethodName(String url) {
		String methodName = "";
		url = url.replace(OverallVariable.WebView.WEB_INTERFACE_HEADER, "");
		if (url.indexOf("/") > -1)
			methodName = url.substring(0, url.indexOf("/"));
		return methodName;
	}

	/**
	 * 解析参数
	 * 
	 * @param url
	 *            header://login/?account=testAccount&pwd=textPwd
	 * @return
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static HashMap<String, String> getUrlParams(String url) {
		HashMap<String, String> params = new HashMap<String, String>();
		// 有参数
		if (url.indexOf("/?") > -1) {
			url = url.replace(" ", "%20");
			try {
				List<NameValuePair> parameters = URLEncodedUtils.parse(new URI(url), "UTF-8");
				for (NameValuePair p : parameters) {
					if (!TextUtils.isEmpty(p.getValue()))
						params.put(p.getName(), p.getValue().trim());
				}
			} catch (URISyntaxException e) {
				MyLog.error(TAG, "getUrlParams", "getUrlParams error", e);
			}
		}
		return params;
	}
}
