package com.storeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;

public class Settings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private static final String EXTRA_DO_ANIMATE = "extra_do_animation";

	private boolean doAnimation = true;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_settings);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			doAnimation = bundle.getBoolean(EXTRA_DO_ANIMATE, false);
		} else if (savedInstanceState != null) {
			doAnimation = false;
		}

		if (doAnimation) {
			overridePendingTransition(R.anim.activity_open_translate,
					R.anim.activity_close_scale);
			SoundEffects.playActivityChangeSound();
		}

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'> Settings</font>"));
		actionBar.setIcon(R.drawable.ic_back);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ADD8E6")));
		actionBar.setHomeButtonEnabled(true);
		getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		startActivity(new Intent(this, Choose_Task.class));

	}

	@Override
	protected void onPause() {

		super.onPause();

		prefs.unregisterOnSharedPreferenceChangeListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		prefs.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();

		if (itemId == android.R.id.home) {

			finish();
			startActivity(new Intent(this, Choose_Task.class));

			return true;

		}
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		String languagePrefKey = getResources().getString(
				R.string.pref_key_change_application_language);
		if (key.equalsIgnoreCase(languagePrefKey)) {
			Prefs.changeUserLanguageToCurrent();

			Intent intent = getIntent();
			intent.putExtra(EXTRA_DO_ANIMATE, false);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			finish();
			overridePendingTransition(0, 0);

			startActivity(intent);
			overridePendingTransition(0, 0);

		}

	}

}
