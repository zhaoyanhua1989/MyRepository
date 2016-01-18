#include <jni.h>
#include "ProtocolXGSDK.h"
#include "ProtocolXGSDK_android.h"
#include "platform/android/jni/JniHelper.h"
#include "cocos2d.h"
#include <android/log.h>

#define TAG "XGSDK" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型
using namespace cocos2d;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

static char* g_channelId = NULL;
XGSDKCallback *globalListener = NULL;
const char XG_PACKAGE[] = "com/xgsdk/client/api/cocos2dx/XGSDKCocos2dxWrapper";
const char XG_ENTITYPAY[] = "com/xgsdk/client/api/entity/PayInfo";
const char XG_ENTITYROLE[] = "com/xgsdk/client/api/entity/RoleInfo";

static jstring stoJstring( JNIEnv* env, const char* pat )
{
	//定义java String类 strClass
	jclass strClass = (env)->FindClass("java/lang/String");
	//获取java String类方法String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
	jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
	jbyteArray bytes = (env)->NewByteArray(strlen(pat));//建立byte数组
	(env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);//将char* 转换为byte数组
	jstring encoding = (env)->NewStringUTF("utf-8");// 设置String, 保存语言类型,用于byte数组转换至String时的参数
	return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);//将byte数组转换为java String,并输出
}

static char* jstringTostr(JNIEnv* jniEnv, jstring jstr) {
	if(jstr == NULL) {
		LOGI("jstr is null");
		char *tmp = (char *)malloc(sizeof(char));
		memset(tmp, 0, sizeof(char));
		return tmp;
	}
	char* pStr = NULL;
	jclass jstrObj = jniEnv->FindClass("java/lang/String");
	jstring encode = jniEnv->NewStringUTF("utf-8");
	jmethodID methodId = jniEnv->GetMethodID(jstrObj, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray byteArray = (jbyteArray) jniEnv->CallObjectMethod(jstr,
			methodId, encode);
	jsize strLen = jniEnv->GetArrayLength(byteArray);
	jbyte *jBuf = jniEnv->GetByteArrayElements(byteArray, JNI_FALSE);
	if (jBuf > 0) {
		pStr = (char*) malloc(strLen + 1);
		if (!pStr) {
			return NULL;
		}
		memcpy(pStr, jBuf, strLen);
		pStr[strLen] = 0;
	}
	jniEnv->ReleaseByteArrayElements(byteArray, jBuf, 0);
	return pStr;
}

static void callJsonPut(jobject& obj, JniMethodInfo& t, const char* key, const char* value)
{
	jstring strKey = stoJstring(t.env,key);
	jstring strValue = stoJstring(t.env,value);
	t.env->CallObjectMethod(obj,t.methodID,strKey,strValue);
	t.env->DeleteLocalRef(strKey);
	t.env->DeleteLocalRef(strValue);
}

static void callJsonPutInt(jobject& obj, JniMethodInfo& t, const char* key, int value)
{
	jstring strKey = stoJstring(t.env,key);
	jint intValue = value;
	t.env->CallObjectMethod(obj, t.methodID, strKey, intValue);
	t.env->DeleteLocalRef(strKey);
}

static jobject payInfoToJson(JNIEnv*& env, PayInfo& payInfo)
{
	jclass cls = env->FindClass("org/json/JSONObject");
	jmethodID mid = env->GetMethodID(cls,"<init>","()V");
	jobject obj = env->NewObject(cls,mid);
	JniMethodInfo t;
	JniMethodInfo tInt;
	if(JniHelper::getMethodInfo(tInt, "org/json/JSONObject", "put", "(Ljava/lang/String;I)Lorg/json/JSONObject;")) {
		callJsonPutInt(obj,tInt,"productUnitPrice",payInfo.productUnitPrice);
		callJsonPutInt(obj,tInt,"productQuantity",payInfo.productQuantity);
		callJsonPutInt(obj,tInt,"totalAmount",payInfo.totalAmount);
		callJsonPutInt(obj,tInt,"payAmount",payInfo.payAmount);
		tInt.env->DeleteLocalRef(tInt.classID);
	}
	if (JniHelper::getMethodInfo(t, "org/json/JSONObject", "put", "(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;"))
	{
		callJsonPut(obj,t,"uid",payInfo.uid);
		callJsonPut(obj,t,"productId",payInfo.productId);
		callJsonPut(obj,t,"productName",payInfo.productName);
		callJsonPut(obj,t,"productDesc",payInfo.productDesc);
		callJsonPut(obj,t,"productUnit",payInfo.productUnit);
		callJsonPut(obj,t,"currencyName",payInfo.currencyName);
		callJsonPut(obj,t,"roleId",payInfo.roleId);
		callJsonPut(obj,t,"roleName",payInfo.roleName);
		callJsonPut(obj,t,"roleLevel",payInfo.roleLevel);
		callJsonPut(obj,t,"roleVipLevel",payInfo.roleVipLevel);
		callJsonPut(obj,t,"serverId",payInfo.serverId);
		callJsonPut(obj,t,"zoneId",payInfo.zoneId);
		callJsonPut(obj,t,"partyName",payInfo.partyName);
		callJsonPut(obj,t,"virtualCurrencyBalance",payInfo.virtualCurrencyBalance);
		callJsonPut(obj,t,"customInfo",payInfo.customInfo);
		callJsonPut(obj,t,"gameTradeNo",payInfo.gameTradeNo);
		callJsonPut(obj,t,"gameCallbackUrl",payInfo.gameCallbackUrl);
		t.env->DeleteLocalRef(t.classID);
	}

	return obj;
}

static jobject roleInfoToJson(JNIEnv*& env, RoleInfo& roleInfo)
{
	jclass cls = env->FindClass("org/json/JSONObject");
	jmethodID mid = env->GetMethodID(cls,"<init>","()V");
	jobject obj = env->NewObject(cls,mid);
	JniMethodInfo t;
	if (JniHelper::getMethodInfo(t, "org/json/JSONObject", "put", "(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;"))
	{
		callJsonPut(obj,t,"uid",roleInfo.uid);
		callJsonPut(obj,t,"roleId",roleInfo.roleId);
		callJsonPut(obj,t,"roleLevel",roleInfo.roleLevel);
		callJsonPut(obj,t,"roleVipLevel",roleInfo.roleVipLevel);
		callJsonPut(obj,t,"roleType",roleInfo.roleType);
		callJsonPut(obj,t,"serverId",roleInfo.serverId);
		callJsonPut(obj,t,"zoneId",roleInfo.zoneId);
		callJsonPut(obj,t,"roleName",roleInfo.roleName);
		callJsonPut(obj,t,"serverName",roleInfo.serverName);
		callJsonPut(obj,t,"zoneName",roleInfo.zoneName);
		callJsonPut(obj,t,"partyName",roleInfo.partyName);
		callJsonPut(obj,t,"gender",roleInfo.gender);
		t.env->DeleteLocalRef(t.classID);
	}
	return obj;
}

void ProtocolXGSDK::setListener(XGSDKCallback *lis) {
	globalListener = lis;

	LOGI("setCallback begin");

	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "setCallback", "()V")) {
		LOGI("setCallback is progressing");
		if(mXGEngine == NULL) {
			LOGI("mXGEngine is null");
			return;
		}
		t.env->CallVoidMethod(mXGEngine, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("setCallback end");
}


void ProtocolXGSDK::prepare() {
	LOGI("prepare begin");
	if(mXGEngine != NULL) {
		LOGI("mXGEngine is already initialized");
		return;
	}

	LOGI("XGSDKCocos2dxWrapper getInstance method is going to be invoking");
	JniMethodInfo t;
	if(JniHelper::getStaticMethodInfo(t, XG_PACKAGE, "getInstance", "()Lcom/xgsdk/client/api/cocos2dx/XGSDKCocos2dxWrapper;")) {
		LOGI("XGSDKCocos2dxWrapper getInstance method is progressing");
		mXGEngine = t.env->NewGlobalRef(t.env->CallStaticObjectMethod(t.classID, t.methodID));
		t.env->DeleteLocalRef(t.classID);
	}

	CCMessageBox(XGSDK_VERSION,"About");
	LOGI("prepare end");
}

char *ProtocolXGSDK::getChannelId() {
	LOGI("getChannelId begin");
	if(g_channelId != NULL) {
		return g_channelId;
	}

	LOGI("XGSDKCocos2dxWrapper getChannelId method is going to be invoking");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "getChannelId", "()Ljava/lang/String;")) {
		LOGI("XGSDKCocos2dxWrapper getChannelId method is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject ret = t.env->CallObjectMethod(mXGEngine, t.methodID);
		if(ret == NULL) {
			char *tmp = (char *)malloc(sizeof(char));
			memset(tmp, 0, sizeof(char));
			return tmp;
		}
		LOGI("jchannelId is ready");
		jstring jchannelId = static_cast<jstring>(ret);
		g_channelId = jstringTostr(t.env, jchannelId);
		LOGI("jchannelId is cast to cstr");
		t.env->DeleteLocalRef(t.classID);

	}

	LOGI("getChannelId end");
	return g_channelId;
}

void ProtocolXGSDK::login(const char *customParams) {
	LOGI("login begin");
	JniMethodInfo t;

	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "login", "(Ljava/lang/String;)V")) {
		LOGI("login is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jstring jtmp = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID, jtmp);
		t.env->DeleteLocalRef(jtmp);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("login end");
}

void ProtocolXGSDK::pay(PayInfo &payInfo) {
	LOGI("pay begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "pay", "(Lcom/xgsdk/client/api/entity/PayInfo;)V")) {
		LOGI("pay is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject payInfoObj = payInfoToJson(t.env,payInfo);
		LOGI("parse payInfo into Json done");
		JniMethodInfo tInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYPAY, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/PayInfo;")) {
			jobject jpayInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, payInfoObj);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject payInfo done");

			t.env->CallVoidMethod(mXGEngine, t.methodID, jpayInfo);
			t.env->DeleteLocalRef(jpayInfo);
		}
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(payInfoObj);
	}

	LOGI("pay end");
}

void ProtocolXGSDK::exit(const char *customParams) {
	LOGI("exit begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "exit", "(Ljava/lang/String;)V")) {
		LOGI("exit is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jstring jtmp = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID, jtmp);
		t.env->DeleteLocalRef(jtmp);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("exit end");
}

void ProtocolXGSDK::logout(const char *customParams) {
	LOGI("logout begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "logout", "(Ljava/lang/String;)V")) {
		LOGI("logout is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jstring jtmp = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID, jtmp);

		t.env->DeleteLocalRef(jtmp);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("logout end");
}

void ProtocolXGSDK::switchAccount(const char *customParams) {
	LOGI("switchAccount begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "switchAccount", "(Ljava/lang/String;)V")) {
		LOGI("switchAccount is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jstring jtmp = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID, jtmp);
		t.env->DeleteLocalRef(jtmp);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("switchAccount end");
}
/*统计*/

void ProtocolXGSDK::onEnterGame(RoleInfo &roleInfo) {
	LOGI("onEnterGame begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onEnterGame", "(Lcom/xgsdk/client/api/entity/RoleInfo;)V")) {
		LOGI("onEnterGame is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject obj = roleInfoToJson(t.env,roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jobject jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, obj);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
			t.env->CallVoidMethod(mXGEngine, t.methodID, jroleInfo);
			t.env->DeleteLocalRef(jroleInfo);
		}
		t.env->DeleteLocalRef(obj);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("onEnterGame end");
}

void ProtocolXGSDK::onCreateRole(RoleInfo &roleInfo) {
	LOGI("onCreateRole begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onCreateRole", "(Lcom/xgsdk/client/api/entity/RoleInfo;)V")) {
		LOGI("onCreateRole is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject obj = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jobject jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, obj);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
			t.env->CallVoidMethod(mXGEngine, t.methodID, jroleInfo);
		}
		t.env->DeleteLocalRef(obj);
		t.env->DeleteLocalRef(t.classID);
	}

	LOGI("onCreateRole end");
}

void ProtocolXGSDK::onRoleLevelUp(RoleInfo &roleInfo) {
	LOGI("onRoleLevelUp begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onRoleLevelup", "(Lcom/xgsdk/client/api/entity/RoleInfo;)V")) {
		LOGI("onRoleLevelUp is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject obj = roleInfoToJson(t.env,roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jobject jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, obj);
			LOGI("parse json into jobject roleInfo done");
			t.env->CallVoidMethod(mXGEngine, t.methodID, jroleInfo);
			t.env->DeleteLocalRef(t.classID);
		}
		t.env->DeleteLocalRef(obj);
	}
	LOGI("onRoleLevelUp end");
}

void ProtocolXGSDK::onEvent(RoleInfo& roleInfo, const char* eventId, const char* eventDesc, int eventValue, const char *eventBody) {
	LOGI("onEvent begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onEvent", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V")) {
		LOGI("onEvent is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}
		jstring jeventId = stoJstring(t.env, eventId);
		jstring jeventDesc = stoJstring(t.env, eventDesc);
		jint jeventValue = eventValue;
		jstring jeventBody = stoJstring(t.env, eventBody);

		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jeventId,jeventDesc,jeventValue,jeventBody);

		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jeventId);
		t.env->DeleteLocalRef(jeventDesc);
		t.env->DeleteLocalRef(jeventBody);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onEvent end");
}

void ProtocolXGSDK::onMissionBegin(RoleInfo& roleInfo,const char* missionId, const char* missionName, const char *customParams) {
	LOGI("onMissionBegin begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onMissionBegin", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V")) {
		LOGI("onMissionBegin is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		jstring jmissionId = stoJstring(t.env, missionId);
		jstring jmissionName = stoJstring(t.env, missionName);
		jstring jcustomParams = stoJstring(t.env, customParams);

		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jmissionId,jmissionName,jcustomParams);

		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jmissionId);
		t.env->DeleteLocalRef(jmissionName);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onMissionBegin end");
}

void ProtocolXGSDK::onMissionSuccess(RoleInfo& roleInfo,const char* missionId, const char* missionName, const char *customParams) {
	LOGI("onMissionSuccess begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onMissionSuccess", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V")) {
		LOGI("onMissionSuccess is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		jstring jmissionId = stoJstring(t.env, missionId);
		jstring jmissionName = stoJstring(t.env, missionName);
		jstring jcustomParams = stoJstring(t.env, customParams);

		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jmissionId,jmissionName,jcustomParams);

		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jmissionId);
		t.env->DeleteLocalRef(jmissionName);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onMissionSuccess end");
}

void ProtocolXGSDK::onMissionFail(RoleInfo& roleInfo,const char* missionId, const char* missionName, const char *customParams) {
	LOGI("onMissionFail begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onMissionFail", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V")) {
		LOGI("onMissionFail is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		jstring jmissionId = stoJstring(t.env, missionId);
		jstring jmissionName = stoJstring(t.env, missionName);
		jstring jcustomParams = stoJstring(t.env, customParams);

		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jmissionId,jmissionName,jcustomParams);

		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jmissionId);
		t.env->DeleteLocalRef(jmissionName);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onMissionFail end");
}

void ProtocolXGSDK::onVirtualCurrencyPurchase(RoleInfo& roleInfo, int amount, const char* customParams) {
	LOGI("onVirtalCurrencyPurchase begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onVirtualCurrencyPurchase", "(Lcom/xgsdk/client/api/entity/RoleInfo;ILjava/lang/String;)V")) {
		LOGI("onVirtalCurrencyPurchase is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		jint jamount = amount;
		jstring jcustomParams = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jamount,jcustomParams);
		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onVirtalCurrencyPurchase end");
}

void ProtocolXGSDK::onVirtualCurrencyReward(RoleInfo& roleInfo,const char* reason, int amount, const char *customParams) {
	LOGI("onVirtualCurrencyReward begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onVirtualCurrencyReward", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;ILjava/lang/String;)V")) {
		LOGI("onVirtualCurrencyReward is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		jstring jreason = stoJstring(t.env, reason);
		jint jamount = amount;
		jstring jcustomParams = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jreason,jamount,jcustomParams);
		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jreason);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onVirtualCurrencyReward end");
}

void ProtocolXGSDK::onVirtualCurrencyConsume(RoleInfo& roleInfo, const char* itemName, int amount, const char* customParams) {
	LOGI("onVirtualCurrencyConsume begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "onVirtualCurrencyConsume", "(Lcom/xgsdk/client/api/entity/RoleInfo;Ljava/lang/String;ILjava/lang/String;)V")) {
		LOGI("ON onVirtualCurrencyConsume is progressing");

		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jobject jroleInfoJson = roleInfoToJson(t.env, roleInfo);
		LOGI("parse roleInfo into Json done");
		JniMethodInfo tInfo;
		jobject jroleInfo;
		if(JniHelper::getStaticMethodInfo(tInfo, XG_ENTITYROLE, "parseJson", "(Lorg/json/JSONObject;)Lcom/xgsdk/client/api/entity/RoleInfo;")) {
			jroleInfo = tInfo.env->CallStaticObjectMethod(tInfo.classID, tInfo.methodID, jroleInfoJson);
			tInfo.env->DeleteLocalRef(tInfo.classID);
			LOGI("parse json into jobject roleInfo done");
		}

		jstring jitemName = stoJstring(t.env, itemName);
		jint jamount = amount;
		jstring jcustomParams = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID,jroleInfo,jitemName,jamount,jcustomParams);
		t.env->DeleteLocalRef(jroleInfo);
		t.env->DeleteLocalRef(jroleInfoJson);
		t.env->DeleteLocalRef(jitemName);
		t.env->DeleteLocalRef(jcustomParams);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("onVirtualCurrencyConsume end");
}

void ProtocolXGSDK::openUserCenter(const char *customParams) {
	LOGI("openUserCenter begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "openUserCenter", "(Ljava/lang/String;)V")) {
		LOGI("openUserCenter is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}

		jstring jtmp = stoJstring(t.env, customParams);
		t.env->CallVoidMethod(mXGEngine, t.methodID, jtmp);
		t.env->DeleteLocalRef(jtmp);
		t.env->DeleteLocalRef(t.classID);
	}
	LOGI("openUserCenter end");
}

bool ProtocolXGSDK::isMethodSupport(const char *methodName)
{
	LOGI("isMethodSupport begin");
	JniMethodInfo t;
	if(JniHelper::getMethodInfo(t, XG_PACKAGE, "isMethodSupport", "(Ljava/lang/String;)Z")) {
		LOGI("isMethodSupport is progressing");
		if(mXGEngine == NULL) {
			LOGE("mXGEngine is NULL");
		}
		jstring jmethodName = stoJstring(t.env, methodName);
		bool ret = t.env->CallBooleanMethod(mXGEngine, t.methodID,jmethodName);
		t.env->DeleteLocalRef(jmethodName);
		t.env->DeleteLocalRef(t.classID);
		return ret;
	} else {
		LOGE("JNI METHOD CAN NOT BE FOUND");
		return false;
	}
	LOGI("isMethodSupport end");
}

static pthread_key_t s_threadKey;

static void detach_current_thread (void *env) {
	JniHelper::getJavaVM()->DetachCurrentThread();
}

static bool getEnv(JNIEnv **env)
{
	bool bRet = false;

	switch(JniHelper::getJavaVM()->GetEnv((void**)env, JNI_VERSION_1_4))
	{
		case JNI_OK:
		bRet = true;
		break;
		case JNI_EDETACHED:
		pthread_key_create (&s_threadKey, detach_current_thread);
		if (JniHelper::getJavaVM()->AttachCurrentThread(env, 0) < 0)
		{
			LOGD("%s", "Failed to get the environment using AttachCurrentThread()");
			break;
		}
		if (pthread_getspecific(s_threadKey) == NULL)
		pthread_setspecific(s_threadKey, env);
		bRet = true;
		break;
		default:
		LOGD("%s", "Failed to get the environment using GetEnv()");
		break;
	}

	return bRet;
}

void ProtocolXGSDK::releaseResource()
{
	if(g_channelId != NULL) {
		free(g_channelId);
		g_channelId = NULL;
	}
	if(mXGEngine != NULL) {
		JNIEnv *pEnv = 0;
		if(getEnv(&pEnv)) {
			pEnv->DeleteGlobalRef(mXGEngine);
			mXGEngine = NULL;
		} else {
			LOGE("Failed to get the environment using getEnv()");
		}
	}
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onInitFail(
		JNIEnv *env, jclass, jint code, jstring msg,jstring channelCode)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pChannelCode = jstringTostr(env, channelCode);
	globalListener->onInitFail(code, pMsg, pChannelCode);
	free(pMsg);
	free(pChannelCode);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onInitSuccess
(JNIEnv *env, jclass obj, jint code, jstring msg, jstring channelCode) {
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pChannelCode = jstringTostr(env, channelCode);
	globalListener->onInitSuccess(code, pMsg, pChannelCode);
	free(pMsg);
	free(pChannelCode);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginSuccess(
		JNIEnv *env, jclass, jint code, jstring msg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	globalListener->onLoginSuccess(code, pMsg);
	free(pMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginFail(
		JNIEnv *env, jclass obj, jint code, jstring msg, jstring channelCode)
{
	if(globalListener == NULL)
	{
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pChannelCode = jstringTostr(env, channelCode);
	globalListener->onLoginFail(code, pMsg, pChannelCode);
	free(pMsg);
	free(pChannelCode);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginCancel(
		JNIEnv *env, jclass obj, jint code, jstring msg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	globalListener->onLoginCancel(code, pMsg);
	free(pMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLogoutFinish(
		JNIEnv *env, jclass obj, jint code, jstring msg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	globalListener->onLogoutFinish(code, pMsg);
	free(pMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPaySuccess(
		JNIEnv *env, jclass, jint code, jstring msg, jstring gameTradeNo, jstring xgTradeNo)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pGameTradeNo = jstringTostr(env, gameTradeNo);
	char* pXgTradeNo = jstringTostr(env, xgTradeNo);
	globalListener->onPaySuccess(code, pMsg,pGameTradeNo,pXgTradeNo);
	free(pMsg);
	free(pGameTradeNo);
	free(pXgTradeNo);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayFail(
		JNIEnv *env, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pGameTradeNo = jstringTostr(env, gameTradeNo);
	char* pChannelCode = jstringTostr(env, channelCode);
	char* pChannelMsg = jstringTostr(env, channelMsg);
	globalListener->onPayFail(code, pMsg,pGameTradeNo,pChannelCode,pChannelMsg);
	free(pMsg);
	free(pGameTradeNo);
	free(pChannelCode);
	free(pChannelMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayCancel(
		JNIEnv *env, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pGameTradeNo = jstringTostr(env, gameTradeNo);
	char* pChannelCode = jstringTostr(env, channelCode);
	char* pChannelMsg = jstringTostr(env, channelMsg);
	globalListener->onPayCancel(code, pMsg,pGameTradeNo,pChannelCode,pChannelMsg);
	free(pMsg);
	free(pGameTradeNo);
	free(pChannelCode);
	free(pChannelMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayOthers(
		JNIEnv *env, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pGameTradeNo = jstringTostr(env, gameTradeNo);
	char* pChannelCode = jstringTostr(env, channelCode);
	char* pChannelMsg = jstringTostr(env, channelMsg);
	globalListener->onPayOthers(code, pMsg,pGameTradeNo,pChannelCode,pChannelMsg);
	free(pMsg);
	free(pGameTradeNo);
	free(pChannelCode);
	free(pChannelMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayProgress(
		JNIEnv *env, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	char* pMsg = jstringTostr(env, msg);
	char* pGameTradeNo = jstringTostr(env, gameTradeNo);
	char* pChannelCode = jstringTostr(env, channelCode);
	char* pChannelMsg = jstringTostr(env, channelMsg);
	globalListener->onPayProgress(code, pMsg,pGameTradeNo,pChannelCode,pChannelMsg);
	free(pMsg);
	free(pGameTradeNo);
	free(pChannelCode);
	free(pChannelMsg);
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxExitCallBack_doExit(
		JNIEnv *, jclass)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	globalListener->doExit();
}

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxExitCallBack_onNoChannelExiter(
		JNIEnv *, jclass)
{
	if(globalListener == NULL) {
		LOGE("globalListener is NULL");
	}
	globalListener->onNoChannelExiter();
}

#endif
