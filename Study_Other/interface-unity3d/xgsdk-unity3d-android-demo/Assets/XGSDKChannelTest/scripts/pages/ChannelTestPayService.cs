using UnityEngine;
using System.Collections;

public class ChannelTestPayService: MonoBehaviour {

	void Start()
	{
		initPayPage ();
	}

	public static PayInfo payInfo;

	private static GameObject payParamsCheckPage;
	private UIGrid paramsGrid;
	private UIButton closeButton;	 
	private UIButton paySuccessButton;	 
	private UIButton payFailedButton;	 
	private UIButton payCancelButton;	
	private UILabel title;

	public static void initChannelTestPayService(GameObject root)
	{
		Debug.Log ("initChannelTestPayService start");
		if (null != root) {
			GameObject obj = GameObject.Find ("channelTestPayParmsCheck");
			if(obj == null){
				payParamsCheckPage = NGUITools.AddChild(root,(GameObject)(Resources.Load ("channelTestPayParmsCheck")));
				payParamsCheckPage.AddComponent<ChannelTestPayService>();		
				UnityEngine.Object.DontDestroyOnLoad(payParamsCheckPage);
			}
		}
		
		Debug.Log("initChannelTestPayService end");
	}

	public static bool activePayPage (bool isActive){
		if (payParamsCheckPage == null) {
			return false;
		}
		
		if (isActive) {
			if(!payParamsCheckPage.activeSelf){
				payParamsCheckPage.SetActive (true);
			}
		} else {
			payParamsCheckPage.SetActive (false);
		}
		return true;
	}

	public static bool activePayPage (GameObject root){
		if (payParamsCheckPage == null) {
			payParamsCheckPage = NGUITools.AddChild(root,(GameObject)(Resources.Load ("channelTestPayParmsCheck")));
			payParamsCheckPage.AddComponent<ChannelTestPayService>();		
			UnityEngine.Object.DontDestroyOnLoad(payParamsCheckPage);
		}

		if(!payParamsCheckPage.activeSelf){
			payParamsCheckPage.SetActive (true);
		}

		return true;
	}
	
	private void initPayPage(){
		//payParamsCheckPage = NGUITools.AddChild(,(GameObject)(Resources.Load ("channelTestPayParmsCheck")));
		this.initPayComponets ();
		if (payParamsCheckPage.activeSelf) {
			payParamsCheckPage.SetActive (false);
		}
	}
	
	private void initPayComponets()
	{
		paramsGrid = GameObject.Find("orderList_Grid").GetComponent<UIGrid>();
		closeButton = GameObject.Find("CloseButton").GetComponent<UIButton>();
		payCancelButton = GameObject.Find("PayCancelledButton").GetComponent<UIButton>();
		payFailedButton = GameObject.Find("PayFailedButton").GetComponent<UIButton>();
		paySuccessButton = GameObject.Find("PaySuccessButton").GetComponent<UIButton>();
		title = GameObject.Find("Label_result").GetComponent<UILabel>();

		this.bindEventForPay ();
	}
	
	private void bindEventForPay()
	{
		EventDelegate onCloseBtnClick = new EventDelegate(this, "onCloseBtnClick");
		closeButton.onClick.Add (onCloseBtnClick);

		EventDelegate onPaySuccessBtnClick = new EventDelegate(this, "onPaySuccessBtnClick");
		paySuccessButton.onClick.Add (onPaySuccessBtnClick);

		EventDelegate onPayFailedBtnClick = new EventDelegate(this, "onPayFailedBtnClick");
		payFailedButton.onClick.Add (onPayFailedBtnClick);

		EventDelegate onPayCancelBtnClick = new EventDelegate(this, "onPayCancelBtnClick");
		payCancelButton.onClick.Add (onPayCancelBtnClick);
		//eventDelegate.parameters[0] = new EventDelegate.Parameter(this, "param");
	}

	public void onCloseBtnClick()
	{
		if (payParamsCheckPage.activeSelf) {
			payParamsCheckPage.SetActive (false);
		}
	}

	public void onPaySuccessBtnClick(){
		title.text = "paySuccess 参数校验结果";
		ChannelTestPayDataBuilder.ChargeSuccess ();
	}

	public void onPayFailedBtnClick(){
		title.text = "payFailed 参数校验结果";
		ChannelTestPayDataBuilder.ChargeFailed ();
	}

	public void onPayCancelBtnClick(){
		title.text = "payCancel 参数校验结果";
		ChannelTestPayDataBuilder.ChargeCancel ();
	}

}
