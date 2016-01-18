using UnityEngine;
using System.Collections;

public class InputPageCtrl : MonoBehaviour 
{

	public GameObject	m_inputPageObject;

	void Start () 
	{
	
	}
	
	// Update is called once per frame
	void Update () 
	{
	
	}

	public void OnCancleClick()
	{
		m_inputPageObject.SetActive (false);
	}
}
