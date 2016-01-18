using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Runtime.InteropServices;


public class XGSDK2GameCenter
{ 
	#if UNITY_IPHONE 
	[DllImport("__Internal")]
	private static extern string XGSDKPlayerInfo();

	[DllImport("__Internal")]
    private static extern void XGSDKSubmitScore(long score, string category);
    
	[DllImport("__Internal")]
	private static extern void XGSDKLoadLeaderboard();

	[DllImport("__Internal")]
    private static extern void XGSDKSubmitAchievement(float percent,string id);

	[DllImport("__Internal")]
	private static extern void XGSDKLoadAchievement();

	[DllImport("__Internal")]
    private static extern void XGSDKShowGamecenter();	
	#endif

	public static string GetPlayerInfo()
	{
		Debug.Log ("call XGSDK GetPlayerInfo");
		#if UNITY_IPHONE 
		return XGSDKPlayerInfo();
		#endif
	}

	public static void SubmitScore(long score,string category)
	{
		Debug.Log ("call XGSDK SubmitScore");
		#if UNITY_IPHONE 
		XGSDKSubmitScore(score,category);
		#endif
	}

	public static void LoadLeaderboard()
	{
		Debug.Log ("call XGSDK LoadLeaderboard");
		#if UNITY_IPHONE 
		XGSDKLoadLeaderboard();
		#endif
	}

	public static void SubmitAchievement(float percent,string id)
	{
		Debug.Log ("call XGSDK SubmitAchievement");
		#if UNITY_IPHONE 
		XGSDKSubmitAchievement(percent,id);
		#endif
	}

	public static void LoadAchievement()
	{
		Debug.Log ("call XGSDK LoadAchievement");
		#if UNITY_IPHONE 
		XGSDKLoadAchievement();
		#endif
	}

	public static void ShowGamecenter()
	{
		Debug.Log ("call XGSDK ShowGamecenter");
		#if UNITY_IPHONE 
		XGSDKShowGamecenter();
		#endif
	}		
}