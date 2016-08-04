package com.example.test.adapter;

import com.example.test.R;
import com.example.test.util.MyLog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment1 extends Fragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.d("Fragment1 onActivityCreated...");
		super.onActivityCreated(savedInstanceState);
		LinearLayout fl = (LinearLayout) getActivity().findViewById(R.id.viewPager_FragmentLL_left);
		LinearLayout f2 = (LinearLayout) getActivity().findViewById(R.id.viewPager_FragmentLL_bottom);
		fl.setBackgroundColor(Color.GREEN);
		f2.setBackgroundColor(Color.GREEN);
		((TextView)getActivity().findViewById(R.id.viewPager_FragmentLL_left_textView)).setText("Fragment1");
		((TextView)getActivity().findViewById(R.id.viewPager_FragmentLL_bottom_textView)).setText("Fragment2");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("Fragment1 onCreateView...");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
