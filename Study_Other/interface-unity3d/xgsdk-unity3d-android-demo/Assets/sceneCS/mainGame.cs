using UnityEngine;
using System.Collections;
using System.IO;
using ChannelUtils;

public class mainGame : MonoBehaviour {


	public GameObject m_RoleNameDialog;
	public UIInput m_InputField;
	public UILabel m_roleNameLabel;
	public UILabel m_roleLevelLabel;
	public UILabel m_amountLabel;
	public UISprite m_dialogBg;

	public string roleName;
	public string roleLevel;
	public string balance;
	public static RoleInfo roleInfo = new RoleInfo();

	public static bool isInitRoleInfo = false;


	// Use this for initialization
	void Start () {
		Debug.Log ("maingame start");
		roleName = PlayerPrefs.GetString("roleName");
		if(!string.IsNullOrEmpty(roleName))
		{
			m_RoleNameDialog.SetActive (false);
			m_dialogBg.gameObject.SetActive(false);
			roleLevel = PlayerPrefs.GetString("roleLevel");
			balance = PlayerPrefs.GetString("balance");

			fillRoleInfoLabel();
			enterGameAndCreateRole();
		}
		PlayerPrefs.SetString ("currentPage", "mainGame");
	}
	
	// Update is called once per frame
	void Update () {
		if(Application.platform == RuntimePlatform.Android && Input.GetKeyDown(KeyCode.Escape))
		{
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

	public void OnRoleCancelClick()
	{
		//do nothing
	}

	public void OnRoleOKClick()
	{
		string rName = m_InputField.value.Trim();
		Debug.Log ("input value " + rName);
		if(!string.IsNullOrEmpty(rName))
		{
			m_RoleNameDialog.SetActive (false);
			m_dialogBg.gameObject.SetActive(false);

			roleName = rName;
			roleLevel = "1";
			balance = "1";

			PlayerPrefs.SetString("roleName", rName);
			PlayerPrefs.SetString("roleLevel", "1");
			PlayerPrefs.SetString("balance", "1");

			fillRoleInfoLabel();
			enterGameAndCreateRole();
		}


	}
	public void logoutGame()
	{
		isInitRoleInfo = false;
		XGSDK2.instance.logout ("game logout");
	}

	public void showUserInfo(){
		#if UNITY_STANDALONE_WIN
		XGSDK2.instance.openUserCenter("open user information center by main game button pressed");
		/*#elif UNITY_ANDROID
		PayInfo payInfo = createPayOrder();
		payInfo.TotalAmount = 123;//分
		payInfo.PayAmount = 123123;
		payInfo.Uid = "123";
		XGSDK2.instance.onPayFinish(payInfo);*/
		#endif

		#if UNITY_ANDROID
		Application.LoadLevel("ShareYourself");

		//tako Feedback
		//string appid = "567b474a1d12541a1db74e40";
		//showTakoFeedback(appid);

		#endif


	}


	//tako feedback
	public void showTakoFeedback(string appId)
	{
		callSdkApi ("com.xgsdk.client.api.tako.TakoFeedback", "showTakoFeedback", appId);
	}

	public void callSdkApi(string sdkJavaClass,string apiName,params object[] args)
	{
		Debug.Log("call xgsdk callRetSdkApi...");
		string retstring = "";
		Debug.Log("callback is ----------- " + apiName);
		#if UNITY_ANDROID 
		using (AndroidJavaClass cls = new AndroidJavaClass(sdkJavaClass)) {
			cls.CallStatic(apiName,args);
		}
		Debug.Log ("callRetSdkApi is ----------- " + apiName);
		#endif   

	}
	//tako feedback

	public void fillRoleInfoLabel()
	{
		m_roleNameLabel.text = roleName;
		m_roleLevelLabel.text = roleLevel;
		m_amountLabel.text = balance;
	}
	

	public void enterGameAndCreateRole()
	{
		initRoleInfo ();
		roleInfo.RoleName = roleName;
		roleInfo.RoleLevel = roleLevel;
		roleInfo.Balance = balance;
		XGSDK2.instance.onCreateRole(roleInfo);
		XGSDK2.instance.onEnterGame (roleInfo);

		// 测试用代码，实际游戏需要按需调用对应事件,数据统计
		XGSDK2.instance.onVirtualCurrencyConsume(roleInfo,"TestItemName",12345,"");
		XGSDK2.instance.onVirtualCurrencyPurchase(roleInfo,22345,"");
		XGSDK2.instance.onVirtualCurrencyReward(roleInfo,"TestReason",32345,"");
		
		//XGSDK2.instance.onMissionBegin(roleInfo,"TestMissionId", "TestMissionName", "");
		//XGSDK2.instance.onMissionSuccess(roleInfo,"TestMissionId", "TestMissionName", "");
		//XGSDK2.instance.onMissionFail(roleInfo,"TestMissionId", "TestMissionName", "");
		XGSDK2.instance.onEvent(roleInfo,"eventId1","eventDesc2",124,"");
		//XGSDK2.instance.onRoleLevelup(roleInfo);
	}
	
	public PayInfo createPayOrder()
	{
		PayInfo payinfo = new PayInfo ();
		payinfo.Uid = "";
		payinfo.ProductId = "199";
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

	public void initRoleInfo()
	{
		roleInfo.RoleId = "1000_unity";
		roleInfo.RoleType = "TanXian";
		roleInfo.RoleVipLevel="3";
		roleInfo.ServerId = "5";
		roleInfo.ZoneId = "701";		
		roleInfo.ServerName = "Gd";
		roleInfo.ZoneName = "battle";
		roleInfo.PartyName = "sky";
		roleInfo.Gender = "m";
	}



	/// <summary>
	/// 根据一个Rect类型来截取指定范围的屏幕
	/// 左下角为(0,0)
	/// </summary>
	/// <param name="mRect">M rect.</param>
	/// <param name="mFileName">M file name.</param>
	private IEnumerator CaptureByRect(Rect mRect,string mFileName)
	{
		//等待渲染线程结束
		yield return new WaitForEndOfFrame();
		//初始化Texture2D
		Texture2D mTexture=new Texture2D((int)mRect.width,(int)mRect.height,TextureFormat.RGB24,false);
		//读取屏幕像素信息并存储为纹理数据
		mTexture.ReadPixels(mRect,0,0);
		//应用
		mTexture.Apply();
		
		
		//将图片信息编码为字节信息
		byte[] bytes = mTexture.EncodeToPNG();  
		//保存
		System.IO.File.WriteAllBytes(mFileName, bytes);
		
		//如果需要可以返回截图
		//return mTexture;
	}

	private void CaptureScreenshot2(Rect rect,string filePath)   
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