package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EndActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
	}
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.endActivity_button1:
			startActivity(new Intent("android.intent.action.MAIN"));
			break;
		}
	}
}
