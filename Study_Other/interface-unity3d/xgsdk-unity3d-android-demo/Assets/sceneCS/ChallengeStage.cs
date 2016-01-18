using UnityEngine;
using System;
using System.Collections;
using ChannelUtils;

public class ChallengeStage : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
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


	public void challengeSuccess()
	{
		XGSDK2.instance.onMissionSuccess(mainGame.roleInfo,"TestMissionId", "TestMissionName", "");
		mainGame.roleInfo.RoleLevel = Convert.ToString(int.Parse (mainGame.roleInfo.RoleLevel) + 1);
		XGSDK2.instance.onRoleLevelup(mainGame.roleInfo);
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("challengeSuccess,role level up one");
	}

	public void challengeFail()
	{
		XGSDK2.instance.onMissionFail(mainGame.roleInfo,"TestMissionId", "TestMissionName", "");
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("challengeFail");
	}

	public void backToMainGame()
	{
		Application.LoadLevel ("mainGame");
	}


}
