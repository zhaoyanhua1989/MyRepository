package com.example.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final int ALERT_DIALOG = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(ALERT_DIALOG);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		if (id == ALERT_DIALOG) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog_MyTheme);
			builder.setMessage("Hello World").setTitle("Alert Dialog").setIcon(android.R.drawable.ic_dialog_alert)
					.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = builder.create();
		}
		if (dialog == null) {
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}
	
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.button2:
			startActivity(new Intent("android.intent.action.MAIN"));
			break;
		case R.id.button3:
			startActivity(new Intent(this, Activity1.class));
			break;
		}
	}
}
