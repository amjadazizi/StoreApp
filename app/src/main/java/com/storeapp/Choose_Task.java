package com.storeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.storeapp.db.DbManager;
import com.storeapp.db.UsersManager;
import com.storeapp.model.User;

public class Choose_Task extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_choose_task_dashboard);

		if (!Prefs.isUserLoggedIn()) {
			startActivity(new Intent(this, Login.class));
		}

		startService(new Intent(this, MaintenanceService.class));

		actionBar.hide();

		final Button btnEmpInfo = (Button) findViewById(R.id.btnEmpInfo);

		btnEmpInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Choose_Task.this, Emp_Info.class));

			}
		});

		Button ctLogOut = (Button) findViewById(R.id.ctLogOut);
		ctLogOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				logout();

			}
		});

		Button ctCashRegBtn = (Button) findViewById(R.id.ctCashRegBtn);
		ctCashRegBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Choose_Task.this,
						Cash_Register.class);
				startActivity(intent);

			}
		});

		Button ctStoreStatus = (Button) findViewById(R.id.ctStoreStatus);
		ctStoreStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Choose_Task.this, Store_Status.class);
				startActivity(intent);

			}
		});

		Button ctStoreItemBtn = (Button) findViewById(R.id.ctStoreItemBtn);
		ctStoreItemBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Choose_Task.this, Store_Items.class));

			}
		});

		Button ctEmplList = (Button) findViewById(R.id.ctEmplList);
		ctEmplList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Choose_Task.this, Emp_List.class));
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();

		logout();
	}

	private void logout() {
		if (Prefs.isUserLoggedIn()) {
			new AlertDialog.Builder(Choose_Task.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.text_log_out)
					.setMessage(R.string.text_want_log_out)
					.setPositiveButton(R.string.text_yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Prefs.setUserLoggedIn(false);
									startActivity(new Intent(Choose_Task.this,
											Login.class));
									finish();

								}
							}).setNegativeButton(R.string.text_no, null).show();

		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			startActivity(new Intent(Choose_Task.this, Settings.class));
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_choose_task;
	}

}
