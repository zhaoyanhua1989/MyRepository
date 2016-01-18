using UnityEngine;
using System.Collections;

public class RoleInfo
{
	string uid;//账号ID
	string roleId;//角色ID
	string roleType;//角色类型
	string roleLevel;//角色等级
	string roleVipLevel;//角色Vip等级
	string serverId;//服Id
	string zoneId;//区ID
	string roleName;//角色ID
	string serverName;//服名称
	string zoneName;//区名称
	string partyName;//帮会名称
	string gender;//性别
	string balance;
	
	public string Uid
	{
		set{ uid = value;}
		get{ return uid;}
	}
	public string RoleId 
	{
		set{ roleId = value;}
		get{ return roleId;}
	}
	public string RoleType
	{
		set{ roleType = value;}
		get{ return roleType;}
	}
	public string RoleLevel 
	{
		set{ roleLevel = value;}
		get{ return roleLevel;}
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
	public string RoleName 
	{
		set{ roleName = value;}
		get{ return roleName;}
	}
	public string ServerName
	{
		set{ serverName = value;}
		get{ return serverName;}
	}
	public string ZoneName
	{
		set{zoneName = value;}
		get{ return zoneName;}
	}
	public string PartyName 
	{
		set{ partyName = value;}
		get{ return partyName;}
	}
	public string Gender 
	{
		set{ gender = value;}
		get{ return gender;}
	}
	public string Balance
	{
		set{balance = value;}
		get{return balance;}
	}
}
