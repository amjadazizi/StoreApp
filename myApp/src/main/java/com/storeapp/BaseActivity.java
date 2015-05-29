package com.storeapp;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
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

}
