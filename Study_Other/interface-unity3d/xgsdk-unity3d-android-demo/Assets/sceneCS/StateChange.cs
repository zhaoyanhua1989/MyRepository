using UnityEngine;
using System.Collections;

public class StateChange : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public string m_strSenceName;

	public void Change()
	{
		if(m_strSenceName == "pay"){
			Pay.payInfo = "";
		}
		Debug.Log ("enter >>>>>"+m_strSenceName);
		Application.LoadLevel (m_strSenceName);

		XGSDK2.instance.onMissionBegin(mainGame.roleInfo,"TestMissionId", "TestMissionName", "");
	}

}
