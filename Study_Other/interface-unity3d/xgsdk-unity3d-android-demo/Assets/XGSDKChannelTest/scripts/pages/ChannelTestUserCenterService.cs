using UnityEngine;
using System.Collections;

public class ChannelTestUserCenterService: MonoBehaviour {

	void Start()
	{
		initUserCenter ();
	}

	//######## user center ########
	
	// user center page params start
	private static GameObject userCenterPage;
	
	private UILabel userName;	
	private UILabel totalMoney;	
	private UILabel totalGame;
	private UIButton goBackButton;
	
	// user center page params end

	public static void initChannelTestUserCenterService(GameObject root)
	{
		Debug.Log ("init ChannelTestWindow start");
		if (null != root) {
			userCenterPage = NGUITools.AddChild(root,(GameObject)(Resources.Load ("channeTestUsercenter")));
			userCenterPage.AddComponent<ChannelTestUserCenterService>();		
			//UnityEngine.Object.DontDestroyOnLoad(userCenterPage);
		}
		
		Debug.Log("init ChannelTestWindow end");
	}

	private void initUserCenter(){
		//GameObject uiRootObject = GameObject.Find ("UI Root");
		//userCenterPage = NGUITools.AddChild(root,(GameObject)(Resources.Load ("channeTestUsercenter")));
		initUserCenterComponets ();
		if (userCenterPage.activeSelf) {
			userCenterPage.SetActive (false);
		}
	}
	
	private void initUserCenterComponets()
	{
		userName = GameObject.Find("Label_username_value").GetComponent<UILabel>();
		totalMoney =  GameObject.Find("Label_totalmoney_value").GetComponent<UILabel>();
		totalGame = GameObject.Find("Label_playinggameno_value").GetComponent<UILabel>();
		goBackButton = GameObject.Find("goBackButton").GetComponent<UIButton>();
		this.bindEventForUserCenter ();
		this.initDatas ();
	}
	
	private void bindEventForUserCenter(){
		// bing event for backbtn in user center page
		EventDelegate onGoBackButtonClick = new EventDelegate(this, "onGoBackButtonClick");
		goBackButton.onClick.Add (onGoBackButtonClick);
		//eventDelegate.parameters[0] = new EventDelegate.Parameter(this, "param");
	}

	private void initDatas(){
		userName.text = ConfigsUtils.getTextByKey("channeltest_userName");
		totalMoney.text = ConfigsUtils.getTextByKey("channeltest_totalMoney");
		totalGame.text = ConfigsUtils.getTextByKey("channeltest_totalGame");
	}
	
	private void onGoBackButtonClick(){
		if (userCenterPage.activeSelf) {
			userCenterPage.SetActive (false);
		}
	}
	
	public static bool activeUserCenter (bool isActive){
		if (userCenterPage == null) {
			return false;
		}
		
		if (isActive) {
			if(!userCenterPage.activeSelf){
				userCenterPage.SetActive (true);
			}
		} else {
			userCenterPage.SetActive (false);
		}

		return true;
	}
}
