using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Security.Cryptography;
using System.Runtime.InteropServices;
using System;
using System.IO;
using ChannelUtils;

public class XGSDKCallbackImpl : XGSDKCallback
{

	public XGSDKCallbackImpl(){}

	//初始化成功回调

	public override void onInitSuccess(int code, string msg, string channelCode)
	{
		string tip = "initSuccess,code:" + code + " msg:" + msg + " channelCode:" + channelCode;
		ChannelUtils.DialogHelper.getInstance().showCallbackTip(tip);		
		//xgsdkShare.XGShareWeChatinit("wxcef4d353c6e2d062");
		//xgsdkShare.XGShareWeiboinit("2644168118");
	}
	
	//初始化失败回调

	public override void onInitFail(int code, string msg, string channelCode)
	{
		string tip = "initFail,code:" + code + " msg:" + msg + " channelCode:" + channelCode;		
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
	}
	
	//登录成功回调

	public override void onLoginSuccess(int code, string authInfo)
	{
		byte[] outputb = GetDecoded(authInfo);
		string info = Encoding.Default.GetString(outputb);
		Dictionary<string, object> authinfoJson = XGSDKMiniJSON.Json.Deserialize(info) as Dictionary<string, object>;
		if(authinfoJson.ContainsKey("uid"))//some channel no uid return
		{
			Pay.uid = authinfoJson ["uid"] as String;
		}else{
			Pay.uid = "";
		}
		
		if(authinfoJson.ContainsKey("channelId"))//some channel no uid return
		{
			Pay.channelId = authinfoJson ["channelId"] as String;
		}else{
			Pay.channelId = "";
		}
		
		if(authinfoJson.ContainsKey("planId"))//some channel no uid return
		{
			Pay.planId = authinfoJson ["planId"] as String;
		}else{
			Pay.planId = "";
		}
				
		AuthService.VerifySesssion.getInstance ().verify (authInfo);

		#if UNITY_STANDALONE_WIN
		StartGame.verifiedInfo =  authInfo;
		Application.LoadLevel("loginSuccess");
		#endif
	}
	
	//登录失败回调

	public override void onLoginFail(int code, string msg,string channelCode)
	{

		string tip = "loginFail,code:" + code + " message:" + msg + " channelCode:" + channelCode;
		#if UNITY_STANDALONE_WIN
			LoginGame.logoutFinishTip = tip;
			Application.LoadLevel("login");
		#elif UNITY_ANDROID
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#endif
	}

	//登录取消回调

	public override void onLoginCancel(int code, string msg)
	{
		string tip = "loginCancel,code:" + code + " msg:" + msg;
		#if UNITY_STANDALONE_WIN
			LoginGame.logoutFinishTip = tip;
			Application.LoadLevel("login");
		#else
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#endif
	}


	//登出成功回调

	public override void onLogoutFinish(int code, string msg)
	{
		string tip = "logoutFinish,code:" + code + " msg:" + msg;
		LoginGame.logoutFinishTip = tip;
		Application.LoadLevel ("login");
	}
	

	
	
	//支付成功回调

	public override void onPaySuccess(string payResult)
	{
		string tip = "paySuccess,payResult: " + payResult;
		#if UNITY_STANDALONE_WIN
			//Pay.payInfo = tip;
			//Application.LoadLevel("pay");
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#else
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#endif
	}
	
	//支付失败回调

	public override void onPayFail(string payResult)
	{
		string tip = "payFail,payResult: " + payResult;
		#if UNITY_STANDALONE_WIN
			//Pay.payInfo = tip;
			//Application.LoadLevel("pay");
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#else
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#endif

	}
	
	//支付取消回调

	public override void onPayCancel(string payResult)
	{
		string tip = "payCancel,payResult: " + payResult;
		#if UNITY_STANDALONE_WIN
			//Pay.payInfo = tip;
			//Application.LoadLevel("pay");
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#else
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
		#endif
	}
	
	//支付结果未知

	public override void onPayOthers(string payResult)
	{
		string tip = "payOthers,payResult: " + payResult;
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("PayOthers, message:" + payResult);
	}
	
	//支付过程中回调

	public override void onPayProgress(string payResult)
	{
		string tip = "payProgress,payResult: " + payResult;
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
	}
	
	//直接退出回调

	public override void doExit(string msg)
	{
		Application.Quit();
	}
	
	//使用游戏方退出回调

	public override void onNoChannelExiter(string msg)
	{
		ChannelUtils.DialogHelper.getInstance().showQuitConfirmDialog();		
	}


	char[] source;
	int length, length2, length3;
	int blockCount;
	int paddingCount;
	
	private void init(char[] input)
	{
		int temp = 0;
		source = input;
		length = input.Length;
		
		for (int x = 0; x < 2; x++)
		{
			if (input [length - x - 1] == '=')
				temp++;
		}
		paddingCount = temp;
		
		blockCount = length / 4;
		length2 = blockCount * 3;
	}
	public byte[] GetDecoded(string strInput)
	{
		init(strInput.ToCharArray());
		
		byte[] buffer = new byte[length];
		byte[] buffer2 = new byte[length2];
		
		for (int x = 0; x < length; x++)
		{
			buffer [x] = char2sixbit(source [x]);
		}
		
		byte b, b1, b2, b3;
		byte temp1, temp2, temp3, temp4;
		
		for (int x = 0; x < blockCount; x++)
		{
			temp1 = buffer [x * 4];
			temp2 = buffer [x * 4 + 1];
			temp3 = buffer [x * 4 + 2];
			temp4 = buffer [x * 4 + 3];
			
			b = (byte)(temp1 << 2);
			b1 = (byte)((temp2 & 48) >> 4);
			b1 += b;
			
			b = (byte)((temp2 & 15) << 4);
			b2 = (byte)((temp3 & 60) >> 2);
			b2 += b;
			
			b = (byte)((temp3 & 3) << 6);
			b3 = temp4;
			b3 += b;
			
			buffer2 [x * 3] = b1;
			buffer2 [x * 3 + 1] = b2;
			buffer2 [x * 3 + 2] = b3;
		}
		
		length3 = length2 - paddingCount;
		byte[] result = new byte[length3];
		
		for (int x = 0; x < length3; x++)
		{
			result [x] = buffer2 [x];
		}
		
		return result;
	}
	
	private byte char2sixbit(char c)
	{
		char[] lookupTable = new char[64]{  
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
			'O','P','Q','R','S','T','U','V','W','X','Y', 'Z',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
			'o','p','q','r','s','t','u','v','w','x','y','z',
			'0','1','2','3','4','5','6','7','8','9','+','/'};
		if (c == '=')
			return 0;
		else
		{
			for (int x = 0; x < 64; x++)
			{
				if (lookupTable [x] == c)
					return (byte)x;
			}
			
			return 0;
		}
		
	}
}
