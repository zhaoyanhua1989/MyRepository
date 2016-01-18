
package com.xgsdk.client.api.unity3d.service;

import com.xgsdk.client.api.utils.XGInfo;
import com.xgsdk.client.api.utils.XGLog;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionHelper {

    protected static String ACCOUNT_VERIFY_SESSION_URI = "/account/verify-session/";
    private static final String TIME_PATTERN = "yyyyMMddHHmmss";
    private static final SimpleDateFormat sTsFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

    public SessionHelper() {
    }

    protected static String ts() {
        return sTsFormat.format(new Date(System.currentTimeMillis()));
    }

    public static String generateVerifySessionUrl(String authInfo) {
        String url = "";
        try {
            List<NameValuePair> requestParams = generateBasicRequestParams(null, "verify-session");
            requestParams.add(new BasicNameValuePair("authInfo", authInfo));
            String requestContent = generateSignRequestContent(null, requestParams);
            url = (new StringBuilder(String.valueOf(XGInfo.getXGAuthUrl()))).append(ACCOUNT_VERIFY_SESSION_URI).append(XGInfo.getXGAppId())
                    .append("?").append(requestContent).toString();
        } catch (Exception e) {
            XGLog.e((new StringBuilder("generateVerifySessionUrl fail,error is ")).append(e.getMessage()).toString());
        }
        return url;
    }

    public static List<NameValuePair> generateBasicRequestParams(Context context, String interfaceType) {
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        String appId = XGInfo.getXGAppId();
        if (!TextUtils.isEmpty(appId))
            requestParams.add(new BasicNameValuePair("xgAppId", appId));
        String channelId = XGInfo.getChannelId();
        if (!TextUtils.isEmpty(channelId))
            requestParams.add(new BasicNameValuePair("channelId", channelId));
        if (!TextUtils.isEmpty(XGInfo.getXGVersion()))
            requestParams.add(new BasicNameValuePair("sdkVersion", XGInfo.getXGVersion()));
        requestParams.add(new BasicNameValuePair("ts", ts()));
        String planId = XGInfo.getXGPlanId();
        if (!TextUtils.isEmpty(planId))
            requestParams.add(new BasicNameValuePair("planId", planId));
        String buildNumber = XGInfo.getXGBuildNumber();
        if (!TextUtils.isEmpty(buildNumber))
            requestParams.add(new BasicNameValuePair("buildNumber", buildNumber));
        String deviceId = XGInfo.getXGDeviceId();
        if (!TextUtils.isEmpty(deviceId))
            requestParams.add(new BasicNameValuePair("deviceId", deviceId));
        requestParams.add(new BasicNameValuePair("type", interfaceType));
        return requestParams;
    }

    public static String generateSignRequestContent(Context context, List<NameValuePair> requestParams) throws Exception {
        Collections.sort(requestParams, new Comparator<NameValuePair>() {
            public int compare(NameValuePair lhs, NameValuePair rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        StringBuilder strSign = new StringBuilder();
        for (int i = 0; i < requestParams.size(); i++) {
            NameValuePair nvPair = (NameValuePair) requestParams.get(i);
            strSign.append(nvPair.getName()).append("=").append(nvPair.getValue());
            if (i < requestParams.size() - 1)
                strSign.append("&");
        }

        String requestContent = URLEncodedUtils.format(requestParams, "UTF-8");
        String sign = SHA1Util.HmacSHA1EncryptByte(strSign.toString(), XGInfo.getXGAppKey());
        XGLog.d((new StringBuilder("before sign:")).append(strSign.toString()).toString());
        XGLog.d((new StringBuilder("after sign:")).append(sign).toString());
        return new StringBuilder(requestContent).append("&sign=").append(sign).toString();
    }

}
