using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Security.Cryptography;
using System.Runtime.InteropServices;
using System;
using System.IO;


public class XGSDKCallbackWrapper : MonoBehaviour
{

	private static XGSDKCallback m_XGSDKCallback;
	
	public static GameObject obj; 

	void Awake()
	{
		m_XGSDKCallback = XGSDKCallback.getInstance ();
	}

	void Start ()
	{
		Debug.Log("XGSDKCallbackWrapper start");
	}

	public static void CreateSDKManager()
	{
		Debug.Log ("CreateSDKManager start");
		if (null == obj) {
			obj = new GameObject("XGSDKCallbackWrapper");
		}
		obj.AddComponent<XGSDKCallbackWrapper>();		
		UnityEngine.Object.DontDestroyOnLoad(obj);
#if UNITY_STANDALONE_WIN
		ChannelTestService.initChannelTestService ();
#endif
		Debug.Log("CreateSDKManager end");

	}


	
	//初始化成功回调

	public void onInitSuccess(string initResult)
	{
		Debug.Log ("onInitSuccess,initResult: " + initResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(initResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		string channelCode = getChannelCode(retTable);

		m_XGSDKCallback.onInitSuccess (code, msg, channelCode);
	}
	
	//初始化失败回调

	public void onInitFail(string initResult)
	{
		Debug.Log ("onInitFail,initResult: " + initResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(initResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		string channelCode = getChannelCode(retTable);
		
		m_XGSDKCallback.onInitFail (code, msg, channelCode);
	}
	
	//登录成功回调

	public void onLoginSuccess(string loginResult)
	{
		Debug.Log("onLoginSuccess,loginResult: " + loginResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(loginResult) as Dictionary<string, object>;
		string authinfo = "";
		if (retTable.ContainsKey ("authInfo")) {
			authinfo = retTable ["authInfo"] as String;
		}
		int code = -1;
		if (retTable.ContainsKey ("code")) {
			code = Convert.ToInt32(retTable ["code"]);
		}

		m_XGSDKCallback.onLoginSuccess (code, authinfo);
	}
	
	//登录失败回调

	public void onLoginFail(string loginResult)
	{
		Debug.Log ("onLoginFail,loginResult: " + loginResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(loginResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		string channelCode = getChannelCode(retTable);

		m_XGSDKCallback.onLoginFail (code,msg,channelCode);
	}

	//登录取消回调

	public void onLoginCancel(string loginResult)
	{
		Debug.Log ("onLoginCancel, loginResult: " + loginResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(loginResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;

		m_XGSDKCallback.onLoginCancel (code, msg);
	}

	//登出成功回调

	public void onLogoutFinish(string logoutResult)
	{
		Debug.Log ("onLogoutFinish,logoutResult: " + logoutResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(logoutResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;

		m_XGSDKCallback.onLogoutFinish (code, msg);
	}
	
	//上传分数成功回调
	public void onGameCenterSubmitScoreSuccess(string submitResult)
	{
		Debug.Log ("onSubmitScoreSuccess, submitResult: " + submitResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(submitResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		
		m_XGSDKCallback.onSubmitScoreSuccess(code, msg);
	}
	
	//上传分数失败回调
	public void onGameCenterSubmitScoreFail(string submitResult)
	{
		Debug.Log ("onSubmitScoreFail, submitResult: " + submitResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(submitResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		
		m_XGSDKCallback.onSubmitScoreFail(code, msg);
	}
	//上传成就成功回调
	public void onGameCenterSubmitAchievementSuccess(string submitResult)
	{
		Debug.Log ("onSubmitAchievementSuccess, submitResult: " + submitResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(submitResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		
		m_XGSDKCallback.onSubmitAchievementSuccess(code, msg);
	}
	//上传成就失败回调
	public void onGameCenterSubmitAchievementFail(string submitResult)
	{
		Debug.Log ("onSubmitAchievementFail, submitResult: " + submitResult);
		Dictionary<string, object> retTable = XGSDKMiniJSON.Json.Deserialize(submitResult) as Dictionary<string, object>;
		int code = Convert.ToInt32(retTable ["code"]);
		string msg = retTable ["msg"] as String;
		
		m_XGSDKCallback.onSubmitAchievementFail(code, msg);
	}
	//支付成功回调

	public void onPaySuccess(string payResult)
	{
		Debug.Log ("onPaySuccess, payResult: " + payResult);

		m_XGSDKCallback.onPaySuccess (payResult);
	}
	
	//支付失败回调

	public void onPayFail(string payResult)
	{
		Debug.Log ("onPayFail, payResult: " + payResult);

		m_XGSDKCallback.onPayFail (payResult);
	}
	
	//支付取消回调

	public void onPayCancel(string payResult)
	{
		Debug.Log ("onPayCancel, payResult: " + payResult);
		
		m_XGSDKCallback.onPayCancel (payResult);
	}
	
	//支付结果未知

	public void onPayOthers(string payResult)
	{
		Debug.Log ("onPayOthers, payResult: " + payResult);
		
		m_XGSDKCallback.onPayOthers (payResult);
	}
	
	//支付过程中回调

	public void onPayProgress(string payResult)
	{
		Debug.Log ("onPayProgress, payResult: " + payResult);
		
		m_XGSDKCallback.onPayProgress (payResult);
	}
	
	//直接退出回调

	public void doExit(string msg)
	{
		Debug.Log ("doExit,msg: " + msg);
		XGSDK2.instance.releaseResource("release source");

		m_XGSDKCallback.doExit (msg);
	}
	
	//使用游戏方退出回调

	public void onNoChannelExiter(string msg)
	{
		Debug.Log ("onNoChannelExiter,msg: " + msg);

		m_XGSDKCallback.onNoChannelExiter (msg);	
	}

	private string getChannelCode(Dictionary<string, object> retTable)
	{
		if(retTable.ContainsKey("channelCode"))
		{
			return retTable["channelCode"] as String;
		}
		return "";
	}

	private Dictionary<string, object> convertToDictionary(string callbackStr){
		Dictionary<string, object> dictMap = new Dictionary<string, object> ();
		dictMap = XGSDKMiniJSON.Json.Deserialize(callbackStr) as Dictionary<string, object>;
		return dictMap;
	}


}