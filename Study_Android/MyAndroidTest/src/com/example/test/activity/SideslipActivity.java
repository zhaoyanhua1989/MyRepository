package com.example.test.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;
import android.widget.ShareActionProvider;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.adapter.SideslipViewPagerAdapter;
import com.example.test.entity.FriendEntity;
import com.example.test.model.DBContext;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.RUtil;
import com.example.test.util.ToastUtil;
import com.example.test.view.SideslipMenu;
import com.example.test.view.SideslipMenu.SideslipMenuListener;

/**
 * 侧滑菜单类，结合了ActionBar相关功能 知识点： 1.setOverflowShowingAlways方法，总是显示溢出菜单
 * 2.重写onMenuOpened方法，让溢出菜单中总是显示图标 3.ActionBar的导航功能，返回主Activity
 * 4.侧滑菜单，控制fragment显示
 * 
 * @author HKW2962
 *
 */
public class SideslipActivity extends FragmentActivity implements TabListener {

	private ActionBar mActionBar;
	private ViewPager mViewPager;
	private List<FriendEntity> friends = new ArrayList<FriendEntity>();
	private SideslipMenu mSlidingMenu; // 侧滑菜单

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_activity_sideslip);
		MyApplication.activitys.add(this);
		// 初始化数据
		initData();
		// 设置viewPager
		initViewPager();
		// 设置侧滑菜单
		initSideslipMenu();
		// 创建ActionBar
		initActionBar();
		// 设置右上角溢出菜单总是显示
		setOverflowShowingAlways();
	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		DBContext dbContext = new DBContext(this, OverallVariable.CUSTOMDATABASE, "friends");
		// 插入数据
		ContentValues values1 = new ContentValues();
		values1.put("id", 1);
		values1.put("name", "关羽");
		values1.put("gender", "男");
		values1.put("addTime", "206-08-16 15:36:33");
		long rowId1 = dbContext.execInsert(values1);
		MyLog.d("SideslipActivity initData end, rowId=" + rowId1);
		ContentValues values2 = new ContentValues();
		values2.put("id", 2);
		values2.put("name", "妙妙");
		values2.put("gender", "女");
		values2.put("addTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		long rowId2 = dbContext.execInsert(values2);
		MyLog.d("SideslipActivity initData end, rowId=" + rowId2);
		ContentValues values3 = new ContentValues();
		values3.put("id", 3);
		values3.put("name", "青河");
		values3.put("gender", "男");
		values3.put("addTime", "3056-12-22 21:36:59");
		long rowId3 = dbContext.execInsert(values3);
		MyLog.d("SideslipActivity initData end, rowId=" + rowId3);
		ContentValues values4 = new ContentValues();
		values4.put("id", 4);
		values4.put("name", "貂蝉");
		values4.put("gender", "女");
		values4.put("addTime", "226-12-11 9:50:12");
		long rowId4 = dbContext.execInsert(values4);
		MyLog.d("SideslipActivity initData end, rowId=" + rowId4);
		ContentValues values5 = new ContentValues();
		values5.put("id", 5);
		values5.put("name", "赵云");
		values5.put("gender", "男");
		values5.put("addTime", "229-10-02 17:55:09");
		long rowId5 = dbContext.execInsert(values5);
		MyLog.d("SideslipActivity initData end, rowId=" + rowId5);
		// 获取数据
		Cursor mCursor = dbContext.execQuery("select id,name,gender,addTime from friends");
		while (mCursor.moveToNext()) {
			FriendEntity friend = new FriendEntity();
			friend.setId(mCursor.getInt(0));
			friend.setName(mCursor.getString(1));
			friend.setGender(mCursor.getString(2));
			friend.setAddTime(mCursor.getString(3));
			MyLog.d("SideslipActivity initData, friend=" + friend.toString());
			friends.add(friend);
		}
		mCursor.close();
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(RUtil.getId(this, "sideslip_viewPager"));
		SideslipViewPagerAdapter mAdapter = new SideslipViewPagerAdapter(2, getSupportFragmentManager(), friends);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				mActionBar.setSelectedNavigationItem(index);
			}

			@Override
			public void onPageScrolled(int index, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int index) {

			}
		});
	}

	/************************ 侧滑菜单弹 ************************/

	private void initSideslipMenu() {
		try {
			mSlidingMenu = new SideslipMenu(this, 20, new SideslipMenuListener() {

				@Override
				protected void onSideslipMenuClicked(int index) {
					if (index < mViewPager.getChildCount()) {
						mViewPager.setCurrentItem(index);
					}
				}
			});
			mSlidingMenu.setSideslipMenuBtName(readSideslipMenuBtName(20));
		} catch (Exception e) {
			MyLog.e("set SideslipMenu error, exception is：" + e.getMessage());
		}
	}

	private String[] readSideslipMenuBtName(int amount) throws Exception {
		XmlResourceParser xmlResourceParser = getResources().getXml(R.xml.slideslip_button_name);
		int eventType = xmlResourceParser.getEventType();
		String[] names = new String[amount];
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {
				for (int i = 0; i < amount; i++) {
					String tagName = xmlResourceParser.getName();
					if (("name" + i).equals(tagName)) {
						names[i] = xmlResourceParser.nextText();
						break;
					}
				}
			}
			eventType = xmlResourceParser.next();
		}
		return names;
	}

	// 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
	private float x1 = 0;
	private float x2 = 0;
	private float y1 = 0;
	private float y2 = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 继承了Activity的onTouchEvent方法，直接监听点击事件
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 当手指按下的时候
			x1 = event.getX();
			y1 = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 当手指离开的时候
			x2 = event.getX();
			y2 = event.getY();
			if (y1 - y2 > 50 && ((y1 - y2) > Math.abs(x1 - x2))) {
				MyLog.d("SideslipActivity 向上滑动");
			} else if (y2 - y1 > 50 && ((y2 - y1) > Math.abs(x1 - x2))) {
				MyLog.d("SideslipActivity 向下滑动");
			} else if (x1 - x2 > 50 && ((x1 - x2) > Math.abs(y1 - y2))) {
				// 第三方侧滑菜单已提供显示和隐藏监听，只是手指要按到左边最边上滑动，不方便
				mSlidingMenu.closeMenu();
			} else if (x2 - x1 > 50 && ((x2 - x1) > Math.abs(y1 - y2))) {
				// 第三方侧滑菜单已提供显示和隐藏监听，只是手指要按到左边最边上滑动，不方便
				mSlidingMenu.showMenu();
			}
		}
		return true;
	}

	/************************ 创建ActionBar和Tab ************************/
	private void initActionBar() {
		mActionBar = getActionBar();
		// 隐藏logo和icon，不想隐藏可以注释
		// mActionBar.setDisplayShowHomeEnabled(false);
		// 隐藏Label标签，不想隐藏可以注释
		// mActionBar.setDisplayShowTitleEnabled(false);
		// 设置ActionBar展示模式
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.addTab(mActionBar.newTab().setText("friends").setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("fragment2").setTabListener(this));
		// 设置导航功能(点击返回上一页)
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// 当Tab选中时
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	/******************** 创建ActionBar的功能按钮(右上角) ********************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_viewpager_activity_menu, menu);
		// 设置searchView展开和合并的不同界面
		MenuItem searchItem = menu.findItem(R.id.viewPager_action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// 尝试没有效果
			searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {
					MyLog.d("on searchItem expand");
					return true;
				}

				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					MyLog.d("on searchItem collapse");
					return true;
				}
			});
		} else {
			searchView.setOnCloseListener(new SearchView.OnCloseListener() {

				@Override
				public boolean onClose() {
					MyLog.d("on searchView collapse");
					return false;
				}
			});
		}
		// 显示自定义的Action Provider
		MenuItem shareItem = menu.findItem(R.id.viewPager_action_share);
		ShareActionProvider provider = (ShareActionProvider) shareItem.getActionProvider();
		// 设置分享意图
		provider.setShareIntent(getDefaultIntent());
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * @return intent，获取所有的分享应用的图片
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 左上角ActionBar标签，实现导航跳转回主Activity
			// 获取跳转到MainActivity的intent
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			// 如果需要创建task(不在同一个task中)，则创建task再跳转，否则直接通过navigateUpTo跳转
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		default:
			ToastUtil.showCustomToast(this, "点击-" + item.getTitle());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 溢出菜单中，默认是不显示图标的，这里调用MenuBuilder类的setOptionalIconsVisible方法来显示图标
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					MyLog.e("SideslipActivity onMenuOpened exception, e=" + e.getMessage());
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	/**
	 * 设置右上角溢出菜单总是显示。溢出菜单在有menu键的手机上通常不显示出来，按menu键就直接弹出来。
	 */
	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
