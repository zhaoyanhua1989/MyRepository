using UnityEngine;
using System.Collections;
using ChannelUtils;
using System.Collections.Generic;
//using System.Threading;

public class ChannelCharge : MonoBehaviour {

	public UILabel title;

	public UILabel payAmount;
	public UILabel uid;
	public UILabel gameTradeNo;
	public UILabel roleLevel;
	public UILabel roleName;
	public UILabel gameCallbackUrl;
	public UILabel customInfo;
	public UILabel roleVipLevel;
	public UILabel productUnit;
	public UILabel zoneId;
	public UILabel productId;
	public UILabel productDes;
	public UILabel currencyName;
	public UILabel productUnitPrice;
	public UILabel productQuantity;
	public UILabel productName;
	public UILabel serverId;
	public UILabel totalAmount;
	public UILabel roleId;
	public UILabel productDesc;
	public UILabel partyName;
	public UILabel virtualCurrencyBalance;
	public UILabel additionalParams;

	public UILabel payAmount_true;
	public UILabel uid_true;
	public UILabel gameTradeNo_true;
	public UILabel roleLevel_true;
	public UILabel roleName_true;
	public UILabel gameCallbackUrl_true;
	public UILabel customInfo_true;
	public UILabel roleVipLevel_true;
	public UILabel productUnit_true;
	public UILabel zoneId_true;
	public UILabel productId_true;
	public UILabel productDes_true;
	public UILabel currencyName_true;
	public UILabel productUnitPrice_true;
	public UILabel productQuantity_true;
	public UILabel productName_true;
	public UILabel serverId_true;
	public UILabel totalAmount_true;
	public UILabel roleId_true;
	public UILabel productDesc_true;
	public UILabel partyName_true;
	public UILabel virtualCurrencyBalance_true;
	public UILabel additionalParams_true;

	public UISprite payAmountFlag;
	public UISprite uidFlag;
	public UISprite gameTradeNoFlag;
	public UISprite roleLevelFlag;
	public UISprite roleNameFlag;
	public UISprite gameCallbackUrlFlag;
	public UISprite customInfoFlag;
	public UISprite roleVipLevelFlag;
	public UISprite productUnitFlag;
	public UISprite zoneIdFlag;
	public UISprite productIdFlag;
	public UISprite productDesFlag;
	public UISprite currencyNameFlag;
	public UISprite productUnitPriceFlag;
	public UISprite productQuantityFlag;
	public UISprite productNameFlag;
	public UISprite serverIdFlag;
	public UISprite totalAmountFlag;
	public UISprite roleIdFlag;
	public UISprite productDescFlag;
	public UISprite partyNameFlag;
	public UISprite virtualCurrencyBalanceFlag;
	public UISprite additionalParamsFlag;

	public static PayInfo payInfo;

	void Awake()
	{

	}

	// Use this for initialization
	void Start () {
		Debug.Log ("init pay information");

		payAmountFlag.spriteName = "";
		uidFlag.spriteName = "";
		gameTradeNoFlag.spriteName = "";
		roleLevelFlag.spriteName = "";
		roleNameFlag.spriteName = "";
		gameCallbackUrlFlag.spriteName = "";
		customInfoFlag.spriteName = "";
		roleVipLevelFlag.spriteName = "";
		productUnitFlag.spriteName = "";
		zoneIdFlag.spriteName = "";
		productIdFlag.spriteName = "";
		productDesFlag.spriteName = "";
		currencyNameFlag.spriteName = "";// miss
		productUnitPriceFlag.spriteName = "";
		productQuantityFlag.spriteName = "";
		productNameFlag.spriteName = "";
		serverIdFlag.spriteName = "";
		totalAmountFlag.spriteName = "";
		roleIdFlag.spriteName = "";
		partyNameFlag.spriteName = "";
		virtualCurrencyBalanceFlag.spriteName = "";
		additionalParamsFlag.spriteName = "";

		ValidateResult result = null;
		if (null != payInfo) {

			result = ChannelValidator.payInfoValidator("payAmount",payInfo.PayAmount);
			if(result.getIsValid()){
				//payAmountFlag.spriteName = "yes";
			}
			result = null;
			payAmount.text = payInfo.PayAmount.ToString();

			result = ChannelValidator.payInfoValidator("uid",payInfo.Uid);
			if(result.getIsValid()){
				//uidFlag.spriteName = "yes";
			}
			result = null;
			uid.text = (string)payInfo.Uid;

			result = ChannelValidator.payInfoValidator("gameTradeNo",payInfo.GameTradeNo);
			if(result.getIsValid()){
				//gameTradeNoFlag.spriteName = "yes";
			}
			result = null;
			gameTradeNo.text = payInfo.GameTradeNo.ToString();

			result = ChannelValidator.payInfoValidator("roleLevel",payInfo.RoleLevel);
			if(result.getIsValid()){
				//roleLevelFlag.spriteName = "yes";
			}
			result = null;
			roleLevel.text = payInfo.RoleLevel.ToString();

			result = ChannelValidator.payInfoValidator("roleName",payInfo.RoleName);
			if(result.getIsValid()){
				//roleNameFlag.spriteName = "yes";
			}
			result = null;
			roleName.text = payInfo.RoleName.ToString();

			result = ChannelValidator.payInfoValidator("gameCallbackUrl",payInfo.GameCallBackURL);
			if(result.getIsValid()){
				//gameCallbackUrlFlag.spriteName = "yes";
			}
			result = null;
			gameCallbackUrl.text = payInfo.GameCallBackURL.ToString();

			result = ChannelValidator.payInfoValidator("additionalParams",payInfo.AdditionalParams);
			if(result.getIsValid()){
				//.spriteName = "yes";
			}
			result = null;
			customInfo.text = payInfo.AdditionalParams.ToString();

			result = ChannelValidator.payInfoValidator("roleVipLevel",payInfo.RoleVipLevel);
			if(result.getIsValid()){
				//.spriteName = "yes";
			}
			result = null;
			roleVipLevel.text = payInfo.RoleVipLevel.ToString();

			result = ChannelValidator.payInfoValidator("productUnit",payInfo.ProductUnit);
			if(result.getIsValid()){
			//	productUnitFlag.spriteName = "yes";
			}
			result = null;
			productUnit.text = payInfo.ProductUnit.ToString();

			result = ChannelValidator.payInfoValidator("zoneId",payInfo.ZoneId);
			if(result.getIsValid()){
				//zoneIdFlag.spriteName = "yes";
			}
			result = null;
			zoneId.text = payInfo.ZoneId.ToString();

			result = ChannelValidator.payInfoValidator("productId",payInfo.ProductId);
			if(result.getIsValid()){
				//productIdFlag.spriteName = "yes";
			}
			result = null;
			productId.text = payInfo.ProductId.ToString();

			result = ChannelValidator.payInfoValidator("productDesc",payInfo.ProductDesc);
			if(result.getIsValid()){
				//productDesFlag.spriteName = "yes";
			}
			result = null;
			productDes.text = payInfo.ProductDesc.ToString();

			result = ChannelValidator.payInfoValidator("productUnitPrice",payInfo.ProductUnitPrice);
			if(result.getIsValid()){
				//productUnitPriceFlag.spriteName = "yes";
			}
			result = null;
			productUnitPrice.text = payInfo.ProductUnitPrice.ToString();

			result = ChannelValidator.payInfoValidator("totalAmount",payInfo.TotalAmount);
			if(result.getIsValid()){
				//productQuantityFlag.spriteName = "yes";
			}
			result = null;
			productQuantity.text = payInfo.TotalAmount.ToString();

			result = ChannelValidator.payInfoValidator("productQuantity",payInfo.ProductQuantity);
			if(result.getIsValid()){
				//productNameFlag.spriteName = "yes";
			}
			result = null;
			productName.text = payInfo.ProductQuantity.ToString();


			result = ChannelValidator.payInfoValidator("serverId",payInfo.ServerId);
			if(result.getIsValid()){
				//serverIdFlag.spriteName = "yes";
			}
			result = null;
			serverId.text = payInfo.ServerId.ToString();
			
		} else {
			Debug.LogError ("can not get payJson");
		}
		PlayerPrefs.SetString ("currentPage", "channelCharge");
	}

	private void setPayInfoCheckResult(List<System.Object> detailArr){
		if (null != detailArr && detailArr.Count > 0) {
			Dictionary<string, object> map;
			for(int i = 0; i < detailArr.Count; i++)
			{
				map = (Dictionary<string, object>)detailArr[i];
				this.displayCheckResult(map);
			}
		} else {
			Debug.LogError ("can not get payJson");
		}
	}

	private string changeLine(string input, int maxLength){
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

	private void displayCheckResult(Dictionary<string, object> result){
		if (this.isValidMap (result)) {

			switch(result["key"].ToString()){
			case  "payInfo.uid" :
				uid.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					uidFlag.spriteName = "yes";
				}else{
					uidFlag.spriteName = "no";
				}
				uid_true.text = (string)result["rule"];
				break;
			case  "payInfo.productName" :	
				productName.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productNameFlag.spriteName = "yes";
				}else{
					productNameFlag.spriteName = "no";
				}
				productName_true.text = (string)result["rule"];
				break;
			case  "payInfo.productId" :	
				productId.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productIdFlag.spriteName = "yes";
				}else{
					productIdFlag.spriteName = "no";
				}
				productId_true.text = (string)result["rule"];
				break;

			case  "payInfo.productDesc" :	
				productDesc.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productDescFlag.spriteName = "yes";
				}else{
					productDescFlag.spriteName = "no";
				}
				productDesc_true.text = (string)result["rule"];
				break;

			case  "payInfo.productUnit" :
				productUnit.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productUnitFlag.spriteName = "yes";
				}else{
					productUnitFlag.spriteName = "no";
				}
				productUnit_true.text = (string)result["rule"];
				break;

			case  "payInfo.productUnitPrice" :	
				productUnitPrice.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productUnitPriceFlag.spriteName = "yes";
				}else{
					productUnitPriceFlag.spriteName = "no";
				}
				productUnitPrice_true.text = (string)result["rule"];
				break;

			case  "payInfo.productQuantity" :	
				productQuantity.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					productQuantityFlag.spriteName = "yes";
				}else{
					productQuantityFlag.spriteName = "no";
				}
				productQuantity_true.text = (string)result["rule"];
				break;

			case  "payInfo.totalAmount" :
				totalAmount.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					totalAmountFlag.spriteName = "yes";
				}else{
					totalAmountFlag.spriteName = "no";
				}
				totalAmount_true.text = (string)result["rule"];
				break;

			case  "payInfo.payAmount" :	
				payAmount.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					payAmountFlag.spriteName = "yes";
				}else{
					payAmountFlag.spriteName = "no";
				}
				payAmount_true.text = (string)result["rule"];
				break;

			case  "payInfo.currencyName" :	
				currencyName.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					currencyNameFlag.spriteName = "yes";
				}else{
					currencyNameFlag.spriteName = "no";
				}
				currencyName_true.text = (string)result["rule"];
				break;

			case  "payInfo.roleId" :
				roleId.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					roleIdFlag.spriteName = "yes";
				}else{
					roleIdFlag.spriteName = "no";
				}
				roleId_true.text = (string)result["rule"];
				break;

			case  "payInfo.roleName" :	
				roleName.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					roleNameFlag.spriteName = "yes";
				}else{
					roleNameFlag.spriteName = "no";
				}
				roleName_true.text = (string)result["rule"];
				break;

			case  "payInfo.roleLevel" :	
				roleLevel.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					roleLevelFlag.spriteName = "yes";
				}else{
					roleLevelFlag.spriteName = "no";
				}
				roleLevel_true.text = (string)result["rule"];
				break;

			case  "payInfo.roleVipLevel" :
				roleVipLevel.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					roleVipLevelFlag.spriteName = "yes";
				}else{
					roleVipLevelFlag.spriteName = "no";
				}
				roleVipLevel_true.text = (string)result["rule"];
				break;

			case  "payInfo.serverId" :	
				serverId.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					serverIdFlag.spriteName = "yes";
				}else{
					serverIdFlag.spriteName = "no";
				}
				serverId_true.text = (string)result["rule"];
				break;

			case  "payInfo.zoneId" :	
				zoneId.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					zoneIdFlag.spriteName = "yes";
				}else{
					zoneIdFlag.spriteName = "no";
				}
				zoneId_true.text = (string)result["rule"];
				break;

			case  "payInfo.partyName" :	
				partyName.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					partyNameFlag.spriteName = "yes";
				}else{
					partyNameFlag.spriteName = "no";
				}
				partyName_true.text = (string)result["rule"];
				break;

			case  "payInfo.virtualCurrencyBalance" :
				virtualCurrencyBalance.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					virtualCurrencyBalanceFlag.spriteName = "yes";
				}else{
					virtualCurrencyBalanceFlag.spriteName = "no";
				}
				virtualCurrencyBalance_true.text = (string)result["rule"];
				break;

			case  "payInfo.customInfo" :	
				customInfo.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					customInfoFlag.spriteName = "yes";
				}else{
					customInfoFlag.spriteName = "no";
				}
				customInfo_true.text = (string)result["rule"];
				break;

			case  "payInfo.gameTradeNo" :	
				gameTradeNo.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					gameTradeNoFlag.spriteName = "yes";
				}else{
					gameTradeNoFlag.spriteName = "no";
				}
				gameTradeNo_true.text = (string)result["rule"];
				break;

			case  "payInfo.gameCallbackUrl" :	
				gameCallbackUrl.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					gameCallbackUrlFlag.spriteName = "yes";
				}else{
					gameCallbackUrlFlag.spriteName = "no";
				}
				gameCallbackUrl_true.text = (string)result["rule"];
				break;

			case  "payInfo.additionalParams" :
				additionalParams.text = (string)result["value"];
				if("1".Equals(result["result"].ToString())){
					additionalParamsFlag.spriteName = "yes";
				}else{
					additionalParamsFlag.spriteName = "no";
				}
				additionalParams_true.text = (string)result["rule"];
				break;

			}
		}
	}

	private bool isValidMap(Dictionary<string, object> result){
		if (!result.ContainsKey ("key") || !result.ContainsKey ("value") || !result.ContainsKey ("result") || !result.ContainsKey ("rule")) {
			return false;
		}
		return true;
	}
	
	// Update is called once per frame
	void Update () {
	// todo
	}

	public void ChargeSuccess()
	{
		Debug.Log ("start to charge : success");
		bool checkPassed = false;
		string msg = "Charge Success Without Pay Info Checking";
		title.text = "paySuccess参数校验结果";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			string responseStr = this.getSeverResponse (this.buildRequestDatas("success"));
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
					this.setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
				}		if(responseObj.ContainsKey("result") && "1".Equals(responseObj["result"]))
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

	public void ChargeCancel()
	{
		Debug.Log ("start to charge : cancel");
		bool checkPassed = false;
		title.text = "payCancel参数校验结果";
		//Utils.DialogHelper.getInstance().showCallbackTip ("PayCancel,message:{\"code\":\"2100\",\"msg\":\"pay cancelled\",\"gameTradeNo\":\"1001\",\"xgTradeNo\":\"\",\"channelCode\":\"\",\"channelMsg\":\"channel pay cancelled\"}");
		string msg = "Charge cancel Without Pay Info Checking";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			string responseStr = this.getSeverResponse (this.buildRequestDatas("cancel"));

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
					this.setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
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

	public void ChargeFailed()
	{
		Debug.Log ("start to charge : failed");
		bool checkPassed = false;
		string msg = "Charge failed Without Pay Info Checking";
		title.text = "payFailed参数校验结果";
		if ("check".Equals (ConfigsUtils.getTextByKey ("interfaceTestCheck"))) {
			Debug.Log ("start to charge : failed request str = "+this.buildRequestDatas("fail"));
			string responseStr = this.getSeverResponse (this.buildRequestDatas("fail"));
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
					this.setPayInfoCheckResult(responseObj["detail"] as List<System.Object>);
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

	public void goBackToPayPage(){
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("Exit channel pay page");
	}

	private string getSeverResponse(string datas){
		string url = ConfigsUtils.getTextByKey ("XgPortalUrl")+"/internal/sdkclient/interface-test";// 后续可以使用不同的url
		return ChannelClient.getResponse (url,  datas);
	}

	private Dictionary<string, object> buildPayInfo(PayInfo payInfo){
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
	
	
	private string buildRequestDatas(string callType){
		Dictionary<string, object> paramsMap = new Dictionary<string, object>();
		paramsMap.Add("payInfo", this.buildPayInfo(payInfo));
		
		Dictionary<string, object> datas = new Dictionary<string, object>();
		datas.Add ("userName", ConfigsUtils.getTextByKey ("userName"));
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
