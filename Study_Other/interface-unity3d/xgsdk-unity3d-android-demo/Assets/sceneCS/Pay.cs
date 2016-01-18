using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using ChannelUtils;
using System.Text;
using System;

public class Pay : MonoBehaviour {
	
	public static string uid;
	public static string channelId;
	public static string planId;
	public const int PAY_AMOUNT_ONE = 100;
	public const int PAY_AMOUNT_SIX = 600;

	public static string payInfo;

	public static GameObject dialogBackground;
	public static GameObject payDialog;
	public static UIInput commodityNumInput;
	public static UIInput commodityUnitPriceInput;
	public static UIInput productIdInput;

	void OnLevelWasLoaded(int level) {
		if (level == 3)
		{
			Debug.Log ("load loginSuccess scene success");
			if(!string.IsNullOrEmpty(payInfo))
			{				
				ChannelUtils.DialogHelper.getInstance().showCallbackTip(payInfo);
			}
		}
		
	}

	void Start () {
		PlayerPrefs.SetString ("currentPage", "pay");
	}
	
	// Update is called once per frame
	void Update () {
		if(Application.platform == RuntimePlatform.Android && Input.GetKeyDown(KeyCode.Escape))
		{
			if(payDialog != null && payDialog.activeSelf)
			{
				dialogBackground.SetActive (false);
				payDialog.SetActive (false);

			}else{
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
	
	}

	public void Pay_ONE()//default pay one yuan
	{
		showPayDialog ();
	}

	public void Pay_SIX()
	{
		pay (1,6,"17");
	}



	public void pay(int ProductQuantity,int ProductUnitPrice,string productId)
	{

		Debug.Log ("uid: " + uid + " channelId: " + channelId + " planId: " + planId);
		PayInfo payInfo = createPayOrder ();
		int amount = ProductQuantity * ProductUnitPrice * 100;
		payInfo.ProductQuantity = ProductQuantity;
		payInfo.ProductUnitPrice = ProductUnitPrice * 100;
		payInfo.TotalAmount = amount;//分
		payInfo.PayAmount = amount;
		payInfo.ProductId = productId;
		payInfo.Uid = uid;

		Dictionary<string, object> custom = new Dictionary<string,object>();
		custom.Add("channelId", channelId);
		custom.Add("planId", planId);
		payInfo.CustomInfo = XGSDKMiniJSON.Json.Serialize(custom);

		XGSDK2.instance.pay(payInfo);

		//data statistic onpayfinish
		//XGSDK2.instance.onPayFinish (payInfo);
	}

	public PayInfo createPayOrder()
	{
		PayInfo payinfo = new PayInfo ();
		payinfo.Uid = "";
		//payinfo.ProductId = "199";
		payinfo.ProductName = "gift";
		payinfo.ProductDesc = "Description";
		payinfo.ProductUnit = "元宝";
		payinfo.ProductUnitPrice = 100;
		payinfo.ProductQuantity = 1;
		//payinfo.TotalAmount = 100;
		//payinfo.PayAmount = 100;
		payinfo.CurrencyName = "CNY";
		payinfo.RoleId = "1000_unity";
		payinfo.RoleName = "xgdemo";
		payinfo.RoleLevel = "12";
		payinfo.RoleVipLevel = "3";
		payinfo.ServerId = "1";
		payinfo.ZoneId = "1025";
		payinfo.PartyName = "world";
		payinfo.VirtualCurrencyBalance = "200";
		payinfo.CustomInfo = "none";
		payinfo.GameTradeNo = "1001";
		payinfo.AdditionalParams = "{'firstName' : 'Bernet'}";
		payinfo.GameCallBackURL = "http://doc.xgsdk.com:18090/internal/sdkserver/receivePayResult";
		return payinfo;
	}

	public void showPayDialog()
	{
		if(payDialog != null)
		{
			dialogBackground.SetActive(true);
			payDialog.SetActive(true);
			return;
		}
		GameObject uiRootObject = GameObject.Find ("UI Root");
		dialogBackground = NGUITools.AddChild(uiRootObject,(GameObject)(Resources.Load ("dialogBackground")));
		payDialog = NGUITools.AddChild (uiRootObject, (GameObject)(Resources.Load ("inputDialog 2")));
		UISprite bgSprite =  dialogBackground.GetComponent<UISprite>();
		//bgSprite.alpha = 0.2f;
		bgSprite.width = 1280;
		bgSprite.height = 720;	
		bgSprite.depth = 3;


		intPayDialogSprite (payDialog);
	}

	public void intPayDialogSprite(GameObject payDialog)
	{
		UISprite payDialogSprite = payDialog.GetComponent<UISprite>();
		payDialogSprite.depth = 4;

		Transform[] trans = payDialog.GetComponentsInChildren<Transform> ();
		foreach(Transform tran in trans)
		{
			if(string.Equals("title label",tran.name))
			{
				UILabel title = tran.GetComponent<UILabel>();
				title.depth = 5;
				tran.GetComponent<UILabel>().text = "支付";
			}

			if(string.Equals("input1",tran.name))
			{
				UILabel title = tran.GetComponent<UILabel>();
				title.depth = 5;
				tran.GetComponent<UILabel>().text = "商品个数:";
			}

			if(string.Equals("input2",tran.name))
			{
				UILabel title = tran.GetComponent<UILabel>();
				title.depth = 5;
				tran.GetComponent<UILabel>().text = "商品单价（元）:";
			}
			if(string.Equals("input3",tran.name))
			{
				UILabel title = tran.GetComponent<UILabel>();
				title.depth = 5;
				tran.GetComponent<UILabel>().text = "商品ID:";
			}

			if(string.Equals("inputField1",tran.name))
			{
				tran.GetComponent<UISprite>().depth = 5;
				commodityNumInput = tran.GetComponent<UIInput>();
				commodityNumInput.value= "1";
				tran.GetComponentInChildren<UILabel>().depth = 6;
			}

			if(string.Equals("inputField2",tran.name))
			{
				tran.GetComponent<UISprite>().depth = 5;
				commodityUnitPriceInput = tran.GetComponent<UIInput>();
				commodityUnitPriceInput.value= "1";
				tran.GetComponentInChildren<UILabel>().depth = 6;
			}
			if(string.Equals("inputField3",tran.name))
			{
				tran.GetComponent<UISprite>().depth = 5;
				productIdInput = tran.GetComponent<UIInput>();
				productIdInput.value= "111";
				tran.GetComponentInChildren<UILabel>().depth = 6;
			}

			if(string.Equals("cancelBtn",tran.name) || string.Equals("okBtn",tran.name) )
			{
				tran.GetComponent<UISprite>().depth = 5;
				tran.GetComponentInChildren<UILabel>().depth = 6;
				if(string.Equals("cancelBtn",tran.name))
				{
					Component cancelCom = tran.GetComponent<UIButton>();
					UIEventListener.Get(cancelCom.gameObject).onClick = onCancelHandler;

				}
				if(string.Equals("okBtn",tran.name))
				{
					Component okCom = tran.GetComponent<UIButton>();
					UIEventListener.Get(okCom.gameObject).onClick = onOkHandler;
					
				}

			}
		}
	}

	private void onCancelHandler(GameObject obj)
	{
		dialogBackground.SetActive (false);
		payDialog.SetActive (false);
	}

	private void onOkHandler(GameObject obj)
	{
		dialogBackground.SetActive (false);
		payDialog.SetActive (false);
		string ProductQuantity = commodityNumInput.value;
		string ProductUnitPrice = commodityUnitPriceInput.value;
		string productId = productIdInput.value;
		Debug.Log ("ProductQuantity " + ProductQuantity + " ProductUnitPrice" + ProductUnitPrice + " productId" + productId);
		pay (Convert.ToInt32 (ProductQuantity), Convert.ToInt32 (ProductUnitPrice),productId);
	}














}
