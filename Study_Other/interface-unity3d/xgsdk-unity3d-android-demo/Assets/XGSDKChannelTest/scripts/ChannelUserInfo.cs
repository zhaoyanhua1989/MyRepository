using UnityEngine;
using System.Collections;

public class ChannelUserInfo : MonoBehaviour {

	public UILabel userName;
	
	public UILabel totalMoney;

	public UILabel totalGame;

	void Awake()
	{
		//todo

	}
	
	// Use this for initialization
	void Start () {
		string currentUserName = ConfigsUtils.getTextByKey ("userName");
		//Debug.Log("currentUserName "+ currentUserName);  
		if (string.IsNullOrEmpty(currentUserName) ){
			userName.text = " ";
		} else {
			userName.text = currentUserName;
		}

		if (string.IsNullOrEmpty(ConfigsUtils.getTextByKey ("totalMoney"))) {
			totalMoney.text = "0";
		}else{
			totalMoney.text = ConfigsUtils.getTextByKey ("totalMoney");
		}

		if (string.IsNullOrEmpty(ConfigsUtils.getTextByKey ("totalMoney"))) {
			totalGame.text = "0";
		}else{
			totalGame.text = ConfigsUtils.getTextByKey ("totalMoney");
		}

	}
	
	// Update is called once per frame
	void Update () {
		// todo
	}

	public void goBack(){

		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("Exit channel user center");
	}
}
