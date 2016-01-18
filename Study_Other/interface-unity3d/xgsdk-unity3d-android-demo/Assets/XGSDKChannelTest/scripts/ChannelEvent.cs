using UnityEngine;
using System.Collections;
using ChannelUtils;
using System.Collections.Generic;

public class ChannelEvent : MonoBehaviour {

	void Awake()
	{
		Debug.Log ("ChannelEvent to Awake");
	}

	// Use this for initialization
	void Start () {
		Debug.Log ("ChannelEvent to Start");
	}
	
	// Update is called once per frame
	void Update () {
	// todo
	}

	private static Dictionary<string, object> buildRequestDatas(RoleInfo roleInfo, string interfaceName){
		Dictionary<string, object> paramsMap = new Dictionary<string, object>();
		paramsMap.Add ("roleInfo", buildRoleInfo (roleInfo));
		
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

		datas.Add ("interfaceName",interfaceName);
		datas.Add ("params", paramsMap);
		return datas;
	}

	private static Dictionary<string, object>  buildRoleInfo(RoleInfo roleInfo){
		
		Dictionary<string, object> roleInfodic = new Dictionary<string, object>();
		roleInfodic.Add ("uid", ConfigsUtils.getTextByKey ("uid"));
		roleInfodic.Add("roleId", roleInfo.RoleId);
		roleInfodic.Add("roleType",roleInfo.RoleType);
		roleInfodic.Add("roleLevel", roleInfo.RoleLevel);
		roleInfodic.Add ("roleVipLevel", roleInfo.RoleVipLevel);
		roleInfodic.Add ("serverId", roleInfo.ServerId);
		roleInfodic.Add("zoneId",roleInfo.ZoneId);
		roleInfodic.Add("roleName", roleInfo.RoleName);
		roleInfodic.Add("serverName", roleInfo.ServerName);
		roleInfodic.Add("zoneName", roleInfo.ZoneName);
		roleInfodic.Add("partyName", roleInfo.PartyName);
		roleInfodic.Add("gender",roleInfo.Gender);
		roleInfodic.Add("balance", roleInfo.Balance);
		
		return roleInfodic;
	}

	private static string getSeverResponse(string datas){
		string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";// 后续可以使用不同的url
		return ChannelClient.getResponse (url,  datas);
	}

	public static void onCreateRole(RoleInfo roleInfo)
	{
		Debug.Log("call ChannelEvent createRole start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onCreateRole");
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent createRole end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onCreateRole";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
	}

	/// <summary>
	/// 建议在登录完成后，进入游戏界面时，调用此接口，上报角色数据
	/// </summary>
	/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
	public static void onEnterGame(RoleInfo roleInfo)
	{   
		Debug.Log("call ChannelEvent onEnterGame start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onEnterGame");
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onEnterGame end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onEnterGame";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
	}
	
	/// <summary>
	/// 建议在角色等级升级时，调用此接口，上报角色数据
	/// </summary>
	/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
	public static void onRoleLevelup(RoleInfo roleInfo)
	{
		Debug.Log("call ChannelEvent onRoleLevelup start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onRoleLevelup");
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onRoleLevelup end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onRoleLevelup";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}

	}


	/// <summary>
	/// releaseResource
	/// </summary>
	/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
	public static void releaseResource(string customParams)
	{
		//ingore
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
		Debug.Log("call ChannelEvent onMissionBegin start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onMissionBegin");
		datas.Add ("missionId", missionId);
		datas.Add ("missionName", missionName);
		datas.Add ("customParams", customParams);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onMissionBegin end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onMissionBegin";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
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
		Debug.Log("call ChannelEvent onMissionSuccess start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onMissionSuccess");
		datas.Add ("missionId", missionId);
		datas.Add ("missionName", missionName);
		datas.Add ("customParams", customParams);
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onMissionSuccess end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onMissionSuccess";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
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
		Debug.Log("call ChannelEvent onMissionFail start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onMissionFail");
		datas.Add ("missionId", missionId);
		datas.Add ("missionName", missionName);
		datas.Add ("customParams", customParams);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onMissionFail end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onMissionFail";
			//Application.LoadLevel("paramsCheckPopupWindow");
			
		}
	}

	/// <summary>
	/// 跟踪玩家的购买及消费行为。当玩家通过充值获得游戏内虚拟货币时，调用此接口
	/// </summary>
	/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
	/// <param name="amount">获得的虚拟货币数量</param>
	/// <param name="customParams">扩展参数，JSON格式，默认为null</param>
	public static void onVirtualCurrencyPurchase(RoleInfo roleInfo, int amount, string customParams)
	{
		Debug.Log("call ChannelEvent onVirtualCurrencyPurchase start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onVirtualCurrencyPurchase");
		datas.Add ("amount", amount);
		datas.Add ("customParams", customParams);
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onVirtualCurrencyPurchase end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onVirtualCurrencyPurchase";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
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
		Debug.Log("call ChannelEvent onVirtualCurrencyReward start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onVirtualCurrencyReward");
		datas.Add ("amount", amount);
		datas.Add ("reason", reason);
		datas.Add ("customParams", customParams);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onVirtualCurrencyReward end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onVirtualCurrencyReward";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
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
		Debug.Log("call ChannelEvent onVirtualCurrencyConsume start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onVirtualCurrencyConsume");
		datas.Add ("amount", amount);
		datas.Add ("itemName", itemName);
		datas.Add ("customParams", customParams);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onVirtualCurrencyConsume end... response is : " + response);
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onVirtualCurrencyConsume";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
	}

	
	
	/// <summary>
	/// 自定义事件，游戏可以通过此接口进行自助数据上报及分析
	/// </summary>
	/// <param name="roleInfo">角色信息，请参考RoleInfo的定义</param>
	/// <param name="eventId">事件类型</param>
	/// <param name="eventDesc">事件分类，比如/login/success,/login/fail等。事件分类支持层次结构</param>
	/// <param name="eventVal">事件数值。ChannelEvent的统计平台可以对此数值进行sum／max／min／avg等操作</param>
	/// <param name="eventBody">事件详细定义，JSON格式，默认为null</param>
	public static void onEvent(RoleInfo roleInfo, string eventId, string eventDesc, int eventVal, string eventBody)
	{
		Debug.Log("call ChannelEvent onEvent start...");
		Dictionary<string, object> datas = buildRequestDatas (roleInfo, "onEvent");
		datas.Add ("eventDesc", eventDesc);
		datas.Add ("eventVal", eventVal);
		datas.Add ("eventId", eventId);
		datas.Add ("eventBody", eventBody);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent onEvent end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "onEvent";
			//Application.LoadLevel("paramsCheckPopupWindow");
		}
	}

	/// <summary>
	/// 网络连接状况检测。ChannelEvent可以帮助游戏从客户端收集到指定服务器的网络连接状况，为网络部署方案优化提供建议
	/// </summary>
	/// <param name="host">需要检测的主机域名或IP地址，ChannelEvent会在后台任务中完成网络状况的检测，不阻塞当前调用线程</param>
	public static void setPingServer(string host)
	{
		//ingore
	}




	/**
	 * 跟踪玩家的支付信息，支付成功时调用此接口。
	 * @param roleInfo - 角色信息，请参考RoleInfo的定义
	 * @param rechargeChannel - 支付渠道，比如：支付宝、银联等等
	 * @param currency - 币种 ，比如："CNY", "KER", "HKD", "USD", "VND", "THB", "PHP"等
	 * @param money - 支付金额
	 */
	public static void onPayFinish(PayInfo payInfo){
		//ingore
	}

	public static void openUserCenter(string customParams){
		Debug.Log("call ChannelEvent openUserCenter start...");
		//Dictionary<string, object> datas = buildRequestDatas (roleInfo, "openUserCenter");
		Dictionary<string, object> paramsMap = new Dictionary<string, object>();
		paramsMap.Add ("customParams", customParams);
		
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
		
		datas.Add ("interfaceName","openUserCenter");
		datas.Add ("params", paramsMap);

		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {

			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent openUserCenter end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "openUserCenter";
		}
	}

	public static void exit(string customParams){
		Debug.Log("call ChannelEvent exit start...");
		//Dictionary<string, object> datas = buildRequestDatas (roleInfo, "openUserCenter");
		Dictionary<string, object> paramsMap = new Dictionary<string, object>();
		paramsMap.Add ("customParams", customParams);
		
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
		
		datas.Add ("interfaceName","exit");
		datas.Add ("params", paramsMap);
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			
			string response = getSeverResponse (XGSDKMiniJSON.Json.Serialize (datas));
			Debug.Log("call ChannelEvent exit end... response is : " + response); 
			
			ChannelParamCheckResultWindow.response = response;
			ChannelParamCheckResultWindow.interfaceName = "exit";
		}
	}

}
