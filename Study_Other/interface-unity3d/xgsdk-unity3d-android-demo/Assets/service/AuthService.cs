using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Runtime.InteropServices;


namespace AuthService
{
	public class VerifySesssion : MonoBehaviour
	{

		[DllImport("__Internal")]
		private static extern string XGSDKGetVerifyUrl(string authInfo);


		private static VerifySesssion instance = null;

		public static VerifySesssion getInstance()
		{
			if(instance == null)
			{
				GameObject ob = new GameObject("VerifySesssion");
				ob.AddComponent<VerifySesssion>();

				instance = ob.GetComponent<VerifySesssion>();
			}
			return instance;
		}

		private const string SDK_JAVA_CLASS = "com.xgsdk.client.api.unity3d.service.SessionHelper";

		public string getAuthUrl(string authInfo)
		{
			string url = "";
			#if UNITY_ANDROID
			url = callSdkApi ("generateVerifySessionUrl", authInfo);
			#elif UNITY_IPHONE
			url= XGSDKGetVerifyUrl(authInfo);
			#elif UNITY_STANDALONE_WIN
			#endif

			Debug.Log ("get verify url is " + url);
			return url;
		}

		public  string callSdkApi(string apiName,params object[] args)
		{
			Debug.Log("call xgsdk callRetSdkApi...");
			string retstring = "";
			Debug.Log("callback is ----------- " + apiName);
			#if UNITY_ANDROID 
			using (AndroidJavaClass cls = new AndroidJavaClass(SDK_JAVA_CLASS)) {
				retstring = cls.CallStatic<string>(apiName,args);
			}
			Debug.Log ("callRetSdkApi is ----------- " + apiName+",ret:"+retstring);
			#endif   
			return retstring;
		}

		public void verify(string authInfo)
		{
			WWW www = new WWW(getAuthUrl(authInfo));
			StartCoroutine (WaitForRequestVerify (www));

		}

		IEnumerator WaitForRequestVerify(WWW www)
		{
			yield return www;
			if(www.error != null)
			{
				Debug.Log("fail to request "+ www.error );
			}else{
				if(www.isDone)
				{
					Debug.Log("verify return " + www.text);
					//Result result = Result.fromJsonStr(www.text);
					Dictionary<string,object> resultDict = XGSDKMiniJSON.Json.Deserialize(www.text) as Dictionary<string,object> ;
					Result resultObj = new Result();
					string codeStr = "";
					string msgStr = "";
					if(resultDict.ContainsKey("code"))
					{
						codeStr = resultDict["code"] as string;
					}
					if(resultDict.ContainsKey("msg"))
					{
						msgStr = resultDict["msg"] as string;
					}

					string dataStr = XGSDKMiniJSON.Json.Serialize(resultDict["data"]);
					Debug.Log ("result code is " +codeStr  +  " msg is " + msgStr + " result data is " + dataStr);

					resultObj.Code = codeStr;
					resultObj.Msg = msgStr;
					resultObj.Data = dataStr;
					if(!string.Equals("0",codeStr) || resultObj == null)
					{
						Debug.Log("verify session fail,msg is " + msgStr);
						string tip = "verify fail," + msgStr;
						ChannelUtils.DialogHelper.getInstance ().showCallbackTip (tip);
					}else{
						Dictionary<string,object> resultDataDict = XGSDKMiniJSON.Json.Deserialize(dataStr) as Dictionary<string,object> ;
						string uid = "";
						if(resultDataDict.ContainsKey("uid"))//some channel no uid return
						{
							uid = resultDataDict ["uid"] as String;
						}
						Pay.uid = uid;
						mainGame.roleInfo.Uid = uid;
						//RoleInfo roleInfo = fillRoleInfo(uid);

						StartGame.verifiedInfo = dataStr;
						Application.LoadLevel("loginSuccess");
					}



				}
			}
		}


		public RoleInfo fillRoleInfo(string uid)
		{
			mainGame.roleInfo.Uid = uid;
			mainGame.roleInfo.RoleId = "56848";
			mainGame.roleInfo.RoleType = "TanXian";
			mainGame.roleInfo.RoleVipLevel="3";
			mainGame.roleInfo.ServerId = "5";
			mainGame.roleInfo.ZoneId = "701";		
			mainGame.roleInfo.ServerName = "Gd";
			mainGame.roleInfo.ZoneName = "battle";
			mainGame.roleInfo.PartyName = "sky";
			mainGame.roleInfo.Gender = "m";

			return mainGame.roleInfo;
		}
	}

}
