package com.storeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.storeapp.db.DbManager;
import com.storeapp.db.UsersManager;
import com.storeapp.model.User;
import com.storeapp.util.Utils;

public class Emp_Details extends BaseActivity {

	private TextView txtPersonName;
	private Button btnCall, btnSMS, btnEmail;

	private int empId = -1;
	private User user;
	private UsersManager um = DbManager.getDbManager().getUserManager();

	private SensorManager sensorManager;

	private ShakeEventListener sensorListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emp_details);

		txtPersonName = (TextView) findViewById(R.id.txtPersonName);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorListener = new ShakeEventListener();

		sensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {
						Utils.showToastLong(getResources().getString(
								R.string.text_you_are_now_calling_to)

								+ user.getName());

						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						// Vibrate for 500 milliseconds
						v.vibrate(500);
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"
								+ user.getPhoneNumb()));
						startActivity(callIntent);

					}
				});

		btnCall = (Button) findViewById(R.id.btnCall);
		btnCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + user.getPhoneNumb()));
				startActivity(callIntent);

			}
		});

		btnSMS = (Button) findViewById(R.id.btnSMS);
		btnSMS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
				smsIntent.setData(Uri.parse("smsto:" + user.getPhoneNumb()));
				startActivity(Intent.createChooser(smsIntent, ""));

			}
		});

		btnEmail = (Button) findViewById(R.id.btnEmail);
		btnEmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.fromParts("mailto", user.getEmail(), null));

				startActivity(Intent.createChooser(emailIntent,
						"Choose an email client :"));
			}
		});

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			empId = bundle.getInt(Emp_List.EXTRA_EMPLOYEE_ID, -1);

			user = um.getUser(empId);
			txtPersonName.setText(user.getName());

			btnCall.setText("  " + user.getPhoneNumb());
			btnSMS.setText("  " + user.getPhoneNumb());
			btnEmail.setText("  " + user.getEmail());

		}

		if (empId == -1) {

			Utils.showToastLong("Invalid Employee Id");
			finish();
			return;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		// finish();
	}

	@Override
	protected void onResume() {
		super.onResume();

		sensorManager.registerListener(sensorListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorListener);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, Choose_Task.class));
			this.finish();
			break;

		}

		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_emp_details;
	}

}
