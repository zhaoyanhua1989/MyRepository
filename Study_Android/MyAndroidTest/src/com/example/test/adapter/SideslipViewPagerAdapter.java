package com.example.test.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.entity.FriendEntity;
import com.example.test.util.MyLog;
import com.example.test.util.RUtil;
import com.example.test.util.ToastUtil;

public class SideslipViewPagerAdapter extends FragmentPagerAdapter {

	private int mCount;
	private List<FriendEntity> mFriends = new ArrayList<FriendEntity>();

	public SideslipViewPagerAdapter(int count, FragmentManager fm, List<FriendEntity> friends) {
		super(fm);
		mCount = count;
		mFriends = friends;
	}

	public SideslipViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return new SideslipFragment1(mFriends);
		} else {
			return new SideslipFragment2();
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}
}

class SideslipFragment1 extends Fragment {

	private List<FriendEntity> mFriends = new ArrayList<FriendEntity>();

	public SideslipFragment1(List<FriendEntity> friends) {
		mFriends = friends;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("SideslipFragment1 onCreateView...");
		FrameLayout layout = (FrameLayout) inflater.inflate(RUtil.getLayout(getActivity(), "sideslip_viewpager_fragmentlayout"), container, false);
		ListView mListView = (ListView) layout.findViewById(RUtil.getId(getActivity(), "sideslip_viewpager_fragment_listview"));
		SideslipListViewAdapter mAdapter = new SideslipListViewAdapter(getActivity(), mFriends);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				ToastUtil.showCustomToast(getActivity(), "选中了" + position);
			}
		});
		layout.setBackgroundColor(Color.YELLOW);
		return layout;
	}

	class SideslipListViewAdapter extends BaseAdapter {

		private List<FriendEntity> friends = new ArrayList<FriendEntity>();
		private Activity mActivity;

		public SideslipListViewAdapter(Activity activity, List<FriendEntity> friends) {
			super();
			mActivity = activity;
			this.friends = friends;
		}

		@Override
		public int getCount() {
			return friends.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mActivity).inflate(RUtil.getLayout(mActivity, "sideslip_viewpager_fragmentl_listview_item"), null);
				holder.idTV = (TextView) convertView.findViewById(RUtil.getId(mActivity, "sideslip_viewpager_fragment_listview_id"));
				holder.nameTV = (TextView) convertView.findViewById(RUtil.getId(mActivity, "sideslip_viewpager_fragment_listview_name"));
				holder.genderTV = (TextView) convertView.findViewById(RUtil.getId(mActivity, "sideslip_viewpager_fragment_listview_gender"));
				holder.timeTV = (TextView) convertView.findViewById(RUtil.getId(mActivity, "sideslip_viewpager_fragment_listview_time"));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.idTV.setText(friends.get(position).getId() + "");
			holder.nameTV.setText(friends.get(position).getName());
			holder.genderTV.setText(friends.get(position).getGender());
			holder.timeTV.setText(friends.get(position).getAddTime());
			return convertView;
		}
	}
}

class ViewHolder {
	TextView idTV;
	TextView nameTV;
	TextView genderTV;
	TextView timeTV;
}

class SideslipFragment2 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("SideslipFragment2 onCreateView...");
		FrameLayout layout = (FrameLayout) inflater.inflate(RUtil.getLayout(getActivity(), "sideslip_viewpager_fragmentlayout"), container, false);
		ListView mListView = (ListView) layout.findViewById(RUtil.getId(getActivity(), "sideslip_viewpager_fragment_listview"));
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, RUtil.getValuesStringArray(getActivity(), "sideslipView_fragment2List")));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				dealWithProviderOperation(position);
			}
		});
		layout.setBackgroundColor(Color.BLUE);
		return layout;
	}

	/**
	 * 处理ContentProvider相关的操作, 这里测试CustomContentProvider是否可用
	 * 
	 * @param position
	 *            点击的位置
	 */
	protected void dealWithProviderOperation(int position) {
		Uri uri = Uri.parse("content://hb.android.contentProvider/teacher");
		ContentResolver cr = getActivity().getContentResolver();
		switch (position) {
		case 0:
			ContentValues cv1 = new ContentValues();
			cv1.put("title", "jiaoshou");
			cv1.put("name", "jiaoshi");
			cv1.put("sex", true);
			Uri uri2 = cr.insert(uri, cv1);
			ToastUtil.showCustomToast(getActivity(), "插入数据，uri=" + uri2.toString());
			break;
		case 1:
			// 查找id为1的数据
			Cursor c2 = cr.query(uri, null, "_ID=?", new String[] { "1" }, null);
			// 这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a
			// size of 1错误；cr.query返回的是一个结果集。
			if (c2.moveToFirst() == false) {
				// 为空的Cursor
				return;
			}
			int name = c2.getColumnIndex("name");
			ToastUtil.showCustomToast(getActivity(), "查询id=1的数据，name=" + c2.getString(name));
			c2.close();
			break;
		case 2:
			Cursor c1 = cr.query(uri, null, null, null, null);
			ToastUtil.showCustomToast(getActivity(), "查询所有数据，总共有 " + c1.getCount() + "条数据");
			c1.close();
			break;
		case 3:
			ContentValues cv2 = new ContentValues();
			cv2.put("name", "huangbiao");
			cv2.put("date_added", (new Date()).toString());
			int count1 = cr.update(uri, cv2, "_ID=?", new String[] { "3" });
			ToastUtil.showCustomToast(getActivity(), "修改数据，成功执行了" + count1 + "条sql");
			break;
		case 4:
			int count2 = cr.delete(uri, "_ID=?", new String[]{"2"});
			ToastUtil.showCustomToast(getActivity(), "删除数据，成功执行了" + count2 + "条sql");
			break;
		default:
			break;
		}
	}
}
