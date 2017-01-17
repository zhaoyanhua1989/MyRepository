package com.example.test.model;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.test.util.OverallVariable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UpdateService {

	private Context mContext;
	private Handler mHandler;

	public UpdateService(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;
	}

	public void readApkInformation() {

	}

	public void checkUpdateInformation() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String url = "";
		asyncHttpClient.get(url, new AsyncHttpResponseHandler() {

			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				// TODO
				mHandler.sendEmptyMessage(OverallVariable.Update.UNDO_UPDATE);
				
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("updateVersion", "");
				data.putString("", "");
				msg.setData(data);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {

			};
		});
	}
}
