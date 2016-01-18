using UnityEngine;
using System.Collections;
using ChannelUtils;
using System.Collections.Generic;

public class ChannelTestLoginDataBuilder {
		
	private static string login_customParams;

	private static string AUTH_INFO = "eyJhdXRoVG9rZW4iOiItODcwMjk0ODg3MTE0MzQ0NDM0MiIsImNoYW5uZWxJZCI6InhndGVzdCIsInBsYW5JZCI6IjgwOSIsInNpZ24iOiI3MmUyNzBkZmY4Y2YwMmM0OWUwMWI0NTE4OWIwZTY1MGQ2Y2VkZjcwIiwidHMiOiIyMDE1MTEyNjExNTcwMyIsInVpZCI6ImRlYnVnXzMzNTg2NjdhZjlkZDQwZWRhYzI5MmY4MWQ2NTY1ZDg1IiwieGdBcHBJZCI6IjIyMzIwMTI0In0=";

	private static string buildRoleInfo(){

		Dictionary<string, string> roleInfo = new Dictionary<string, string>();
		roleInfo.Add ("uid", ConfigsUtils.getTextByKey ("uid"));
		roleInfo.Add("roleId", "hero123");
		roleInfo.Add("roleType", "vip");
		roleInfo.Add("roleLevel", "88");
		roleInfo.Add ("roleVipLevel", "vip13");
		roleInfo.Add ("serverId", "334337427");
		roleInfo.Add("zoneId","7878");
		roleInfo.Add("roleName", "stone.jones");
		roleInfo.Add("serverName", "XXXX");
		roleInfo.Add("zoneName", "YYYY");
		roleInfo.Add("partyName", "");
		roleInfo.Add("gender", "m");
		roleInfo.Add("balance", "12148钻石");

		return XGSDKMiniJSON.Json.Serialize(roleInfo);
	}

	private static string buildRequestDatas(string callType){
		Dictionary<string, string> paramsMap = new Dictionary<string, string>();
		paramsMap.Add("customParams", login_customParams);
		Dictionary<string, object> datas = new Dictionary<string, object>();
		datas.Add ("userName", ConfigsUtils.getTextByKey ("channeltest_userName"));
		datas.Add ("xgAppId", ConfigsUtils.getTextByKey ("XgAppId"));
		datas.Add ("uid", ConfigsUtils.getTextByKey ("uid"));
		datas.Add ("token", ConfigsUtils.getTextByKey ("token"));
		datas.Add ("authInfo", ConfigsUtils.getTextByKey ("authInfo"));
		datas.Add ("platform", ConfigsUtils.getTextByKey ("platform"));
		datas.Add ("interfaceType", ConfigsUtils.getTextByKey ("interfaceType"));
		datas.Add ("time", System.DateTime.Now.ToShortTimeString());
		datas.Add ("appKey", ConfigsUtils.getTextByKey ("XgAppKey"));
		datas.Add ("xgPlanId", ConfigsUtils.getTextByKey ("XgPlanId"));
		datas.Add ("interfaceName", "login");
		datas.Add ("callBackType",callType);

		datas.Add ("params", paramsMap);
		return XGSDKMiniJSON.Json.Serialize (datas);
	}

	private static string buildCallBackDatas(string authInfo, int code, string msg){
		Dictionary<string, object> datas = new Dictionary<string, object>();
		if (string.IsNullOrEmpty (authInfo)) {
			authInfo = AUTH_INFO;
		}
		datas.Add ("authInfo", authInfo);
		datas.Add ("code", code);
		datas.Add ("msg", msg);
		return XGSDKMiniJSON.Json.Serialize (datas);
	}

	public static bool validateUserNameAndPassword(string userName, string password)
	{
		if (string.IsNullOrEmpty (userName) || string.IsNullOrEmpty (password)) {
			ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("UserName or PassWord can not be empty!");
			return false;
		}
		// store user name and password
		ConfigsUtils.put("channeltest_userName", userName);
		ConfigsUtils.put("channeltest_password", password);
		return true;
	}

	public static void LoginSuccess()
	{
		Debug.Log ("start to login " + ConfigsUtils.getTextByKey("interfaceTestCheck"));
		//validateUserNameAndPassword ();
		string responseStr = "Login Success Without Role Info Checking";
		string msg = string.Empty;
		string authInfo = ConfigsUtils.getTextByKey ("authInfo");
		int code = ChannelServer.OTHER_ERROR;

		if ("check".Equals(ConfigsUtils.getTextByKey("interfaceTestCheck"))) {
			
			string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";
			responseStr = ChannelClient.getResponse (url,  buildRequestDatas("success"));
			Debug.Log ("login responseStr : " + responseStr);
			if(!string.IsNullOrEmpty(responseStr)){

				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;			
				if(responseObj.ContainsKey("uid"))
				{
					ConfigsUtils.put("uid", responseObj["uid"] as string);
				}
				
				if(responseObj.ContainsKey("token"))
				{
					ConfigsUtils.put("token", responseObj["token"] as string);
				}
				
				if(responseObj.ContainsKey("authInfo"))
				{
					authInfo = responseObj["authInfo"] as string;
					ConfigsUtils.put("authInfo", authInfo);
				}
				
				if(responseObj.ContainsKey("result"))
				{
					if("1".Equals(responseObj["result"].ToString())){
						code = ChannelServer.SUCCESS;
					}else{
						msg = "params check failed";
					}
				}
				
			}
		}else{
			code = ChannelServer.SUCCESS;
			msg = "success without sever verify";
		}

		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onLoginSuccess ( buildCallBackDatas(authInfo,code,msg));
	}

	public  static void Cancel()
	{
		Debug.Log ("start to login cancel ");
		//validateUserNameAndPassword ();
		string responseStr = "Login Cancel Without Role Info Checking";
		string msg = string.Empty;
		string authInfo = ConfigsUtils.getTextByKey ("authInfo");
		int code = ChannelServer.OTHER_ERROR;

		if ("check".Equals(ConfigsUtils.getTextByKey("interfaceTestCheck"))) {
			
			string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";
			responseStr = ChannelClient.getResponse (url,  buildRequestDatas("cancel"));
			
			Debug.Log ("login cancel responseStr : " + responseStr);
			if(!string.IsNullOrEmpty(responseStr)){

				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;
				if(responseObj.ContainsKey("result"))
				{
					if("1".Equals(responseObj["result"].ToString())){
						code = ChannelServer.LOGIN_CANCELED;
					}else{
						msg = "params check failed";
					}
				}
			}

		}else{
			code = ChannelServer.LOGIN_CANCELED;
			msg = "cancel without sever verify";
		}

		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onLoginCancel ( buildCallBackDatas(authInfo,code,msg));
	}

	public static void LoginFailed()
	{
		Debug.Log ("start to login");
		//validateUserNameAndPassword ();
		string responseStr = "Login Failed Without Role Info Checking";
		string msg = string.Empty;
		string authInfo = ConfigsUtils.getTextByKey ("authInfo");
		int code = ChannelServer.OTHER_ERROR;

		if ("check".Equals(ConfigsUtils.getTextByKey("interfaceTestCheck"))) {
			
			string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";
			responseStr = ChannelClient.getResponse (url,  buildRequestDatas("fail"));
			
			Debug.Log ("login failed responseStr : " + responseStr);
			if(!string.IsNullOrEmpty(responseStr)){

				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;
				if(responseObj.ContainsKey("result"))
				{
					if("1".Equals(responseObj["result"].ToString())){
						code = ChannelServer.LOGIN_CANCELED;
					}else{
						msg = "params check failed";
					}
				}
			}
		}else{
			code = ChannelServer.LOGIN_CANCELED;
			msg = "cancel without sever verify";
		}
		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onLoginFail (buildCallBackDatas(authInfo,code,msg));
	}

	public static void switchAccount(){
		string logout_customParams = PlayerPrefs.GetString ("logout_customParams");
		//to do call the sever
		Dictionary<string, string> paramsMap = new Dictionary<string, string>();
		paramsMap.Add("customParams", logout_customParams);
		
		Dictionary<string, object> datas = new Dictionary<string, object>();
		datas.Add ("userName", ConfigsUtils.getTextByKey ("channeltest_userName"));
		datas.Add ("xgAppId", ConfigsUtils.getTextByKey ("XgAppId"));
		datas.Add ("uid", ConfigsUtils.getTextByKey ("uid"));
		datas.Add ("token", ConfigsUtils.getTextByKey ("token"));
		datas.Add ("authInfo", ConfigsUtils.getTextByKey ("authInfo"));
		datas.Add ("platform", ConfigsUtils.getTextByKey ("platform"));
		datas.Add ("interfaceType", ConfigsUtils.getTextByKey ("interfaceType"));
		datas.Add ("time", System.DateTime.Now.ToShortTimeString());
		datas.Add ("appKey", ConfigsUtils.getTextByKey ("XgAppKey"));
		datas.Add ("xgPlanId", ConfigsUtils.getTextByKey ("XgPlanId"));

		datas.Add ("interfaceName", "switchAccount");
		datas.Add ("params", paramsMap);

		string msg = string.Empty;
		string authInfo = ConfigsUtils.getTextByKey ("authInfo");
		int code = ChannelServer.OTHER_ERROR;

		if ("check".Equals(ConfigsUtils.getTextByKey("interfaceTestCheck"))) {
			string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";
			string responseStr = ChannelClient.getResponse (url, XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log ("switchAccount responseStr : " + responseStr);
			if(!string.IsNullOrEmpty(responseStr)){

				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;
				if(responseObj.ContainsKey("result"))
				{
					if("1" == responseObj["result"].ToString()){
						code = ChannelServer.SUCCESS;
					}else{
						msg = "params check failed";
					}
				}
			}
		}else{
			code = ChannelServer.SUCCESS;
			msg = "switchAccount without sever verify";
		}

		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onLogoutFinish (buildCallBackDatas(authInfo,code,msg));
	}

	public static void logout(){
		string logout_customParams = PlayerPrefs.GetString ("logout_customParams");
		//to do call the sever
		Dictionary<string, string> paramsMap = new Dictionary<string, string>();
		paramsMap.Add("customParams", logout_customParams);
		
		Dictionary<string, object> datas = new Dictionary<string, object>();
		datas.Add ("userName", ConfigsUtils.getTextByKey ("channeltest_userName"));
		datas.Add ("xgAppId", ConfigsUtils.getTextByKey ("XgAppId"));
		datas.Add ("uid", ConfigsUtils.getTextByKey ("uid"));
		datas.Add ("token", ConfigsUtils.getTextByKey ("token"));
		datas.Add ("authInfo", ConfigsUtils.getTextByKey ("authInfo"));
		datas.Add ("platform", ConfigsUtils.getTextByKey ("platform"));
		datas.Add ("interfaceType", ConfigsUtils.getTextByKey ("interfaceType"));
		datas.Add ("time", System.DateTime.Now.ToShortTimeString());
		datas.Add ("appKey", ConfigsUtils.getTextByKey ("XgAppKey"));
		datas.Add ("xgPlanId", ConfigsUtils.getTextByKey ("XgPlanId"));
		
		datas.Add ("interfaceName", "logout");
		datas.Add ("params", paramsMap);
		
		string msg = string.Empty;
		string authInfo = ConfigsUtils.getTextByKey ("authInfo");
		int code = ChannelServer.OTHER_ERROR;
		
		if ("check".Equals(ConfigsUtils.getTextByKey("interfaceTestCheck"))) {
			string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";
			string responseStr = ChannelClient.getResponse (url, XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log ("logout responseStr : " + responseStr);
			if(!string.IsNullOrEmpty(responseStr)){
				
				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;
				if(responseObj.ContainsKey("result"))
				{
					if("1" == responseObj["result"].ToString()){
						code = ChannelServer.SUCCESS;
					}else{
						msg = "params check failed";
					}
				}
			}
		}else{
			code = ChannelServer.SUCCESS;
			msg = "logout without sever verify";
		}
		
		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onLogoutFinish (buildCallBackDatas(authInfo,code,msg));
	}

}
