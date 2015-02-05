package com.storeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.storeapp.db.CommunicationManager;
import com.storeapp.db.DbManager;
import com.storeapp.empinfofragments.FragmentRreadMsg;
import com.storeapp.model.Message;
import com.storeapp.util.Utils;

public class Message_Details extends BaseActivity {

	protected static final String LOG_KEY = null;

	private int messageId = -1;
	private Message message;
	CommunicationManager communicationManager = DbManager.getDbManager()
			.getCommunicationManager();
	private TextView txtFromInput, txtHealineInput, txtTimeInput;
	private EditText editMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message__details);

		txtFromInput = (TextView) findViewById(R.id.txtFromInput);
		txtHealineInput = (TextView) findViewById(R.id.txtHealineInput);

		editMsg = (EditText) findViewById(R.id.editMsg);

		txtTimeInput = (TextView) findViewById(R.id.txtTimeInput);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(Html
				.fromHtml("<font color='#000000'>  Message Details</font>"));

		actionBar.setLogo(R.drawable.ic_home_24);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ADD8E6")));
		actionBar.setHomeButtonEnabled(true);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {

			messageId = bundle.getInt(FragmentRreadMsg.EXTRA_MESSAGE_ID, -1);
			message = communicationManager.getMessage(messageId);

			txtFromInput.setText(message.getSender());
			txtHealineInput.setText(message.getHeadline());
			editMsg.setText(message.getMessage());
			txtTimeInput.setText(Utils.convertTime(message.getTime()));

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

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
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(Message_Details.this, Choose_Task.class));
			finish();
			break;

		}
		return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_message__details;
	}

}
