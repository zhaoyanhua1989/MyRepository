#include <jni.h>
#include <stdlib.h>

#ifndef PROTOCOLXGSDK_ANDROID_H
#define PROTOCOLXGSDK_ANDROID_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onInitFail(
		JNIEnv *, jclass, jint, jstring,jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onInitSuccess
  (JNIEnv *, jclass, jint, jstring, jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginSuccess(
		JNIEnv *, jclass, jint, jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginFail(
		JNIEnv *, jclass, jint, jstring,jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLoginCancel(
		JNIEnv *, jclass, jint,jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxUserCallBack_onLogoutFinish(
		JNIEnv *, jclass, jint,jstring);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPaySuccess(
		JNIEnv *, jclass, jint code, jstring msg, jstring gameTradeNo, jstring xgTradeNo);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayFail(
		JNIEnv *, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayCancel(
		JNIEnv *, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayOthers(
		JNIEnv *, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg);


JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxPayCallBack_onPayProgress(
		JNIEnv *, jclass, jint code, jstring msg, jstring gameTradeNo, jstring channelCode, jstring channelMsg);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxExitCallBack_doExit(
		JNIEnv *, jclass);

JNIEXPORT void JNICALL Java_com_xgsdk_client_api_cocos2dx_Cocos2dxExitCallBack_onNoChannelExiter(
		JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif

#endif

