package com.storeapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.storeapp.db.DbManager;
import com.storeapp.db.UsersManager;
import com.storeapp.loginfragments.RegistrationFragChooseInitials;
import com.storeapp.loginfragments.RegistrationFragPersonInfo;
import com.storeapp.util.Utils;

public class Login extends BaseActivity implements OnClickListener {

	private static final String KEY_ANIMATE_LOGO = "key_animate_logo";
	private static final String KEY_SLIDING_DRAWER_OPEN = "sliding_drawer_open";
	private static final String KEY_CURRENT_FRAGMENT_INDEX = "current_fragment_index";

	private Button slideButton;
	private SlidingDrawer slidingDrawer;
	private TextView txtStoreApp;

	private Animation animTranslate;
	private EditText liInitials, liPassword;

	private boolean animationDone = false;
	private boolean isSlidDraerOpen = false;
	private int currentFragIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		if (Prefs.isUserLoggedIn()) {
			Intent intent = new Intent(Login.this, Choose_Task.class);
			startActivity(intent);
			finish();
		}

		// do not do sound effect for this activity
		doSoundeffect = false;

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



		if (savedInstanceState == null) {
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			Fragment fragment = new RegistrationFragPersonInfo();
			transaction.add(R.id.fragConLay, fragment);
			// transaction.addToBackStack(null);
			transaction.commit();
		}

		// get fragment count after the first one is added
		currentFragIndex = getFragmentManager().getBackStackEntryCount();
		if (savedInstanceState != null) {
			animationDone = savedInstanceState.getBoolean(KEY_ANIMATE_LOGO);
			isSlidDraerOpen = savedInstanceState
					.getBoolean(KEY_SLIDING_DRAWER_OPEN);
			currentFragIndex = savedInstanceState
					.getInt(KEY_CURRENT_FRAGMENT_INDEX);

		}

		liInitials = (EditText) findViewById(R.id.liInitials);
		liPassword = (EditText) findViewById(R.id.liPassword);

		getActionBar().hide();

		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {

				slideButton.setText(R.string.text_close);
				isSlidDraerOpen = true;

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				int stactCount = getFragmentManager().getBackStackEntryCount();
				if (stactCount > 0) {

					for (int i = stactCount; i >= 0; i--) {
						getFragmentManager().popBackStack();

					}

				}

				slideButton.setText(R.string.text_register);
				isSlidDraerOpen = false;

			}
		});

		Button liLoginBtn = (Button) findViewById(R.id.liLoginBtn);
		liLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String initials = liInitials.getText().toString(), password = liPassword
						.getText().toString();

				UsersManager um = DbManager.getDbManager().getUserManager();

				if (um.loginExists(initials, password)) {

					Prefs.setUserLoggedIn(true);
					Prefs.setStringPref(Prefs.KEY_INITIALS, liInitials
							.getText().toString());

					startActivity(new Intent(Login.this, Choose_Task.class));
					finish();

				} else {
					Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					vibrator.vibrate(500);

					Utils.showToastLong(getResources().getString(
							R.string.text_wrong_credentials));

				}

			}
		});

		final LinearLayout loginBox = (LinearLayout) findViewById(R.id.LoginBox);
		loginBox.setVisibility(View.GONE);

		slideButton = (Button) findViewById(R.id.slideButton);
		slideButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (slidingDrawer.isOpened()) {
					RegistrationFragPersonInfo personInfo = new RegistrationFragPersonInfo();
					personInfo.readPhoneOwnerName();

				}

			}
		});

		txtStoreApp = (TextView) findViewById(R.id.txtStoreApp);
		animTranslate = AnimationUtils.loadAnimation(Login.this,
				R.anim.translate);
		animTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				loginBox.setVisibility(View.VISIBLE);
				slideButton.setVisibility(View.VISIBLE);
				Animation animFade = AnimationUtils.loadAnimation(Login.this,
						R.anim.fade);
				loginBox.startAnimation(animFade);

				animationDone = true;

				if (isSlidDraerOpen) {
					slidingDrawer.animateOpen();
				}
			}

		});

		if (animationDone) {
			animTranslate.setDuration(0);
		}

		txtStoreApp.startAnimation(animTranslate);

		IntentFilter intentFilter = new IntentFilter(
				RegistrationFragChooseInitials.ACTION_REGISTRATION_DONE);
		registerReceiver(br, intentFilter);

	}

	@Override
	public void onBackPressed() {

		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();

		} else if (getFragmentManager().getBackStackEntryCount() <= 0) {
			if (slidingDrawer.isOpened()) {
				slidingDrawer.animateClose();

			} else {
				super.onBackPressed();
			}

		}
	}

	@Override
	public void onClick(View v) {

	}

	private BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			slidingDrawer.animateClose();

		}
	};

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(br);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_ANIMATE_LOGO, animationDone);
		outState.putBoolean(KEY_SLIDING_DRAWER_OPEN, isSlidDraerOpen);

		currentFragIndex = getFragmentManager().getBackStackEntryCount();
		outState.putInt(KEY_CURRENT_FRAGMENT_INDEX, currentFragIndex);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isSlidDraerOpen = savedInstanceState
				.getBoolean(KEY_SLIDING_DRAWER_OPEN);
		animationDone = savedInstanceState.getBoolean(KEY_ANIMATE_LOGO);
		currentFragIndex = savedInstanceState
				.getInt(KEY_CURRENT_FRAGMENT_INDEX);

	}

	@Override
	protected int getActivityTitleResId() {
		return R.string.title_activity_login;
	}

}
