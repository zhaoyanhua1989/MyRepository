using UnityEngine;
using System.Collections;

public class PayInfo
{
	string uid;//账号ID
	string productId;//产品ID
	string productName;//产品名称
	string productDesc;//产品描述
	string productUnit;//产品单位：比如元宝或金币等
	int productUnitPrice;//产品单价,单位分
	int productQuantity;//产品数量
	int totalAmount;//总金额,单位分
	int payAmount;//付费金额,单位分
	string currencyName;//货币名称(人民币：CNY)
	string roleId;//角色ID
	string roleName;//角色名
	string roleLevel;//角色等级
	string roleVipLevel;//角色的VIP等级
	string serverId;//服ID
	string zoneId;//区ID
	string partyName;//帮会名称
	string virtualCurrencyBalance;//虚拟货币余额
	string customInfo;//扩展字段，订单支付成功后，透传给游戏
	string gameTradeNo;//游戏订单ID，支付成功后，透传给游戏
	string gameCallBackURL;//支付回调地址，如果为空，则后台配置的回调地址
	string additionalParams;// 扩展参数
	
	
	public string Uid 
	{
		set{ uid = value;}
		get{ return uid;}
	}
	public string ProductId 
	{
		set{ productId = value;}
		get{ return productId;}
	}
	
	public string ProductName 
	{
		set{ productName = value;}
		get{ return productName;}
	}
	
	public string ProductDesc 
	{
		set{ productDesc = value;}
		get{ return productDesc;}
	}
	public string ProductUnit
	{
		set{ productUnit = value;}
		get{ return productUnit;}
	}
	public int ProductUnitPrice 
	{
		set{ productUnitPrice = value;}
		get{ return productUnitPrice;}
	}
	public int ProductQuantity 
	{
		set{ productQuantity = value;}
		get{ return productQuantity;}
	}
	
	public int TotalAmount 
	{
		set{ totalAmount = value;}
		get{ return totalAmount;}
	}
	public int PayAmount
	{
		set{ payAmount = value;}
		get{ return payAmount;}
	}
	public string CurrencyName 
	{
		set{ currencyName = value;}
		get{ return currencyName;}
	}
	public string RoleId 
	{
		set{ roleId = value;}
		get{ return roleId;}
	}
	
	public string RoleName 
	{
		set{ roleName = value;}
		get{ return roleName;}
	}
	public string RoleLevel
	{
		set{ roleLevel = value;}
		get{return roleLevel;}
	}
	public string RoleVipLevel
	{
		set{ roleVipLevel = value;}
		get{ return roleVipLevel;}
	}
	public string ServerId 
	{
		set{ serverId = value;}
		get{ return serverId;}
	}
	public string ZoneId 
	{
		set{ zoneId = value;}
		get{ return zoneId;}
	}
	public string PartyName
	{
		set{ partyName = value;}
		get{ return partyName;}
	}
	public string VirtualCurrencyBalance
	{
		set{virtualCurrencyBalance = value;}
		get{ return virtualCurrencyBalance;}
	}
	public string CustomInfo
	{
		set{ customInfo = value;}
		get{ return customInfo;}
	}
	public string GameTradeNo 
	{
		set{ gameTradeNo = value;}
		get{ return gameTradeNo;}
	}
	public string GameCallBackURL
	{
		set{ gameCallBackURL = value;}
		get{ return gameCallBackURL;}
	}
	public string AdditionalParams 
	{
		set{ additionalParams = value;}
		get{ return additionalParams;}
	}
	
}
