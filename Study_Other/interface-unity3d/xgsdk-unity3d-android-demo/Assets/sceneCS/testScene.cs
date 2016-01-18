using UnityEngine;
using System.Collections;

public class testScene : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Debug.Log("test scene start");
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void OnLevelWasLoaded(int level)
	{
		if(level == 6)
		{
			Debug.Log("test scene OnLevelWasLoaded");
		}
	}
}
