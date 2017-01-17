package com.example.test.adapter;

import com.example.test.R;
import com.example.test.util.MyLog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerFragment extends Fragment {

	private int mColor;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.d("Fragment onActivityCreated...");
		super.onActivityCreated(savedInstanceState);
		LinearLayout fl = (LinearLayout) getActivity().findViewById(R.id.viewPager_FragmentLL_left);
		LinearLayout f2 = (LinearLayout) getActivity().findViewById(R.id.viewPager_FragmentLL_bottom);
		fl.setBackgroundColor(mColor);
		f2.setBackgroundColor(mColor);
		((TextView) getActivity().findViewById(R.id.viewPager_FragmentLL_left_textView)).setText("Fragment1");
		((TextView) getActivity().findViewById(R.id.viewPager_FragmentLL_bottom_textView)).setText("Fragment2");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("Fragment onCreateView...");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void setMContent(int color) {
		mColor = color;
	}
}
