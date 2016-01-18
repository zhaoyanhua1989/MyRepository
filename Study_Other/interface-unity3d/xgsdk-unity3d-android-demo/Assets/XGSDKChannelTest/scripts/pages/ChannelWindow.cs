using UnityEngine;
using System.Collections;

public class ChannelWindow : MonoBehaviour {

	//private Rect windowPosition;//儲存不可被拖曳的window的位置
	private Rect windowPositionMove;//儲存可被拖曳的window的位置
	//private Rect windowUserInfoPosition;//儲存可被拖曳的window的位置
	private Rect buttonUserInfoPosition;//儲存button在window內的位置
	private Rect buttonChangeUserPosition;//儲存button在window內的位置
	private Rect buttonTestInterfacePosition;//儲存button在window內的位置

	
	void Start()
	{
		#if UNITY_STANDALONE_WIN
		GUI.UnfocusWindow();
		setWindowPosition();
		setButtonPosition();
		GUI.FocusWindow(1);
		#endif
	}
	
	private void setWindowPosition()//設定window的位置
	{
		float windowWidth = 70f;
		float windowHeight = 100f;
		float windowLeft = 0f;//window和Game左邊的距離，目前設定的值會讓window顯示在螢幕正中央
		float windowTop = Screen.height * 0.5f - windowHeight * 0.5f;//window和Game上面的距離，目前設定的值會讓window顯示在螢幕正中央
		windowPositionMove = new Rect(windowLeft, windowTop, windowWidth, windowHeight);//將可被拖曳的視窗設定在Game中央
		//windowUserInfoPosition = new Rect(windowWidth + 10f, windowTop, windowWidth, windowHeight);
		//windowPosition = new Rect(0, 0, windowWidth, windowHeight);//將不可被拖曳的window設定在Game左上角
	}
	
	private void setButtonPosition()//設定windows內的button位置
	{
		float buttonWidth = 64f;//按鈕的寬度
		float buttonHeight = 20f;//按鈕的高度
		
		buttonUserInfoPosition = new Rect(windowPositionMove.width * 0.5f - buttonWidth * 0.5f, windowPositionMove.height * 0.8f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		buttonChangeUserPosition = new Rect(windowPositionMove.width * 0.5f - buttonWidth * 0.5f, windowPositionMove.height * 0.5f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		buttonTestInterfacePosition = new Rect(windowPositionMove.width * 0.5f - buttonWidth * 0.5f, windowPositionMove.height * 0.2f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
	}
	
	private void OnGUI()
	{
		//顯示window，但是無法拖曳
		//GUI.Window(0, windowPosition, windowEvent, "不可拖曳");
		
		//顯示window，可以被拖曳
		windowPositionMove = GUI.Window(1, windowPositionMove, windowEvent, "");

	}
	
	private void windowEvent(int id)//處理視窗裡面要顯示的文字、按鈕、事件處理。必須要有一個為int的傳入參數
	{
		if(GUI.Button(buttonUserInfoPosition, "个人中心"))//在window上顯示按鈕
		{
			Debug.Log ("buttonUserInfoPosition pressed");
			XGSDK2.instance.openUserCenter("open user information center by dragable window");
		}
		if(GUI.Button(buttonChangeUserPosition, "切换账号"))//在window上顯示按鈕
		{
			Debug.Log ("buttonChangeUserPosition pressed");
			XGSDK2.instance.switchAccount("switch user from dragable window");
		}
		if(GUI.Button(buttonTestInterfacePosition, "接口检查"))//在window上顯示按鈕
		{
			Debug.Log ("buttonTestInterfacePosition pressed");
		}
	
		if(id == 1)//若是id為1，代表是可被拖曳的window
		{
			GUI.DragWindow();
		}
	}

}
