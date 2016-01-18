#include "XGCocosDemo.h"
#include "AppMacros.h"
#include "cocos-ext.h"
#include "CocoStudio/Json/rapidjson/document.h"
#include "support/base64.h"
#include "CocoStudio/Json/rapidjson/stringbuffer.h" 
#include "CocoStudio/Json/rapidjson/writer.h" 


#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

#include "../proj.android/jni/hellocpp/ProtocolXGSDK.h"

#endif

USING_NS_CC;
USING_NS_CC_EXT;


std::string xg_strCurrentUid = "";
std::string xg_strPlanId = "";
std::string xg_strChannelId = "";

void XGSDKTestCallback::onInitFail(int code, const char * msg, const char* channelCode){
    char tmp[300];
    sprintf(tmp, "初始化失败, code is %d, msg is %s, channelCode is %s", code, msg, channelCode);
    CCMessageBox(tmp,"info");
}

void XGSDKTestCallback::onInitSuccess(int code, const char* msg, const char* channelCode){
    char tmp[300];
    sprintf(tmp, "初始化成功, code is %d, msg is %s, channelCode is %s", code, msg, channelCode);
    CCMessageBox(tmp, "info");
}

void XGSDKTestCallback::onLoginSuccess(int code, const char * authInfo){
    unsigned char *authInfoJson;
    unsigned char *coded = reinterpret_cast<unsigned char*>(const_cast<char *>(authInfo));
    base64Decode(coded, strlen(authInfo), &authInfoJson);
    char *authInfoJsonStr = reinterpret_cast<char*>(authInfoJson);
    std::string tmp(authInfoJsonStr);
    int pos = tmp.find_last_of('}');
    std::string getTmp = tmp.substr(0,pos+1);
    rapidjson::Document _doc;
    _doc.Parse<0>(getTmp.c_str());
    if(!_doc.HasMember("uid") || !_doc.HasMember("planId") || !_doc.HasMember("channelId")){
        CCMessageBox("no member exist","error");
    }else{
        xg_strCurrentUid = _doc["uid"].GetString();
        xg_strPlanId = _doc["planId"].GetString();
        xg_strChannelId = _doc["channelId"].GetString();
    }
    char tmpMsg[300];
    sprintf(tmpMsg, "登陆成功, %s", getTmp.c_str());
    CCMessageBox(tmpMsg, "info");
}
void XGSDKTestCallback::onLoginFail(int code, const char * msg, const char* channelCode){
    char tmp[200];
    sprintf(tmp, "登录失败, code is %d, msg is %s", code, msg);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::onLoginCancel(int code, const char * msg){
    char tmp[200];
    sprintf(tmp, "登录取消, code is %d, msg is %s", code, msg);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::onLogoutFinish(int code, const char * msg){
    char tmp[200];
    sprintf(tmp, "code is %d, msg is %s", code, msg);
    CCMessageBox(tmp, "info");
}

void XGSDKTestCallback::onPaySuccess(int code, const char* msg, const char* gameTradeNo, const char* xgTradeNo){
    char tmp[300];
    sprintf(tmp, "支付成功, code is %d, msg is %s, gameTradeNo is %s, xgTradeNo is %s", code, msg, gameTradeNo, xgTradeNo);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::onPayFail(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg){
    char tmp[300];
    sprintf(tmp, "支付失败, code is %d, msg is %s, gameTradeNo is %s, channelCode is %s, channelMsg is %s", code, msg, gameTradeNo, channelCode, channelMsg);
    CCMessageBox(tmp, "error");
}
void XGSDKTestCallback::onPayCancel(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg){
    char tmp[300];
    sprintf(tmp, "支付取消, code is %d, msg is %s, gameTradeNo is %s, channelCode is %s, channelMsg is %s", code, msg, gameTradeNo, channelCode, channelMsg);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::onPayProgress(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg){
    char tmp[300];
    sprintf(tmp, "支付进行, code is %d, msg is %s, gameTradeNo is %s, channelCode is %s, channelMsg is %s", code, msg, gameTradeNo, channelCode, channelMsg);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::onPayOthers(int code, const char* msg, const char* gameTradeNo, const char* channelCode, const char* channelMsg){
    char tmp[300];
    sprintf(tmp, "支付其他, code is %d, msg is %s, gameTradeNo is %s, channelCode is %s, channelMsg is %s", code, msg, gameTradeNo, channelCode, channelMsg);
    CCMessageBox(tmp, "info");
}
void XGSDKTestCallback::doExit(){
    CCMessageBox("使用渠道退出退出游戏", "info");
}
void XGSDKTestCallback::onNoChannelExiter(){
    CCMessageBox("渠道没退出方法，使用游戏方的退出", "info");
}

CCScene* HelloWorld::scene()
{
    // 'scene' is an autorelease object
    CCScene *scene = CCScene::create();
    
    // 'layer' is an autorelease object
    HelloWorld *layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !CCLayer::init() )
    {
        return false;
    }
    
    CCSize visibleSize = CCDirector::sharedDirector()->getVisibleSize();
    CCPoint origin = CCDirector::sharedDirector()->getVisibleOrigin();

    mXgSdk = new ProtocolXGSDK();
    mTestListener = new XGSDKTestCallback();
    mXgSdk->setListener(mTestListener);
    static char channelLabel[64];
    sprintf(channelLabel,"XGSDK测试Demo(%s)",mXgSdk->getChannelId());
    CCLabelTTF* pLabel = CCLabelTTF::create(channelLabel, "Arial", TITLE_FONT_SIZE);
    pLabel->setPosition(ccp(origin.x + visibleSize.width/2,origin.y+visibleSize.height - 10));
    this->addChild(pLabel, 1);

    int x = origin.x+80;
    int y = origin.y+visibleSize.height-50;     
	createMenu("登陆",x,y,menu_selector(HelloWorld::login));
	createMenu("支付",x,y-80,menu_selector(HelloWorld::pay));
    createMenu("切换账号",x,y-160,menu_selector(HelloWorld::switchAccount));

    createMenu("登出",x+100,y,menu_selector(HelloWorld::logout));
	createMenu("用户中心",x+100,y-80,menu_selector(HelloWorld::openUserCenter));
	createMenu("退游戏",x+100,y-160,menu_selector(HelloWorld::exitGame));

    createMenu("关卡事件",x+200,y,menu_selector(HelloWorld::mission));
    createMenu("货币事件",x+200,y-80,menu_selector(HelloWorld::currency));
    createMenu("自定义事件",x+200,y-160,menu_selector(HelloWorld::onevent));

    createMenu("进入游戏",x+300,y,menu_selector(HelloWorld::enterGame));
    createMenu("创建角色",x+300,y-80,menu_selector(HelloWorld::createRole));
    createMenu("角色升级",x+300,y-160,menu_selector(HelloWorld::roleLevelup));
    return true;
}

void HelloWorld::login(CCObject* pSender)
{
    CCLOG("xgsdk call login...");
    mXgSdk->login();
}

void HelloWorld::logout(CCObject* pSender)
{
    CCLOG("xgsdk call logout...");
	mXgSdk->logout();
}

void HelloWorld::pay(CCObject* pSender)
{
    CCLOG("xgsdk call pay...");

    rapidjson::Document _doc;
    _doc.SetObject();
    rapidjson::Document::AllocatorType &allocator = _doc.GetAllocator();
    rapidjson::Value jsonPlanId(rapidjson::kStringType);
    rapidjson::Value jsonChannelId(rapidjson::kStringType);
    jsonPlanId.SetString(xg_strPlanId.c_str());
    jsonChannelId.SetString(xg_strChannelId.c_str());
    _doc.AddMember("planId", jsonPlanId, allocator);
    _doc.AddMember("channelId", jsonChannelId, allocator);
    
    rapidjson::StringBuffer buffer;
    rapidjson::Writer<rapidjson::StringBuffer> writer(buffer);
    _doc.Accept(writer);
    std::string strTmp = buffer.GetString();

    PayInfo payInfo;
    payInfo.uid = xg_strCurrentUid.c_str();
    payInfo.productId = "payment017";
    payInfo.productName = "大宝剑";
    payInfo.productDesc = "倚天不出谁与争锋";
    payInfo.productUnit = "个";
    payInfo.productUnitPrice = 1;
    payInfo.productQuantity = 1;
    payInfo.totalAmount = 100;
    payInfo.payAmount = 100;
    payInfo.customInfo = strTmp.c_str();
    payInfo.currencyName = "CNY";
    payInfo.roleId = "1234";
    payInfo.roleName = "yeye";
    payInfo.roleLevel = "65";
    payInfo.roleVipLevel = "8";
    payInfo.serverId = "11";
    payInfo.zoneId = "33";
    payInfo.partyName = "丐帮";
    payInfo.virtualCurrencyBalance = "0";
    payInfo.gameTradeNo = "12480";
    payInfo.gameCallbackUrl = "http://doc.xgsdk.com:18090/internal/sdkserver/receivePayResult";
    payInfo.additionalParams = "";
	mXgSdk->pay(payInfo);
}
void HelloWorld::switchAccount(CCObject* pSender)
{
    CCLOG("xgsdk demo call switch account...");
	mXgSdk->switchAccount();
}

void HelloWorld::openUserCenter(CCObject* pSender)
{
    CCLOG("call user center...");
	mXgSdk->openUserCenter();

}

void HelloWorld::exitGame(CCObject* pSender)
{
    CCLOG("xgsdk demo call exist game...");
	mXgSdk->exit();
}

void HelloWorld::currency(CCObject* pSender)
{
    CCLOG("xgsdk demo call currency game...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";    
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    roleInfo.roleName = "onVirtualCurrencyPurchase";
    mXgSdk->onVirtualCurrencyPurchase(roleInfo,100);

    roleInfo.roleName = "onVirtualCurrencyReward";
    mXgSdk->onVirtualCurrencyReward(roleInfo,"登录奖励",100);

    roleInfo.roleName = "onVirtualCurrencyConsume";
    mXgSdk->onVirtualCurrencyConsume(roleInfo,"十连抽",200);
}

void HelloWorld::mission(CCObject* pSender)
{
    CCLOG("xgsdk demo call mission game...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    roleInfo.roleName = "onMissionBegin";
    mXgSdk->onMissionBegin(roleInfo,"10001","第二章节－第四关");

    roleInfo.roleName = "onMissionSuccess";
    mXgSdk->onMissionSuccess(roleInfo,"10001","第二章节－第四关");

    roleInfo.roleName = "onMissionFail";
    mXgSdk->onMissionFail(roleInfo,"10001","第二章节－第四关");    
}

void HelloWorld::onevent(CCObject* pSender)
{
    CCLOG("xgsdk demo call onEvent game...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";
    roleInfo.roleType = "弓箭手";
    roleInfo.roleName = "onEvent";
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    mXgSdk->onEvent(roleInfo,"test.event","test/event/desc",1,"{\"prop1\":\"value1\"}");
}

void HelloWorld::enterGame(CCObject* pSender)
{
    CCLOG("xgsdk demo call enterGame...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";
    roleInfo.roleType = "弓箭手";
    roleInfo.roleName = "onEnterGame";
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    mXgSdk->onEnterGame(roleInfo);
}

void HelloWorld::createRole(CCObject* pSender)
{
    CCLOG("xgsdk demo call createRole...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";
    roleInfo.roleType = "弓箭手";
    roleInfo.roleName = "onCreateRole";
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    mXgSdk->onCreateRole(roleInfo);
}

void HelloWorld::roleLevelup(CCObject* pSender)
{
    CCLOG("xgsdk demo call onEvent game...");
    RoleInfo roleInfo;
    roleInfo.uid = xg_strCurrentUid.c_str();
    roleInfo.roleId = "1000";
    roleInfo.roleType = "弓箭手";
    roleInfo.roleName = "onRoleLevelUp";
    roleInfo.roleLevel = "1";
    roleInfo.roleVipLevel = "13";
    roleInfo.serverId = "0";
    roleInfo.zoneId = "1";
    mXgSdk->onRoleLevelUp(roleInfo);
}

void HelloWorld::createMenu(char const* name, int x, int y, SEL_MenuHandler handler)
{
    CCLabelTTF *label = CCLabelTTF::create(name, "Arial", 16); // create a exit botton
    CCMenuItemLabel *pMenuItem = CCMenuItemLabel::create(label, this,handler);
    pMenuItem->setPosition(ccp(x,y));
    CCMenu* pMenu = CCMenu::create(pMenuItem,NULL);
    pMenu->setPosition(CCPointZero);
    this->addChild(pMenu, 1);
}

void HelloWorld::cleanup(){
    delete mXgSdk;
    delete mTestListener;
    CCLayer::cleanup();
}

