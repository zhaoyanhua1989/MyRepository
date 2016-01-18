using UnityEngine;
using System.Collections;
using System;

public class ChannelValidator{

	private static bool isString(object value){
		if (null != value && value.GetType().Name == "String") {
			return true;
		}
		return false;
	}

	private static bool isInt(object value){
		if (null != value && value.GetType().Name.IndexOf ("Int") > -1) {
			return true;
		}
		return false;
	}

	private static bool isBoolean(object value){
		if (null != value && value.GetType().Name == "Boolean") {
			return true;
		}
		return false;
	}
	
	// validatot for pay params
	// index : name index;
	// value : the index value;
	// return : ture or false, false as default
	public static ValidateResult payInfoValidator(string index,object value){
		if (string.IsNullOrEmpty (index)) {
			return new ValidateResult(false,"please input correct index!");
		}

		ValidateResult result = new ValidateResult (false, "no msg");
		switch(index)
		{
		//  必填，游戏必须使用登录时西瓜服务器返回的uid，且最大长度128;
		case "uid":
			//todo call the xg server to validate the uid existence
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 128)
			{
				result.setIsValid(false);
				result.setValidationMsg("uid is empty or its length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("uid is ok");
			}
			break;
		// 非必填，产品ID，最大长度64
		case "productId":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("productId length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productId is ok");
			}
			break;
		// 非必填，产品名称，最大长度64
		case "productName":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("productName length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productName is ok");
			}
			break;
		// 非必填，产品描述，最大长度128
		case "productDesc":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 128){
				result.setIsValid(false);
				result.setValidationMsg("productDesc length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productDesc is ok");
			}
			break;
		// 非必填，产品单位，最大长度64
		case "productUnit":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("productUnit length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productUnit is ok");
			}
			break;
		// 非必填，产品单价,单位分，最大长度10
		case "productUnitPrice":
			if(value != null && isInt(value) && (value.ToString().Length > 10 || Convert.ToInt64(value) < 0)){
				result.setIsValid(false);
				result.setValidationMsg("productUnitPrice must be greater than zero and not longger than 10");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productUnitPrice is ok");
			}
			break;
		// 非必填，产品数量，最大长度10
		case "productQuantity":
			if(value != null && isInt(value) && (value.ToString().Length > 10 || Convert.ToInt64(value) < 0)){
				result.setIsValid(false);
				result.setValidationMsg("productUnit must be greater than zero and not longger than 10");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("productUnit is ok");
			}
			break;
		// 必填，产品总额,单位分,最大长度10
		case "totalAmount":
			if(value == null || !isInt(value) || (value.ToString().Length > 10 || Convert.ToInt64(value) < 0)){
				result.setIsValid(false);
				result.setValidationMsg("totalAmount must be greater than zero and not longger than 10 and not be null");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("totalAmount is ok");
			}
			break;
		// 必填，付费总额,单位分,最大长度10
		case "payAmount":
			if(value == null || !isInt(value) || (value.ToString().Length > 10 || Convert.ToInt64(value) < 0)){
				result.setIsValid(false);
				result.setValidationMsg("payAmount must be greater than zero and not longger than 10 and not be null");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("payAmount is ok");
			}
			break;
		// 非必填，实际支付的国际标准货币代码,比如CNY(人民币)/USD(美元)，最大长度64
		case "currencyName":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("currencyName length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("currencyName is ok");
			}
			break;
		//  必填，角色ID，且最大长度32；
		case "roleId":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleId is empty or its length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleId is ok");
			}
			break;
		// 非必填，角色名称，最大长度64
		case "roleName":
			if(value != null && !string.IsNullOrEmpty(value.ToString()) && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("roleName length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleName is ok");
			}
			break;
		// 非必填，角色等级，最大长度32
		case "roleLevel":
			if(value != null && isString(value) && ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleLevel length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleLevel is ok");
			}
			break;
		// 非必填，角色vip等级，最大长度32
		case "roleVipLevel":
			if(value != null && isString(value) && ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleVipLevel length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleVipLevel is ok");
			}
			break;
			// 非必填，服ID，最大长度32
		case "serverId":
			if(value != null && isString(value) && ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("serverId length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("serverId is ok");
			}
			break;

			// 非必填，区ID，最大长度32
		case "zoneId":
			if(value != null && isString(value) && ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleVipLevel length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleVipLevel is ok");
			}
			break;

			// 非必填，帮会名称，最大长度32
		case "partyName":
			if(value != null && isString(value) && ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("partyName length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("partyName is ok");
			}
			break;

			//TODO 长度未知 需要确认
			// 非必填，虚拟货币余额
		case "virtualCurrencyBalance":
			if(value != null && !isString(value)){
				result.setIsValid(false);
				result.setValidationMsg("virtualCurrencyBalance is not a string");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("virtualCurrencyBalance is ok");
			}
			break;

			// 非必填，扩展字段，订单支付成功后，透传给游戏，最大长度2000
		case "customInfo":
			if(value != null && isString(value) && ((string)value).Length > 2000){
				result.setIsValid(false);
				result.setValidationMsg("customInfo length excced 2000");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("customInfo is ok");
			}
			break;

			// 非必填，游戏订单ID，支付成功后，透传给游戏，最大长度64
		case "gameTradeNo":
			if(value != null && isString(value) && ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("gameTradeNo length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("gameTradeNo is ok");
			}
			break;

			// 非必填，帮会名称，最大长度128
		case "gameCallbackUrl":
			if(value != null && isString(value) && ((string)value).Length > 128){
				result.setIsValid(false);
				result.setValidationMsg("gameCallbackUrl length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("gameCallbackUrl is ok");
			}
			break;

			//TODO 长度未知 需要确认
			// 非必填，扩展参数
		case "additionalParams":
			if(value != null && !isString(value)){
				result.setIsValid(false);
				result.setValidationMsg("additionalParams is not s string");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("additionalParams is ok");
			}
			break;
		default:
			break;
		}
		return result;
	}

	// validatot for role info params
	// index : name index;
	// value : the index value;
	// return : ture or false, false as default
	public static ValidateResult roleInfoValidator(string index,object value){
		if (string.IsNullOrEmpty (index)) {
			return new ValidateResult(false,"please input correct index!");
		}
		
		ValidateResult result = new ValidateResult (false, "no msg");
		switch (index){
			//  必填，游戏必须使用登录时西瓜服务器返回的uid，且最大长度128;
		case "uid":
			//todo call the xg server to validate the uid existence
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 128){
				result.setIsValid(false);
				result.setValidationMsg("uid is empty or its length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("uid is ok");
			}
			break;
			//  必填，角色ID，且最大长度32；
		case "roleId":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleId is empty or its length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleId is ok");
			}
			break;
			// 必填，角色类型，最大长度20
		case "roleType":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 20){
				result.setIsValid(false);
				result.setValidationMsg("roleType is empty or its length excced 20");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleType is ok");
			}
			break;
			// 必填,角色等级，最大长度32
		case "roleLevel":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("roleLevel is empty or its length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleLevel is ok");
			}
			break;
			// 非必填,角色等级，最大长度32
		case "roleVipLevel":
			if((value != null && isString(value) && ((string)value).Length > 32 ) || (value != null && !isString(value))){
				result.setIsValid(false);
				result.setValidationMsg("roleVipLevel length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleVipLevel is ok");
			}
			break;
			// 必填,最大长度32,游戏服ID，示例：s1,s2
		case "serverId":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 32){
				result.setIsValid(false);
				result.setValidationMsg("serverId is empty or its length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("serverId is ok");
			}
			break;
			// 非必填,最大长度32,游戏区ID
		case "zoneId":
			if((value != null && !isString(value)) || (value != null && string.IsNullOrEmpty(value.ToString()) && ((string)value).Length > 32 )){
				result.setIsValid(false);
				result.setValidationMsg("zoneId length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("zoneId is ok");
			}
			break;
			// 必填,最大长度64,角色名
		case "roleName":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("roleName is empty or its length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("roleName is ok");
			}
			break;
			// 必填,最大长度64,游戏服名称，示例：风云争霸
		case "serverName":
			if(value == null || string.IsNullOrEmpty(value.ToString()) || !isString(value) || ((string)value).Length > 64){
				result.setIsValid(false);
				result.setValidationMsg("serverName is empty or its length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("serverName is ok");
			}
			break;
			// 非必填,最大长度64,游戏区名称
		case "zoneName":
			if((value != null && !isString(value)) || (value != null && string.IsNullOrEmpty(value.ToString()) && ((string)value).Length > 64 )){
				result.setIsValid(false);
				result.setValidationMsg("zoneName length excced 64");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("zoneName is ok");
			}
			break;
			// 非必填,最大长度32,游戏区ID
		case "partyName":
			if((value != null && !isString(value)) || (value != null && string.IsNullOrEmpty(value.ToString()) && ((string)value).Length > 32 )){
				result.setIsValid(false);
				result.setValidationMsg("partyName length excced 32");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("partyName is ok");
			}
			break;
			// 必填,枚举值：m,f;分别代表男女,角色性别
		case "gender":
			if(value == null || !isString(value) || ((string)value != "f" || (string)value != "m")){
				result.setIsValid(false);
				result.setValidationMsg("gender is invalid");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("gender is ok");
			}
			break;
			// 非必填,最大长度128(待定),角色账户余额
		case "balance":
			if((value != null && !isString(value)) || (value != null && string.IsNullOrEmpty(value.ToString()) && ((string)value).Length > 128 )){
				result.setIsValid(false);
				result.setValidationMsg("balance length excced 128");
			}else{
				result.setIsValid(true);
				result.setValidationMsg("balance is ok");
			}
			break;
		default:
			break;
		}
		return result;
	}
}
