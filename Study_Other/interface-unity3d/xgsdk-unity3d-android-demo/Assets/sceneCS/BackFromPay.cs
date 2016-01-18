using UnityEngine;
using System.Collections;
using ChannelUtils;

public class BackFromPay : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Debug.Log("challenge start");
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public void back()
	{
		Application.LoadLevel ("mainGame");
	}

	public void gotoTestScene()
	{
		Application.LoadLevel(6);
	}
}
