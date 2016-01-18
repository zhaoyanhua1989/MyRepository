using UnityEngine;
using System.Collections;

public class Result
{
	public const string CODE_SUCCESS = "0";
	private string code;
	private string msg;
	private string data;
	private string orderId;
	
	public string Code
	{
		set{code = value;}
		get{ return code;}
	}
	
	public string Msg
	{
		set{msg = value;}
		get{ return msg;}
	}
	
	public string Data
	{
		set{data = value;}
		get{ return data;}
	}
	
	public string OrderId
	{
		set{orderId = value;}
		get{ return orderId;}
	}
	
	public string toJsonStr()
	{
		return XGSDKMiniJSON.Json.Serialize(this);
	}
	
	public static Result fromJsonStr(string jsonStr)
	{
		return XGSDKMiniJSON.Json.Deserialize (jsonStr) as Result;
	}
}
