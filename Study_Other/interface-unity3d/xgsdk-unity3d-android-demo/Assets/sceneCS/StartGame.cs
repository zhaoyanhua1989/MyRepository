using UnityEngine;
using System.Collections;
using ChannelUtils;

public class StartGame : MonoBehaviour {

	public static string verifiedInfo;
	// Use this for initialization
	void Start () {
		PlayerPrefs.SetString ("currentPage", "loginSuccess");
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
	public void switchAccount()
	{
		Debug.Log ("call switchAccount");
		XGSDK2.instance.switchAccount ("");
	}
	public void startGame()
	{
		Debug.Log ("start to login");
		Application.LoadLevel("mainGame");
	}

	void OnLevelWasLoaded(int level) {
		if (level == 1)
		{
			Debug.Log ("load loginSuccess scene success");
			if(!string.IsNullOrEmpty(verifiedInfo))
			{

				ChannelUtils.DialogHelper.getInstance().showCallbackTip(verifiedInfo);
			}
		}
		
	}

}
