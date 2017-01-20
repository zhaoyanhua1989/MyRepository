package com.example.test.base;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ZHttpResponseHandler extends AsyncHttpResponseHandler{

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
		
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		
	}
	
	

}
