package com.storeapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.storeapp.empinfofragments.FragmentRreadMsg;
import com.storeapp.empinfofragments.FragmentWriteMsg;

public class Emp_Info extends BaseFragmentActivity implements TabListener {

	ActionBar actionBar;
	ViewPager viewPager;
	int onStartCount = 0;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_emp__info);
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);

				Fragment fragment = findFragmentByPosition(position);
				if (fragment instanceof UpdateFragment) {
					UpdateFragment uf = (UpdateFragment) fragment;
					uf.update();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		actionBar = getActionBar();
		actionBar.setTitle(Html.fromHtml("<font color='#000000'>  "
				+ getResources().getString(R.string.title_activity_emp__info)
				+ "</font>"));
		actionBar.setIcon(R.drawable.ic_home_24);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ADD8E6")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tabWrite = actionBar.newTab();
		tabWrite.setText(getResources().getString(R.string.text_read_messages));
		tabWrite.setTabListener(this);

		ActionBar.Tab tabRead = actionBar.newTab();
		tabRead.setText(getResources().getString(R.string.text_write_messages));
		tabRead.setTabListener(this);

		actionBar.addTab(tabWrite);

		actionBar.addTab(tabRead);

	}

	public Fragment findFragmentByPosition(int position) {

		FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager
				.getAdapter();

		String defaultFragmentTag = "android:switcher:" + viewPager.getId()
				+ ":" + fragmentPagerAdapter.getItemId(position);
		return getSupportFragmentManager()
				.findFragmentByTag(defaultFragmentTag);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case android.R.id.home:

			finish();
			break;

		}

		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

}

class MyAdapter extends FragmentPagerAdapter {

	public MyAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;

		if (position == 0) {
			fragment = new FragmentRreadMsg();
		}
		if (position == 1) {
			fragment = new FragmentWriteMsg();
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
