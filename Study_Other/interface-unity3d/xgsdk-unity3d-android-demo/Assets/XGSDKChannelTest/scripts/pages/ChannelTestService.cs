using UnityEngine;
using System.Collections;

public class ChannelTestService : MonoBehaviour {

	public static GameObject root;

	void Start()
	{
		ChannelTestLoginService.initChannelTestLoginService (root);
		ChannelTestUserCenterService.initChannelTestUserCenterService (root);
		ChannelTestPayService.initChannelTestPayService (root);
		initChannelTestDraggableWindow ();
	}

	public static void initChannelTestService()
	{
		Debug.Log ("init ChannelTestWindow start");
		if (null == root) {
			root = new GameObject("ChannelTestService");
		}
		root.AddComponent<ChannelTestService>();		
		UnityEngine.Object.DontDestroyOnLoad(root);

		Debug.Log("init ChannelTestWindow end");
	}

	public static void initChannelTestDraggableWindow()
	{
		Debug.Log ("init ChannelWindow start");
		//if (null == root) {
		//GameObject obj = GameObject.Find ("ChannelWindow");
		//if(obj == null){
		GameObject windowRoot = GameObject.Find ("XGSDKCallbackWrapper");
		if(windowRoot.GetComponent(typeof(ChannelWindow)) == null){
			windowRoot.AddComponent<ChannelWindow>();		
			//UnityEngine.Object.DontDestroyOnLoad(windowRoot);
		}
		//}
		
		Debug.Log("init ChannelWindow end");
	}

	public static void activeLoginPage(bool isActive){
		if (!ChannelTestLoginService.activeLoginPage (isActive)) {
			initChannelTestService();
			//ChannelTestLoginService.initChannelTestLoginService (root);
			ChannelTestLoginService.activeLoginPage (isActive);
		}
	}

	public static void activeUserCenter(bool isActive){
		if (!ChannelTestUserCenterService.activeUserCenter (isActive)) {
			initChannelTestService();
			//ChannelTestUserCenterService.initChannelTestUserCenterService (root);
			ChannelTestUserCenterService.activeUserCenter (isActive);
		}
	}

	public static void activePayPage(bool isActive){
		if (!ChannelTestPayService.activePayPage (isActive)) {
			initChannelTestService();
			//ChannelTestUserCenterService.initChannelTestUserCenterService (root);
			ChannelTestPayService.activePayPage (root);
		}
	}

	public static void switchAccount(){
		ChannelTestLoginDataBuilder.switchAccount();
	}
}
