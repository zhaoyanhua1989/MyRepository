#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"

#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

#include "../proj.android/jni/hellocpp/ProtocolXGSDK.h"

#endif

class XGSDKTestCallback : public XGSDKCallback{
public:
	void onInitSuccess(int code, const char* msg, const char* channelCode);
	void onInitFail(int code, const char * msg, const char* channelCode); 
	void onLoginSuccess(int code, const char * authInfo);
	void onLoginFail(int code, const char * msg, const char* channelCode);
	void onLoginCancel(int code, const char * msg);
	void onLogoutFinish(int code, const char * msg);

	void onPaySuccess(int code, const char* msg, const char* gameTradeNo, const char* xgTradeNo);
	void onPayFail(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg);
	void onPayCancel(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg);
	void onPayProgress(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg);
	void onPayOthers(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg);

	void doExit();
	void onNoChannelExiter();

	~XGSDKTestCallback() {}
};

class HelloWorld : public cocos2d::CCLayer
{
public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // there's no 'id' in cpp, so we recommend returning the class instance pointer
    static cocos2d::CCScene* scene();
    
    // a selector callback
    void menuCloseCallback(CCObject* pSender);
    
    ProtocolXGSDK *mXgSdk;
    XGSDKTestCallback *mTestListener;
    char *channelId;

    // implement the "static node()" method manually
    CREATE_FUNC(HelloWorld);

    void cleanup();
private:
	void login(CCObject* pSender);
	void logout(CCObject* pSender);
	void pay(CCObject* pSender);
	void openUserCenter(CCObject* pSender);
    void switchAccount(CCObject* pSender);
    void exitGame(CCObject* pSender);
    void mission(CCObject* pSender);
    void currency(CCObject* pSender);
    void onevent(CCObject* pSender);
    void enterGame(CCObject* pSender);
    void createRole(CCObject* pSender);
    void roleLevelup(CCObject* pSender);
	void createMenu(char const* name, int x, int y, cocos2d::SEL_MenuHandler handler);    	
};

#endif // __HELLOWORLD_SCENE_H__
