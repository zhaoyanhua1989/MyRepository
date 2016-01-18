#include <jni.h>
#include <stdlib.h>

#ifndef PROTOCOLXGSDK_H
#define PROTOCOLXGSDK_H

#define XGSDK_VERSION "XGSDK version \" 2.0.0 \" "

#ifdef __cplusplus
extern "C" {
#endif

//游戏角色信息
struct RoleInfo {
	RoleInfo():uid(""),roleId(""),roleName(""),roleType(""),
			roleLevel(""),roleVipLevel(""),serverId(""),zoneId(""),
			serverName(""),zoneName(""),partyName(""),gender(""){}
	const char *uid; //用户账号ID
	const char *roleId; //角色ID
	const char *roleName; //角色名
	const char *roleType;//角色类型
	const char *roleLevel; //角色等级
	const char *roleVipLevel; //角色VIP等级
	const char *serverId; //服ID
	const char *zoneId; //区ID
	const char *serverName;//服名称
	const char *zoneName;//区名称
	const char *partyName;//帮会或公会名称
	const char *gender;//性别
};

//支付信息
struct PayInfo {
	PayInfo():uid(""),productId(""),productName(""),productDesc(""),
			productUnit(""),productQuantity(0),productUnitPrice(0),
			totalAmount(0),payAmount(0),currencyName(""),roleId(""),
			roleName(""),roleLevel(""),roleVipLevel(""),serverId(""),
			zoneId(""),partyName(""),virtualCurrencyBalance(""),
			customInfo(""),gameTradeNo(""),gameCallbackUrl(""),
			additionalParams(""){}
	const char *uid;//用户账号ID
	const char *productId;//产品ID
	const char *productName;//产品名称
	const char *productDesc;//产品描述
	const char *productUnit;//产品单位，比如元宝等
	int productQuantity;//购买的数量
	int productUnitPrice;//产品单价(现实货币，最小货币单位。比如人民币最小货币单位为分)
	int totalAmount;//总价(现实货币，最小货币单位。比如人民币最小货币单位为分)
	int payAmount;//玩家应付总额(现实货币，最小货币单位。比如人民币最小货币单位为分)
	const char *currencyName;//国际标准货币代码(ISO 4217，比如人民币的货币代码是CNY)
	const char *roleId;//角色ID
	const char *roleName;//角色名
	const char *roleLevel;//角色等级
	const char *roleVipLevel;//角色VIP等级
	const char *serverId;//服ID
	const char *zoneId;//区ID
	const char *partyName;//帮会名称
	const char *virtualCurrencyBalance;//虚拟货币余额
	const char *customInfo;//透传字段，充值成功后，XG服务器会将此字段传给游戏服务器
	const char *gameTradeNo;//游戏侧订单号，用于订单核对
	const char *gameCallbackUrl;//充值到账通知的地址，如果为空，则使用XGSDK后台配置的充值回调地址
	const char *additionalParams;//扩展字段
};


//回调函数接口，XGSDK会在对应操作完成后，通过此回调接口通知游戏
class XGSDKCallback {
public:
	//渠道初始化成功，建议游戏在渠道初始化成功后，再调用XGSDK的接口
	virtual void onInitSuccess(int code, const char* msg, const char* channelCode)=0;
	
	//渠道初始化失败，建议重启游戏
	virtual void onInitFail(int code, const char * msg, const char* channelCode)=0;

	//登录成功，游戏需要根据authInfo到西瓜服务器进行登录验证
	virtual void onLoginSuccess(int code, const char * authInfo)=0;

	//登录失败， 建议游戏返回账号登录界面
	virtual void onLoginFail(int code, const char * msg, const char* channelCode)=0;
	
	//登录取消，建议游戏返回账号登录界面
	virtual void onLoginCancel(int code, const char * msg)=0;

	//登出完成，建议游戏返回账号登录界面
	virtual void onLogoutFinish(int code, const char * msg)=0;

	//支付成功，游戏在此回调中实现支付成功后的逻辑
	virtual void onPaySuccess(int code, const char* msg, const char* gameTradeNo, const char* xgTradeNo)=0;

	//支付失败，游戏在此回调中实现支付失败后的逻辑
	virtual void onPayFail(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg)=0;

	//支付取消，游戏在此回调中实现支付取消后的逻辑
	virtual void onPayCancel(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg)=0;

	//支付进行，游戏在此回调中实现支付进行的逻辑
	virtual void onPayProgress(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg)=0;

	//支付其他回调，游戏在此回调中根据code实现相应的逻辑
	virtual void onPayOthers(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg)=0;

	//退出游戏，游戏在此回调中实现退出游戏逻辑(已经弹出渠道的退出界面，且用户选择退出后，调用此方法，建议游戏直接退出)
	virtual void doExit()=0;

	//没有渠道的退出界面，游戏可以在此回调中拉起自己的退出确认界面（如果没有游戏退出确认界面，则直接退出)
	virtual void onNoChannelExiter()=0;

	virtual ~XGSDKCallback() {};
};

class ProtocolXGSDK {
public:
	ProtocolXGSDK()
	{
		mXGEngine = NULL; //XGSDKCocos2dxWrapper实例
		prepare(); //获取XGSDKCocos2dxWrapper实例
	}
	virtual ~ProtocolXGSDK() {
	}
public:	
	/****************基础接口*****************/
	//登录
	void login(const char *customParams = "");
	//支付
	void pay(PayInfo &);
	//游戏退出
	void exit(const char *customParams = "");
	//登出
	void logout(const char *customParams = "");
	//切换账号
	void switchAccount(const char *customParams = "");

	/****************渠道扩展接口*****************/
	//进入游戏，玩家完成身份验证进入游戏时调用
	void onEnterGame(RoleInfo &);
	//创建角色，玩家新创建角色的时候调用
	void onCreateRole(RoleInfo &);
	//角色升级，角色升级时调用
	void onRoleLevelUp(RoleInfo &);

	/***************数据统计接口********************/
	//自定义事件
	void onEvent(RoleInfo& roleInfo, const char* eventId, const char* eventDesc,int eventValue, const char* eventBody="");

	//任务开始接口
	void onMissionBegin(RoleInfo& roleInfo, const char* missionId, const char* missionName, const char *customParams="");

	//任务成功接口
	void onMissionSuccess(RoleInfo& roleInfo, const char* missionId, const char* missionName, const char *customParams="");

	//任务失败接口
	void onMissionFail(RoleInfo& roleInfo, const char* missionId, const char* missionName, const char *customParams="");

	//充值虚拟货币接口
	void onVirtualCurrencyPurchase(RoleInfo& roleInfo, int amount, const char* customParams="");

	//赠送虚拟货币接口
	void onVirtualCurrencyReward(RoleInfo& roleInfo, const char* reason, int amount, const char* customParams="");

	//消费虚拟货币接口
	void onVirtualCurrencyConsume(RoleInfo& roleInfo, const char* itemName, int amount, const char* customParams="");

    /**********************辅助接口****************/
	//打开用户中心接口
	void openUserCenter(const char *customParams = "");

	//判断是否支持该方法
	bool isMethodSupport(const char * methodName);

	//获取渠道ID
	char *getChannelId();

	//设置回调
	void setListener(XGSDKCallback *);

	//释放资源
	void releaseResource();

private:
	void prepare();
	jobject mXGEngine;
};

#ifdef __cplusplus
}
#endif

#endif

