

public abstract class XGSDKCallback
{
	private static XGSDKCallback instance;
	
	public static XGSDKCallback getInstance()
	{
		if(instance == null)
		{
			instance = new XGSDKCallbackImpl();
		}
		return instance;
	}
	//初始化成功回调
	
	//public static void onInitSuccess(int code, string msg, string channelCode){}
	public abstract void onInitSuccess (int code, string msg, string channelCode);

	//初始化失败回调
	
	public abstract void onInitFail (int code, string msg,string channelCode);
	
	
	//登录成功回调
	public abstract void onLoginSuccess (int code, string authInfo);		
	//登录失败回调
	public abstract void onLoginFail (int code, string msg, string channelCode);
	//登录取消回调
	public abstract void onLoginCancel (int code, string msg);
	

	//上传分数成功回调
	public abstract void onSubmitScoreSuccess(int code,string msg);
	//上传分数失败回调
	public abstract void onSubmitScoreFail(int code,string msg);
	//上传成就成功回调
	public abstract void onSubmitAchievementSuccess(int code,string msg);
	//上传成就失败回调
	public abstract void onSubmitAchievementFail(int code,string msg);

	//登出完成回调
	public abstract void onLogoutFinish (int code, string msg);
	
	
	
	//支付成功回调
	public abstract void onPaySuccess(string payResult);
	//支付失败回调
	public abstract void onPayFail(string payResult);	
	//支付取消回调
	public abstract void onPayCancel(string payResult);
	//支付结果未知
	public abstract void onPayOthers(string payResult);
	//支付过程中回调
	
	public abstract void onPayProgress(string payResult);
	
	
	//直接退出回调
	
	public abstract void doExit(string msg);
	//使用游戏方退出回调
	
	public abstract void onNoChannelExiter(string msg);
	
	
}