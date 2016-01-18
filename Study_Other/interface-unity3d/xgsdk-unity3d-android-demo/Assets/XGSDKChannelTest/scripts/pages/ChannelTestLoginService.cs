using UnityEngine;
using System.Collections;

public class ChannelTestLoginService: MonoBehaviour {

	void Start()
	{
		initLoginPage ();
	}

	// ######## login page ########
	// login page params start
	private static GameObject loginPage;
	//private GameObject root;

	private UIButton checkBox;	 
	private UIButton closeButton;	 
	private UIButton loginSuccessButton;	 
	private UIButton loginFailedButton;	 
	private UIButton loginCancelButton;	
	private UISprite checkBoxSprite;
	private UIInput inputPassword;
	private UIInput inputUserName;
	// login page params end

	public static void initChannelTestLoginService(GameObject root)
	{
		Debug.Log ("initChannelTestLoginService start");
		if (null != root) {
			GameObject obj = GameObject.Find ("channelTestLogin");
			if(obj == null){
				loginPage = NGUITools.AddChild(root,(GameObject)(Resources.Load ("channelTestLogin")));
				loginPage.AddComponent<ChannelTestLoginService>();		
				//UnityEngine.Object.DontDestroyOnLoad(loginPage);
			}
		}
		
		Debug.Log("initChannelTestLoginService end");
	}
	
	public static bool activeLoginPage (bool isActive){
		if (loginPage == null) {
			return false;
		}
		
		if (isActive) {
			if(!loginPage.activeSelf){
				loginPage.SetActive (true);
			}
		} else {
			loginPage.SetActive (false);
		}
		return true;
	}
	
	private void initLoginPage(){
		
		//GameObject uiRootObject = GameObject.Find ("UI Root");
		this.initLoginComponets ();
		if (loginPage.activeSelf) {
			loginPage.SetActive (false);
		}
	}
	
	private void initLoginComponets()
	{
		checkBox = GameObject.Find("CheckBox").GetComponent<UIButton>();
		checkBoxSprite =  GameObject.Find("Checkmark").GetComponent<UISprite>();
		closeButton = GameObject.Find("CloseButton").GetComponent<UIButton>();
		loginSuccessButton = GameObject.Find("LoginSuccessButton").GetComponent<UIButton>();
		loginFailedButton = GameObject.Find("LoginFailedButton").GetComponent<UIButton>();
		loginCancelButton = GameObject.Find("LoginCancelledButton").GetComponent<UIButton>();
		inputUserName = GameObject.Find("InputUserName").GetComponent<UIInput>();
		inputPassword = GameObject.Find("InputPassword").GetComponent<UIInput>();
		this.bindEventForLogin ();
	}
	
	private void bindEventForLogin()
	{
		// bing event for checkbox in login page
		EventDelegate onCheckBoxClick = new EventDelegate(this, "onCheckBoxClick");
		checkBox.onClick.Add (onCheckBoxClick);
		//eventDelegate.parameters[0] = new EventDelegate.Parameter(this, "param");
		
		// bing event for close btn in login page
		EventDelegate onCloseBtnClick = new EventDelegate(this, "onCloseBtnClick");
		closeButton.onClick.Add (onCloseBtnClick);
		
		// bing event for login success btn in login page
		EventDelegate onLoginSuccessBtnClick = new EventDelegate(this, "onLoginSuccessBtnClick");
		loginSuccessButton.onClick.Add (onLoginSuccessBtnClick);
		
		// bing event for close btn in login page
		EventDelegate onLoginFailedBtnClick = new EventDelegate(this, "onLoginFailedBtnClick");
		loginFailedButton.onClick.Add (onLoginFailedBtnClick);
		
		// bing event for close btn in login page
		EventDelegate onLoginCancelBtnClick = new EventDelegate(this, "onLoginCancelBtnClick");
		loginCancelButton.onClick.Add (onLoginCancelBtnClick);
	}
	
	public void onCheckBoxClick(){	
		
		if (checkBoxSprite.spriteName == "check") {
			Debug.Log ("#### check to checked");
			checkBoxSprite.spriteName = "checked";
			ConfigsUtils.put ("interfaceTestCheck", "check");
		} else if (checkBoxSprite.spriteName == "checked"){
			Debug.Log ("#### checked to check");
			checkBoxSprite.spriteName = "check";
			ConfigsUtils.put ("interfaceTestCheck", "uncheck");
		}
		
	}
	
	public void onCloseBtnClick()
	{
		if (loginPage.activeSelf) {
			loginPage.SetActive (false);
		}
	}
	
	public void onLoginSuccessBtnClick(){
		if (ChannelTestLoginDataBuilder.validateUserNameAndPassword (inputUserName.value, inputPassword.value)) {
			ChannelTestLoginDataBuilder.LoginSuccess ();
		}
	}
	
	public void onLoginFailedBtnClick(){
		if (ChannelTestLoginDataBuilder.validateUserNameAndPassword (inputUserName.value, inputPassword.value)) {
			ChannelTestLoginDataBuilder.LoginFailed ();
		}
	}
	
	public void onLoginCancelBtnClick(){
		if (ChannelTestLoginDataBuilder.validateUserNameAndPassword (inputUserName.value, inputPassword.value)) {
			ChannelTestLoginDataBuilder.Cancel ();
		}
	}

}
