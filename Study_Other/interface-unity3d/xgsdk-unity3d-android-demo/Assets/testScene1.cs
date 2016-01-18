using UnityEngine;
using System.Collections;

public class testScene1 : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Debug.Log ("this is start");
		showPayDialog ();
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public void showPayDialog()
	{
		GameObject uiRootObject = GameObject.Find ("UI Root");
		GameObject dialogBackground = NGUITools.AddChild(uiRootObject,(GameObject)(Resources.Load ("dialogBackground")));
		GameObject payDialog = NGUITools.AddChild (uiRootObject, (GameObject)(Resources.Load ("inputDialog")));
		UISprite bgSprite =  dialogBackground.GetComponent<UISprite>();
		//bgSprite.alpha = 0.2f;
		bgSprite.width = 1280;
		bgSprite.height = 720;	
		bgSprite.depth = 1;
		//bgSprite.depth = 3;
		Debug.Log (dialogBackground.activeSelf + "xxxx");
		dialogBackground.SetActive (false);
		Debug.Log (dialogBackground.activeSelf + "xxxx");
		
		intPayDialogSprite (payDialog);
	}
	
	public void intPayDialogSprite(GameObject payDialog)
	{
		UISprite payDialogSprite = payDialog.GetComponent<UISprite>();
		//payDialogSprite.depth = 4;
		
		Transform[] trans = payDialog.GetComponentsInChildren<Transform> ();
		foreach(Transform tran in trans)
		{

			//Debug.Log(tran.GetType());//UnityEngine.Transform
			if(string.Equals("title label",tran.name))
			{
				UILabel title = tran.GetComponent<UILabel>();
				Debug.Log (title.depth);
				//title.depth = 7;
				tran.GetComponent<UILabel>().text = "支付";
			}
			Debug.Log (tran.name);
			if(string.Equals("inputField1",tran.name))
			{

				UIInput commodityInput = tran.GetComponent<UIInput>();
				commodityInput.value= "1";
				UILabel label1 = commodityInput.GetComponentInChildren<UILabel>();
				label1.text = "lalalalla";
				Debug.Log (commodityInput.value + "xxx");
			}
			
		}
	}



}
