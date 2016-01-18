using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Runtime.InteropServices;

namespace XGSDK2
{
    public class instance : MonoBehaviour
    {

        void Awake()
        {
		}

		#if UNITY_IPHONE 

		[DllImport("__Internal")]
		private static extern void XGSDKInitSDK (string custInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKLogin (string custInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKPay (string payInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKLogout (string custInfo);

		[DllImport("__Internal")]
		private static extern string XGSDKGetChannelID ();

		[DllImport("__Internal")]
		private static extern void XGSDKOpenUserCenter();

		[DllImport("__Internal")]
		private static extern void XGSDKOnEnterGame(string roleInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKOnLevelUp(string roleInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKOnMissionBegin(string roleInfo,string missionId,string missionName,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnMissionSuccess(string roleInfo,string missionId,string missionName,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnMissionFail(string roleInfo,string missionId,string missionName,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnVirtualCurrencyPurchase(string roleInfo,int amount,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnVirtualCurrencyReward(string roleInfo,string reason,int amount,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnVirtualCurrencyConsume(string roleInfo,string itemName,int amount,string customParams);

		[DllImport("__Internal")]
		private static extern void XGSDKOnEvent(string roleInfo, string eventId, string eventDesc, int eventVal, string eventBody);


		[DllImport("__Internal")]
		private static extern void XGSDKPayFinish(string buyInfo);

		[DllImport("__Internal")]
		private static extern void XGSDKBindAccount(string customInfo);

		
		[DllImport("__Internal")]
		private static extern void XGSDKSwitchAccount(string customInfo);

		#endif
		
		//配置SDK路径 
		private const string SDK_JAVA_CLASS = "com.xgsdk.client.api.unity3d.XGSDKUnity3DWrapper";
         
        private static void callSdkApi(string apiName, params object[] args)
        {
            Debug.Log("callSdkApi is ----------- " + apiName + ",class name:" + SDK_JAVA_CLASS);
            #if UNITY_ANDROID 
            using (AndroidJavaClass cls = new AndroidJavaClass(SDK_JAVA_CLASS)) {           
                AndroidJavaObject instance = cls.CallStatic<AndroidJavaObject> ("getInstance"); 
                instance.Call(apiName,args);   
            } 
            #endif
        }

        private static string callRetSdkApi(string apiName, params object[] args)
        {
            Debug.Log("call xgsdk callRetSdkApi...");
            string retstring = "";
            Debug.Log("callback is ----------- " + apiName);
            #if UNITY_ANDROID 
            using (AndroidJavaClass cls = new AndroidJavaClass(SDK_JAVA_CLASS)) {
                AndroidJavaObject instance = cls.CallStatic<AndroidJavaObject> ("getInstance"); 
                retstring = instance.Call<string>(apiName,args);    
            }
            Debug.Log ("callRetSdkApi is ----------- " + apiName+",ret:"+retstring);
            #endif   
            return retstring;
        }

		//*********************基础接口*********************

		/// <summary>
		/// 设置UserCallBack，初始化及用户登录结果会通过此回调对象通知给游戏
		/// </summary>
		public static void setCallback()
		{
			Debug.Log("call xgsdk setCallback...");
			#if UNITY_ANDROID
			callSdkApi("setCallback");
			#endif
			#if UNITY_IPHONE 
			XGSDKInitSDK("");
			Debug.Log("call xgsdk setCallback skip for ios...");
			#endif
		}
		


		/// <summary>
		/// 登录接口
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void login(string customParams)
		{
			Debug.Log("call xgsdk login...");
			#if UNITY_ANDROID
			callSdkApi("login", customParams);
            #elif UNITY_IPHONE 
			XGSDKLogin(customParams);
			#elif UNITY_STANDALONE_WIN
			PlayerPrefs.SetString ("login_customParams", customParams);
			ChannelTestService.activeLoginPage(true);
			#endif
		} 
		  
			
		/// <summary>
		/// 支付接口
		/// </summary>
		/// <param name="payInfo">支付信息请参考PayInfo类定义 </param>
		public static void pay(PayInfo payInfo)
		{
			Debug.Log("call xgsdk pay...");
			Dictionary<string, object> pay = new Dictionary<string, object>();
			pay.Add ("uid", payInfo.Uid);
			pay.Add("productId", payInfo.ProductId);
			pay.Add("productName", payInfo.ProductName);
			pay.Add("productDesc", payInfo.ProductDesc);
			pay.Add ("productUnit", payInfo.ProductUnit);
			pay.Add ("productUnitPrice", payInfo.ProductUnitPrice);
			pay.Add("productQuantity", payInfo.ProductQuantity);
			pay.Add("totalAmount", payInfo.TotalAmount);
			pay.Add("payAmount", payInfo.PayAmount);
			pay.Add("currencyName", payInfo.CurrencyName);
			pay.Add("roleId", payInfo.RoleId);
			pay.Add("roleName", payInfo.RoleName);
			pay.Add("roleLevel", payInfo.RoleLevel);
			pay.Add("roleVipLevel", payInfo.RoleVipLevel);
			pay.Add("serverId", payInfo.ServerId);
			pay.Add("zoneId", payInfo.ZoneId);
			pay.Add("partyName", payInfo.PartyName);
			pay.Add("virtualCurrencyBalance", payInfo.VirtualCurrencyBalance);
			pay.Add("customInfo", payInfo.CustomInfo);
			pay.Add("gameTradeNo", payInfo.GameTradeNo);
			pay.Add("gameCallbackUrl", payInfo.GameCallBackURL);
			pay.Add("additionalParams", payInfo.AdditionalParams);


			string payJson = XGSDKMiniJSON.Json.Serialize(pay);
			Debug.Log(payJson);
			#if UNITY_ANDROID
			callSdkApi("pay", payJson);
			#elif UNITY_IPHONE
			XGSDKPay(payJson);
			#elif UNITY_STANDALONE_WIN

			ChannelTestPayDataBuilder.payInfo = payInfo;
			ChannelTestService.activePayPage(true);
            #endif        

		}

		/// <summary>
		/// 登出接口
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void logout(string customParams)
		{
			Debug.Log("call xgsdk logout..."); 
			#if UNITY_ANDROID
			callSdkApi("logout", customParams);
			#elif UNITY_IPHONE
			XGSDKLogout(customParams);
			#elif UNITY_STANDALONE_WIN
			PlayerPrefs.SetString ("logout_customParams", customParams);
			ChannelLogin.logout();
			#endif
		}

			
		/// <summary>
		/// 退出接口
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void exit(string customParams)
		{
			Debug.Log("call xgsdk exit...");
			#if UNITY_ANDROID 
			callSdkApi("exit", customParams);
			#elif UNITY_IPHONE
			Debug.Log("call xgsdk exit skip for ios...");
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.exit(customParams);
			#endif
			
		}

		/// <summary>
		/// 获得当前的渠道ID
		/// </summary>
		/// <returns>当前的渠道ID，具体渠道编码请参考西瓜文档中心</returns>
		public static string getChannelId()
		{
			Debug.Log ("call sgsdk getChannelId...");
			string channel = "";
			#if UNITY_ANDROID
			channel = callRetSdkApi("getChannelId");
			#elif UNITY_IPHONE
			channel= XGSDKGetChannelID();
			#elif UNITY_STANDALONE_WIN
			channel = ChannelServer.getChannelId();
			#endif
			Debug.Log ("The channel tag is: " + channel);
			return channel;
		}


		//*********************扩展接口***********************

		/// <summary>
		/// 打开渠道提供的用户中心（在渠道支持的情况下)
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void openUserCenter(string customParams)
		{
			Debug.Log("call xgsdk openUserCenter...");      
			#if UNITY_ANDROID
			callSdkApi("openUserCenter", customParams);
			#elif UNITY_IPHONE
			XGSDKOpenUserCenter();
			#elif UNITY_STANDALONE_WIN
			PlayerPrefs.SetString ("openUserCenter_customParams", customParams);
			ChannelEvent.openUserCenter(customParams);
			ChannelTestService.activeUserCenter(true);
			#endif
		}
		
		/// <summary>
		/// 打开渠道的切换账号界面（在渠道支持的情况下)
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void switchAccount(string customParams)
		{
			Debug.Log("call xgsdk switchAccount...");    
			#if UNITY_ANDROID               
			callSdkApi("switchAccount", customParams);
			#elif UNITY_IPHONE
			XGSDKLogout(customParams);
			#elif UNITY_STANDALONE_WIN
			PlayerPrefs.SetString ("switchAccount_customParams", customParams);
			ChannelTestService.switchAccount();
			#endif
		}


		/// <summary>
		/// 建议在玩家新创建游戏角色的时候，调用此接口，上报角色数据
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		public static void onCreateRole(RoleInfo roleInfo)
		{
			Debug.Log("call xgsdk createRole...");
			#if UNITY_ANDROID
			string roleJson = toRoleJson(roleInfo);
			callSdkApi("onCreateRole",roleJson);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onCreateRole(roleInfo);
			#endif
		}

		/// <summary>
		/// 建议在登录完成后，进入游戏界面时，调用此接口，上报角色数据
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		public static void onEnterGame(RoleInfo roleInfo)
		{   
			Debug.Log("call xgsdk enterGame...");      
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onEnterGame", roleJson);
			#elif UNITY_IPHONE
			XGSDKOnEnterGame(roleJson);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onEnterGame(roleInfo);
			#endif
		}
		
		/// <summary>
		/// 建议在角色等级升级时，调用此接口，上报角色数据
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		public static void onRoleLevelup(RoleInfo roleInfo)
		{
			Debug.Log("call xgsdk onRoleLevelup...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onRoleLevelup",roleJson);
			#elif UNITY_IPHONE
			XGSDKOnLevelUp(roleJson);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onRoleLevelup(roleInfo);
			#endif
		}


		/// <summary>
		/// 测试上述的扩展方法，渠道是否支持，目前保留
		/// </summary>
		/// <returns>true - 渠道支持此方法</returns>
		/// <param name="methodName">方法名(openUserCenter/switchAccount等)</param>
		public static bool isMethodSupport(string methodName)
		{
			Debug.Log("call xgsdk isMethodSupport...");    
			bool retMsg = false;
			#if UNITY_ANDROID
			using (AndroidJavaClass cls = new AndroidJavaClass(SDK_JAVA_CLASS)) {
				AndroidJavaObject instance = cls.CallStatic<AndroidJavaObject> ("getInstance"); 
				retMsg = instance.Call<bool>("isMethodSupport",methodName);    
			}
            #elif UNITY_IPHONE
			Debug.Log("call xgsdk isMethodSupport...method deprecated for iOS!");
			#endif
			return retMsg;
		}

		/// <summary>
		/// releaseResource
		/// </summary>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void releaseResource(string customParams)
		{
			Debug.Log("call xgsdk releaseResource...");      
			#if UNITY_ANDROID
			callSdkApi("releaseResource",customParams);
			#elif UNITY_IPHONE
			#endif
		}


		//************share api*************
		public static void xgShareLocalImage(string uid,string roleId,string shareChannle, string imagename, string title, string description, string targeturl)
		{
			Debug.Log("call xgsdk xgShareLocalImage...");      
			#if UNITY_ANDROID
			callSdkApi("xgShareLocalImage",uid,roleId,shareChannle,imagename,title,description,targeturl);
			#elif UNITY_IPHONE
			#endif
		}

		public static void xgPhotoShareActivity(string uid,string roleId,string title, string description, string targeturl)
		{
			Debug.Log("call xgsdk xgPhotoShareActivity...");      
			#if UNITY_ANDROID
			callSdkApi("xgPhotoShareActivity",uid,roleId,title,description,targeturl);
			#elif UNITY_IPHONE
			#endif
		}



		//***********************统计接口*************************

		/// <summary>
		/// 跟踪玩家的任务或关卡完成情况。当开始某个任务或关卡时，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="missionId">任务ID</param>
		/// <param name="missionName">任务名称</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onMissionBegin(RoleInfo roleInfo, string missionId, string missionName, string customParams)
		{
			Debug.Log("call xgsdk onMissionBegin...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onMissionBegin", roleJson, missionId, missionName ,customParams); 
            #elif UNITY_IPHONE
			XGSDKOnMissionBegin(roleJson,missionId,missionName,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onMissionBegin(roleInfo, missionId, missionName, customParams);
			#endif
		}
		
		/// <summary>
		/// 跟踪玩家的任务或关卡完成情况。当某个任务或关卡已经成功完成，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="missionId">任务ID</param>
		/// <param name="missionName">任务名称</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onMissionSuccess(RoleInfo roleInfo, string missionId, string missionName, string customParams)
		{
			Debug.Log("call xgsdk onMissionSuccess...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onMissionSuccess", roleJson, missionId, missionName, customParams);
			#elif UNITY_IPHONE
			XGSDKOnMissionSuccess(roleJson,missionId,missionName,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onMissionSuccess(roleInfo, missionId, missionName, customParams);
			#endif
		}
		
		/// <summary>
		/// 跟踪玩家的任务或关卡完成情况。当某个任务或关卡执行失败时，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="missionId">任务ID</param>
		/// <param name="missionName">任务名称</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onMissionFail(RoleInfo roleInfo, string missionId, string missionName, string customParams)
		{
			Debug.Log("call xgsdk onMissionFail...");
			string roleJson = toRoleJson(roleInfo);
            #if UNITY_ANDROID
			callSdkApi("onMissionFail", roleJson, missionId, missionName, customParams);
            #elif UNITY_IPHONE
			XGSDKOnMissionFail(roleJson,missionId,missionName,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onMissionFail(roleInfo, missionId, missionName, customParams);
			#endif

		}

		/// <summary>
		/// 跟踪玩家的购买及消费行为。当玩家通过充值获得游戏内虚拟货币时，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="amount">获得的虚拟货币数量</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onVirtualCurrencyPurchase(RoleInfo roleInfo, int amount, string customParams)
		{
			Debug.Log("call xgsdk onVirtualCurrencyPurchase...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onVirtualCurrencyPurchase", roleJson, amount, customParams);
            #elif UNITY_IPHONE
			XGSDKOnVirtualCurrencyPurchase(roleJson,amount,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onVirtualCurrencyPurchase(roleInfo, amount, customParams);
			#endif
		}

		/// <summary>
		/// 跟踪玩家的购买及消费行为。当玩家完成活动或任务后，系统赠送给玩家虚拟货币时，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="reason">赠送原因，比如（每日登录奖励,抽宝箱等)</param>
		/// <param name="amount">赠送的虚拟货币数量</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onVirtualCurrencyReward(RoleInfo roleInfo, string reason, int amount, string customParams)
		{
			Debug.Log("call xgsdk onVirtualCurrencyReward...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onVirtualCurrencyReward", roleJson, reason, amount, customParams);
            #elif UNITY_IPHONE
			XGSDKOnVirtualCurrencyReward(roleJson,reason,amount,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onVirtualCurrencyReward(roleInfo, reason, amount, customParams);
			#endif
		}

		/// <summary>
		/// 跟踪玩家的购买及消费行为。当玩家消费虚拟货币时，调用此接口
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="itemName">消费行为（比如十连抽，购买体力等)</param>
		/// <param name="amount">消费的数量</param>
		/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
		public static void onVirtualCurrencyConsume(RoleInfo roleInfo, string itemName, int amount, string customParams)
		{
			Debug.Log("call xgsdk onVirtualCurrencyConsume...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onVirtualCurrencyConsume", roleJson, itemName, amount, customParams);
            #elif UNITY_IPHONE
			XGSDKOnVirtualCurrencyConsume(roleJson,itemName,amount,customParams);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onVirtualCurrencyConsume(roleInfo, itemName, amount, customParams);
			#endif
		}

		
		
		/// <summary>
		/// 自定义事件，游戏可以通过此接口进行自助数据上报及分析
		/// </summary>
		/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
		/// <param name="eventId">事件类型</param>
		/// <param name="eventDesc">事件分类，比如/login/success,/login/fail等。事件分类支持层次结构</param>
		/// <param name="eventVal">事件数值。XGSDK的统计平台可以对此数值进行sum／max／min／avg等操作</param>
		/// <param name="eventBody">事件详细定义，JSON格式，默认为null</param>
		public static void onEvent(RoleInfo roleInfo, string eventId, string eventDesc, int eventVal, string eventBody)
		{
			Debug.Log("call xgsdk onEvent...");
			string roleJson = toRoleJson(roleInfo);
			#if UNITY_ANDROID
			callSdkApi("onEvent",roleJson, eventId, eventDesc, eventVal, eventBody); 
			#elif UNITY_IPHONE
			XGSDKOnEvent(roleJson,eventId,eventDesc,eventVal,eventBody);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onEvent(roleInfo, eventId, eventDesc,eventVal, eventBody);
			#endif
		}




		/**
		 * 跟踪玩家的支付信息，支付成功时调用此接口。
		 * @param roleInfo - 角色信息，请参考RoleInfo的定义
		 * @param rechargeChannel - 支付渠道，比如：支付宝、银联等等
		 * @param currency - 币种 ，比如："CNY", "KER", "HKD", "USD", "VND", "THB", "PHP"等
		 * @param money - 支付金额
		 */
		public static void onPayFinish(PayInfo payInfo){
			Debug.Log("call xgsdk onPayFinish...");
			
			Dictionary<string, object> pay = new Dictionary<string, object>();
			pay.Add ("uid", payInfo.Uid);
			pay.Add("productId", payInfo.ProductId);
			pay.Add("productName", payInfo.ProductName);
			pay.Add("productDesc", payInfo.ProductDesc);
			pay.Add ("productUnit", payInfo.ProductUnit);
			pay.Add ("productUnitPrice", payInfo.ProductUnitPrice);
			pay.Add("productQuantity", payInfo.ProductQuantity);
			pay.Add("totalAmount", payInfo.TotalAmount);
			pay.Add("payAmount", payInfo.PayAmount);
			pay.Add("currencyName", payInfo.CurrencyName);
			pay.Add("roleId", payInfo.RoleId);
			pay.Add("roleName", payInfo.RoleName);
			pay.Add("roleLevel", payInfo.RoleLevel);
			pay.Add("roleVipLevel", payInfo.RoleVipLevel);
			pay.Add("serverId", payInfo.ServerId);
			pay.Add("zoneId", payInfo.ZoneId);
			pay.Add("partyName", payInfo.PartyName);
			pay.Add("virtualCurrencyBalance", payInfo.VirtualCurrencyBalance);
			pay.Add("customInfo", payInfo.CustomInfo);
			pay.Add("gameTradeNo", payInfo.GameTradeNo);
			pay.Add("gameCallbackUrl", payInfo.GameCallBackURL);
			pay.Add("additionalParams", payInfo.AdditionalParams);
			
			
			string payJson = XGSDKMiniJSON.Json.Serialize(pay);
			#if UNITY_ANDROID
			callSdkApi("onPayFinish",payJson); 
			#elif UNITY_IPHONE
			XGSDKPayFinish(payJson);
			#elif UNITY_STANDALONE_WIN
			ChannelEvent.onPayFinish(payInfo);
			#endif
		}
        
		public static void onBindAccount(string customInfo){
			Debug.Log("call xgsdk onBindAccount...");
			#if UNITY_ANDROID
			Debug.Log("this is an empty implement...");
            #elif UNITY_IPHONE
			XGSDKBindAccount(customInfo);
			#elif UNITY_STANDALONE_WIN
			Debug.Log("this is an empty implement...");
			#endif
		}


		//RoleInfo转化成json
		public static string toRoleJson(RoleInfo roleInfo)
		{
			Dictionary<string, object> role = new Dictionary<string, object>();
			role.Add ("uid", roleInfo.Uid);
			role.Add ("roleId", roleInfo.RoleId);
			role.Add ("roleType", roleInfo.RoleType);
			role.Add ("roleLevel", roleInfo.RoleLevel);
			role.Add ("roleVipLevel", roleInfo.RoleVipLevel);
			role.Add ("serverId", roleInfo.ServerId);
			role.Add ("zoneId", roleInfo.ZoneId);
			role.Add ("roleName", roleInfo.RoleName);
			role.Add ("serverName", roleInfo.ServerName);
			role.Add ("zoneName", roleInfo.ZoneName);
			role.Add ("partyName", roleInfo.PartyName);
			role.Add ("gender", roleInfo.Gender);
			role.Add ("balance",roleInfo.Balance);
			string roleJson = XGSDKMiniJSON.Json.Serialize(role);
			return roleJson;
		}

    }


}