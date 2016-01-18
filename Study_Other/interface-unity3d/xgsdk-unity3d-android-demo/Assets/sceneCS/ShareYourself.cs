using UnityEngine;
using System.Collections;

public class ShareYourself : MonoBehaviour {

	//public static string filePath = Application.persistentDataPath + "/screenShot.png";
	public static string uid = "unity_123";
	public static string roleId = "unity_1234";
	public static string weichatfriend = "weichatfriend";
	public static string weichatzone = "weichatzone";
	public static string weibo = "weibo";
	public static string qq = "qq";
	public static string qqzone = "qqzone";
	public static string xgPhotoShareActivity = "xgPhotoShareActivity";

	public static string photoName = "Screenshot.png";
	public static string title = "Screenshot title";
	public static string description = "Screenshot description";
	public static string targetUrl = "http://www.baidu.com";
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public void weichatfriendShare()
	{
		XGSDK2.instance.xgShareLocalImage (uid,roleId,weichatfriend,photoName,title,description,targetUrl);
	}

	public void weichatzoneShare()
	{
		XGSDK2.instance.xgShareLocalImage (uid,roleId,weichatzone,photoName,title,description,targetUrl);
	}


	public void weiboShare()
	{
		XGSDK2.instance.xgShareLocalImage (uid,roleId,weibo,photoName,title,description,targetUrl);
	}

	public void QQShare()
	{
		XGSDK2.instance.xgShareLocalImage (uid,roleId,qq,photoName,title,description,targetUrl);
	}
	
	public void QQqzoneShare()
	{
		XGSDK2.instance.xgShareLocalImage (uid,roleId,qqzone,photoName,title,description,targetUrl);
	}

	public void photoShare()
	{
		XGSDK2.instance.xgPhotoShareActivity (uid,roleId,title, description, targetUrl);
	}

	public void goBackToMainGame()
	{
		Application.LoadLevel ("mainGame");
	}


	public void screenShot()
	{
		//Utils.Utils.CaptureScreenshot2( new Rect( Screen.width*0f, Screen.height*0f, Screen.width*1f, Screen.height*1f),filePath);
	}

}
