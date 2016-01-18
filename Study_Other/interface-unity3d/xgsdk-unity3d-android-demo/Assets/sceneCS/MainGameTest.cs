using UnityEngine;
using System.Collections;

public class MainGameTest : MonoBehaviour {

	public UISprite bg;
	public UISprite dialog;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public void bgPress()
	{
		Debug.Log ("background");
	}

	public void pay()
	{
		Debug.Log ("pay button press");
	}

	public void okPress()
	{
		bg.gameObject.SetActive (false);
		dialog.gameObject.SetActive (false);
	}
}
