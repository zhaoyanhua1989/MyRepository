using UnityEngine;
using System.Collections;
using ChannelUtils;
using System.Collections.Generic;

public class ChannelTestPayDataBuilder {

	public static PayInfo payInfo;

	public static GameObject gird;

	private static void setPayInfoCheckResult(List<System.Object> detailArr){
		if (null != detailArr && detailArr.Count > 0) {
			Dictionary<string, object> map;
			// 删除原有节点
			deleteOldRows();

			if(null == gird)
			{
				gird = GameObject.Find("orderList_Grid");
			}
			GameObject row;
			for(int i = 0; i < detailArr.Count; i++)
			{
				map = (Dictionary<string, object>)detailArr[i];
				row = NGUITools.AddChild(gird,(GameObject)(Resources.Load ("channelTestParamsRow")));
				row.name = "channelTestParamsRow_"+i;
				displayCheckResult(map,row);
			}

			UIGrid girdobj = gird.GetComponent<UIGrid>();
			girdobj.Reposition();
		} else {
			Debug.LogError ("can not get payJson");
		}
	}

	private static void deleteOldRows(){
		if(null != gird && null != GameObject.Find("channelTestParamsRow_0"))
		{
			for(int i = 0; i < gird.transform.childCount; i++){
				GameObject.Destroy(gird.transform.GetChild(i).gameObject);
			}
			UIGrid girdobj = gird.GetComponent<UIGrid>();
			girdobj.Reposition();
		}
	}
	
	private static string changeLine(string input, int maxLength){
		if (maxLength <= 0) {
			Debug.LogError ("invalid length = " + maxLength);
			return input;
		}
		string output = string.Empty;
		if (!string.IsNullOrEmpty (input) && input.Length > maxLength) {
			Debug.Log ("input is "+input);
			Debug.Log ("input length is "+input.Length);
			for(int i = 0; i < input.Length/maxLength; i++){
				Debug.Log ("i = "+i);
				if(i == 0){
					output += input.Substring(0,maxLength) + "\n";
					//Debug.Log ("input is "+input);
				}
				else if(i == input.Length/maxLength - 1){
					output += input.Substring (i*maxLength,input.Length - (i*maxLength + 1));
				}else{
					output += input.Substring(i*maxLength,maxLength) + "\n";
				}
			}
		}else{
			return input;
		}
		return output;
	}
	
	private static void displayCheckResult(Dictionary<string, object> result, GameObject row){
		if (isValidMap (result)) {
			row.GetComponentsInChildren<UILabel>()[0].text = result["key"] as string;
			row.GetComponentsInChildren<UILabel>()[1].text = result["value"] as string;
			row.GetComponentsInChildren<UILabel>()[2].text = result["rule"] as string;

			if("1".Equals(result["result"].ToString())){
				row.GetComponentsInChildren<UISprite> () [0].spriteName = "yes";
			}else{
				row.GetComponentsInChildren<UISprite> () [0].spriteName = "no";
			}
			
		}
	}
	
	private static bool isValidMap(Dictionary<string, object> result){
		if (!result.ContainsKey ("key") || !result.ContainsKey ("value") || !result.ContainsKey ("result") || !result.ContainsKey ("rule")) {
			return false;
		}
		return true;
	}

	public static void ChargeSuccess()
	{
		Debug.Log ("start to charge : success");
		bool checkPassed = false;
		string msg = "Charge Success Without Pay Info Checking";
		//title.text = "paySuccess参数校验结果";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			string responseStr = getSeverResponse (buildRequestDatas("success"));
			if(!string.IsNullOrEmpty(responseStr)){
				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;			
				Debug.Log ("charge success " + responseStr);
				if(responseObj.ContainsKey("tradeNo"))
				{
					ConfigsUtils.put("tradeNo", responseObj["tradeNo"] as string);
				}
				
				if(responseObj.ContainsKey("detail"))
				{
					//Debug.Log ("detail: " + responseObj["detail"] as List<System.Object>);
					setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
				}

				if(responseObj.ContainsKey("result") && "1".Equals(responseObj["result"]))
				{
					checkPassed = true;
				}
				
				msg = "Cancel.";
			}
		}
		if (!checkPassed) {
			return;
		}
		Dictionary<string, object> callBackInfo = new Dictionary<string, object> ();
		callBackInfo.Add ("code",200);
		callBackInfo.Add ("msg",msg);
		callBackInfo.Add ("gameTradeNo",ConfigsUtils.getTextByKey("gameTradeNo"));
		callBackInfo.Add ("tradeNo",ConfigsUtils.getTextByKey("tradeNo"));
		callBackInfo.Add ("channelCode","xgtest");
		callBackInfo.Add ("channelMsg","test channel pay success");
		
		//string info = "{\"code\":\"200\",\"msg\":\"Succes.\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"b1502d1000071602\",\"channelCode\":\"testchannel-01\",\"channelMsg\":\"test channel pay success\"}";
		string info = XGSDKMiniJSON.Json.Serialize (callBackInfo);
		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onPaySuccess (info);
		
		//Pay.payInfo = info;
		//Application.LoadLevel("pay");
	}
	
	public static void ChargeCancel()
	{
		Debug.Log ("start to charge : cancel");
		bool checkPassed = false;
		//title.text = "payCancel参数校验结果";
		//Utils.DialogHelper.getInstance().showCallbackTip ("PayCancel,message:{\"code\":\"2100\",\"msg\":\"pay cancelled\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"\",\"channelCode\":\"\",\"channelMsg\":\"channel pay cancelled\"}");
		string msg = "Charge cancel Without Pay Info Checking";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			string responseStr = getSeverResponse (buildRequestDatas("cancel"));
			
			if(!string.IsNullOrEmpty(responseStr)){
				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;			
				if(responseObj.ContainsKey("tradeNo"))
				{
					ConfigsUtils.put("tradeNo", responseObj["tradeNo"] as string);
				}
				msg = "Cancel.";
				
				if(responseObj.ContainsKey("detail"))
				{
					//Debug.Log ("detail: " + responseObj["detail"] as List<System.Object>);
					setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
				}
				
				if(responseObj.ContainsKey("result") && "1".Equals(responseObj["result"]))
				{
					checkPassed = true;
				}
			}
		}
		if (!checkPassed) {
			return;
		}
		Dictionary<string, object> callBackInfo = new Dictionary<string, object> ();
		callBackInfo.Add ("code",200);
		callBackInfo.Add ("msg",msg);
		callBackInfo.Add ("gameTradeNo",ConfigsUtils.getTextByKey("gameTradeNo"));
		callBackInfo.Add ("tradeNo",ConfigsUtils.getTextByKey("tradeNo"));
		callBackInfo.Add ("channelCode","xgtest");
		callBackInfo.Add ("channelMsg","test channel pay cancel");
		
		//string info = "{\"code\":\"200\",\"msg\":\"Succes.\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"b1502d1000071602\",\"channelCode\":\"testchannel-01\",\"channelMsg\":\"test channel pay success\"}";
		string info = XGSDKMiniJSON.Json.Serialize (callBackInfo);
		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onPayCancel (info);
		
		//string info = "{\"code\":\"2100\",\"msg\":\"Cancelled.\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"b1502d1000071602\",\"channelCode\":\"testchannel-01\",\"channelMsg\":\"test channel pay cancel\"}";
		//XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		//callback.onPayCancel (info);
		
	}
	
	public static void ChargeFailed()
	{
		Debug.Log ("start to charge : failed");
		bool checkPassed = false;
		string msg = "Charge failed Without Pay Info Checking";
		//title.text = "payFailed参数校验结果";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			Debug.Log ("start to charge : failed request str = "+buildRequestDatas("fail"));
			string responseStr = getSeverResponse (buildRequestDatas("fail"));
			Debug.Log ("start to charge : failed response str = "+responseStr);
			if(!string.IsNullOrEmpty(responseStr)){
				Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;			
				if(responseObj.ContainsKey("tradeNo"))
				{
					ConfigsUtils.put("tradeNo", responseObj["tradeNo"] as string);
				}
				msg = "Failed.";
				
				if(responseObj.ContainsKey("detail"))
				{
					//Debug.Log ("detail: " + responseObj["detail"] as List<System.Object>);
					setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
				}
				Debug.Log ("responseObj sure "+responseObj.ContainsKey("result") + "   "+"1".Equals(responseObj["result"].ToString()));
				if(responseObj.ContainsKey("result") && "1".Equals(responseObj["result"]))
				{
					checkPassed = true;
				}
			}
		}
		
		if (!checkPassed) {
			//Utils.DialogHelper.getInstance ().showCallbackTip ("Check faild");
			return;
		}
		Dictionary<string, object> callBackInfo = new Dictionary<string, object> ();
		callBackInfo.Add ("code",200);
		callBackInfo.Add ("msg",msg);
		callBackInfo.Add ("gameTradeNo",ConfigsUtils.getTextByKey("gameTradeNo"));
		callBackInfo.Add ("tradeNo",ConfigsUtils.getTextByKey("tradeNo"));
		callBackInfo.Add ("channelCode","xgtest");
		callBackInfo.Add ("channelMsg","test channel pay failed");
		
		//string info = "{\"code\":\"200\",\"msg\":\"Succes.\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"b1502d1000071602\",\"channelCode\":\"testchannel-01\",\"channelMsg\":\"test channel pay success\"}";
		string info = XGSDKMiniJSON.Json.Serialize (callBackInfo);
		XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		callback.onPayFail (info);
		
		//string info = "{\"code\":\"2000\",\"msg\":\"Failed.\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"b1502d1000071602\",\"channelCode\":\"testchannel-01\",\"channelMsg\":\"test channel pay failed\"}";
		//XGSDKCallbackWrapper callback = new XGSDKCallbackWrapper ();
		//callback.onPayFail (info);
		
		//Pay.payInfo = info;
		//Application.LoadLevel("pay");
	}


	private static string getSeverResponse(string datas){
		string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";// 后续可以使用不同的url
		return ChannelClient.getResponse (url,  datas);
	}
	
	private static Dictionary<string, object> buildPayInfo(PayInfo payInfo){
		Dictionary<string, object> pay = new Dictionary<string, object>();
		//Debug.Log ("buildPayInfo uid "+payInfo.Uid);
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
		return pay;
	}
	
	
	private static string buildRequestDatas(string callType){
		Dictionary<string, object> paramsMap = new Dictionary<string, object>();
		paramsMap.Add("payInfo", buildPayInfo(payInfo));
		
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
		
		datas.Add ("callBackType",callType);
		datas.Add ("interfaceName", "pay");
		
		datas.Add ("params", paramsMap);
		return XGSDKMiniJSON.Json.Serialize (datas);
	}

}
