using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ChannelServer{

	public static int SUCCESS = 200; // 成功
	public static int INIT_FAILED = 1000; // 客户端初始化失败
	public static int  LOGIN_FAILED = 1100 ;// 客户端登录失败
	public static int  LOGOUT_FAILED  =1300 ;// 客户端登出失败
	public static int  EXIT_FAILED = 1400; // 客户端退出失败
	public static int  LOGIN_FAILED_CHANNEL_INIT = 1500; // 登陆失败--渠道正在初始化	
	public static int  PAY_FAILED  =2000 ;// 支付失败
	public static int  PAY_FAILED_CREATE_ORDER_FAILED  =2010; // 创建订单失败
	public static int  PAY_FAILED_PRODUCT_TOTAL_PRICE_INVALID  =2020 ;// 订单金额错误
	public static int  PAY_FAILED_UID_INVALID = 2030; // UID错误
	public static int  PAY_FAILED_PRODUCT_COUNT_INVALID = 2040; // 产品数量错误
	public static int  PAY_FAILED_EXT_INVALID = 2050 ;// 扩展信息错误
	public static int  PAY_FAILED_CHANNEL_RESPONSE  =2060 ;// 渠道反馈的支付失败
	public static int  PAY_FAILED_REPEAT = 2070; //短时间内重复支付(2秒内)
	public static int  PAY_PROGRESS = 2080 ;//正在支付中
	public static int  PAY_RESULT_UNKOWN = 2090 ;//支付结果未知，需要服务端进一步确认
	public static int  PAY_CANCELED  =2100 ;//支付取消	
	public static int  OTHER_ERROR  =9999;
	public static int  LOGIN_CANCELED = 1200 ;//客户端登陆取消

	public static void init(){
		//todo
	}

	public static string getChannelId(){
		// todo 需要访问渠道server
		return "XGTestChannelForWin";
	}

	public static string getAuthInfo(int loginStatus){
		string result = "";
		// success
		if (0 == loginStatus) {
			string userName;
			string password;

			userName = PlayerPrefs.GetString ("userName");
			password = PlayerPrefs.GetString ("password");

			string session = userName + "_" + password;
		// cancelled
		} else if (1 == loginStatus) {

		// failed
		} else {

		}

		return result;
	}

	public static string buildRoleInfo(){
		
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
	
}
