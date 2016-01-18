using UnityEngine;
using System.Collections;

namespace Utils
{
	public class Utils : MonoBehaviour{

		public static IEnumerator hideDialog(float sec,GameObject dialog)
		{
			yield return new WaitForSeconds (sec);
			//Code here will be executed after 3 secs
			dialog.SetActive (false);
		}


		public static void CaptureScreenshot2(Rect rect,string filePath)   
		{  
			// 先创建一个的空纹理，大小可根据实现需要来设置  
			Texture2D screenShot = new Texture2D((int)rect.width, (int)rect.height, TextureFormat.RGB24,false);  
			
			// 读取屏幕像素信息并存储为纹理数据，  
			screenShot.ReadPixels(rect, 0, 0);  
			screenShot.Apply();  
			
			// 然后将这些纹理数据，成一个png图片文件  
			byte[] bytes = screenShot.EncodeToPNG();  
			//string filename = Application.persistentDataPath + "/Screenshot.png";
			//string filename = "/data/data/com.seasun.powerking.sdklibrary/files/dest.png";
			System.IO.File.WriteAllBytes(filePath, bytes);  
			Debug.Log(string.Format("截屏了一张图片: {0}", filePath));  
			
			// 最后，我返回这个Texture2d对象，这样我们直接，所这个截图图示在游戏中，当然这个根据自己的需求的。  
			//return screenShot;  
		}  
		
	}

	public class DialogHelper:MonoBehaviour
	{
		GameObject confirmDialog;
		GameObject dialogBackground;

		public enum Result
		{
			OK,CANCEL
		}

		private static DialogHelper instance = null;
		
		public static DialogHelper getInstance()
		{
			if(instance == null)
			{
				GameObject ob = new GameObject("DialogHelper");
				ob.AddComponent<DialogHelper>();
				
				instance = ob.GetComponent<DialogHelper>();
			}
			return instance;
		}
		
		public void showCallbackTip(string tip)
		{
			Debug.Log ("callback tip is " + tip);
			GameObject uiRootObject = GameObject.Find ("UI Root");//how to find root
			GameObject dialog = NGUITools.AddChild (uiRootObject, (GameObject)(Resources.Load ("callbackdialog_1")));
			UILabel contentLabel = dialog.GetComponentInChildren<UILabel> ();
			UISprite uis = dialog.GetComponent<UISprite>();
			uis.height = contentLabel.height + 50;
			contentLabel.text = tip;
			StartCoroutine(Utils.hideDialog(2.0f,dialog));
		}

		public void showQuitConfirmDialog()
		{
			if(confirmDialog == null)
			{
				Debug.Log ("pop quit dialog");
				GameObject uiRootObject = GameObject.Find ("UI Root");
				dialogBackground = NGUITools.AddChild(uiRootObject,(GameObject)(Resources.Load ("dialogBackground")));
				confirmDialog = NGUITools.AddChild (uiRootObject, (GameObject)(Resources.Load ("confirmDialog")));
				UISprite bgSprite =  dialogBackground.GetComponent<UISprite>();
				//bgSprite.alpha = 0.2f;
				bgSprite.width = 1280;
				bgSprite.height = 720;

				bgSprite.depth = 12;
				UISprite confirmSprite = confirmDialog.GetComponent<UISprite>();
				//confirmSprite.depth = 13;

				setDialogDepth(confirmDialog);

				Component[] btns = confirmDialog.GetComponentsInChildren<UIButton> ();
				foreach(Component com in btns)
				{
					Debug.Log("com name " + com.name);
					if(string.Equals("okBtn",com.name))
					{
						UIEventListener.Get(com.gameObject).onClick = OnSubmitHandler;
					}
					if(string.Equals("cancelBtn",com.name))
					{
						UIEventListener.Get(com.gameObject).onClick = OnCancelHandler;
					}
				}
			}



		}

		private void OnSubmitHandler(GameObject obj)
		{
			Debug.Log ("okBtn is lanuch");
			confirmDialog = null;
			dialogBackground = null;
			instance = null;
			XGSDK2.instance.releaseResource("release source");
			Application.Quit ();
		}

		private void OnCancelHandler(GameObject obj)
		{		
			Debug.Log ("cancelBtn is lanuch");
			confirmDialog.SetActive (false);
			dialogBackground.SetActive (false);
			confirmDialog = null;
			dialogBackground = null;

		}

		public GameObject ConfirmDialog{
			set{confirmDialog = value;}
			get{return confirmDialog;}
		}

		public GameObject DialogBackground{
			set{dialogBackground = value;}
			get{return dialogBackground;}
		}


		public void setDialogDepth(GameObject confirmDialog)
		{
			UISprite confirmSprite = confirmDialog.GetComponent<UISprite>();

			confirmSprite.depth = 13;
			Transform[] trans = confirmDialog.GetComponentsInChildren<Transform> ();
			foreach(Transform tran in trans)
			{
				Debug.Log(tran.name + " " + tran.GetComponent<Component>().GetType());
				if(tran.name == "Label")
				{
					tran.GetComponent<UILabel>().depth = 14;


				}
				if(tran.name == "BG - Stripes" || tran.name == "cancelBtn" || tran.name == "okBtn")
				{
					tran.GetComponent<UISprite>().depth = 14;
					if(tran.childCount > 0)
					{
						tran.GetChild(0).GetComponent<UILabel>().depth = 15;
					}

				}

			}
		}

	}
}

