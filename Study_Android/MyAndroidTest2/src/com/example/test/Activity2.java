package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
	}
	
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.activity2_button1:
			finish();
			break;

		case R.id.activity2_button2:
			startActivity(new Intent(this, EndActivity.class));
			break;
		}
	}
}
