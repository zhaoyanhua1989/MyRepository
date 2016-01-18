using UnityEngine;
using System.Collections;
using ChannelUtils;

public class LoginGame : MonoBehaviour {

	public static string logoutFinishTip;

	void Awake()
	{
		XGSDKCallbackWrapper.CreateSDKManager ();
	}

	// Use this for initialization
	void Start () {
		Debug.Log ("login game start");
		#if UNITY_ANDROID || UNITY_IPHONE 
			XGSDK2.instance.setCallback ();
		#endif
		XGSDK2.instance.initSdk ("");
	}
	
	// Update is called once per frame
	void Update () {
		if(Application.platform == RuntimePlatform.Android && Input.GetKeyDown(KeyCode.Escape))
		{
			DialogHelper dialogInstance = ChannelUtils.DialogHelper.getInstance();
			if(dialogInstance.ConfirmDialog != null)
			{
				dialogInstance.ConfirmDialog.SetActive(false);
				dialogInstance.DialogBackground.SetActive(false);
				dialogInstance.ConfirmDialog = null;
			}else{
				XGSDK2.instance.exit("");
			}


		}
	}

	public void Login()
	{
		Debug.Log ("start to login");
		XGSDK2.instance.login ("");

	}

	public void Exit()
	{
		Debug.Log ("start to login");
		XGSDK2.instance.exit ("");
		
	}



	void OnLevelWasLoaded(int level)
	{
		if(level == 0)
		{
			Debug.Log ("logoutFinishTip");
			if(!string.IsNullOrEmpty(logoutFinishTip))
			{
				ChannelUtils.DialogHelper.getInstance().showCallbackTip("logoutFinishTip is " + logoutFinishTip);
			}
		}
	}

	
}
