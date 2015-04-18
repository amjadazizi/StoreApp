package com.storeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

public abstract class BaseActivity extends Activity {

	protected ActionBar actionBar;
	protected abstract int getActivityTitleResId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);

		actionBar = getActionBar();
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'>    "
						+ getResources().getString(getActivityTitleResId())
						+ "</font>"));

		actionBar.setLogo(getActivityHomeIconResId());
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ADD8E6")));
		actionBar.setHomeButtonEnabled(true);

	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	protected int getActivityHomeIconResId() {
		return R.drawable.ic_arrow_left;
	}

	

}
