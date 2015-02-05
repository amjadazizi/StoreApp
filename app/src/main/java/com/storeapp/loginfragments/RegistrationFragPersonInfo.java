package com.storeapp.loginfragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.RegisterPrefs;
import com.storeapp.util.Utils;

public class RegistrationFragPersonInfo extends BaseFragment {
	private View view;

	private EditText editName, editLastName;
	private Button btnContinuePersonInfo;
	private long btnLastClickTime = 0;

	public RegistrationFragPersonInfo() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_login_personinfo, container,
				false);

		getActivity().getActionBar().hide();

		editName = (EditText) view.findViewById(R.id.txtName);
		editLastName = (EditText) view.findViewById(R.id.txtLastName);

//		readPhoneOwnerName();
		btnContinuePersonInfo = (Button) view
				.findViewById(R.id.btnContinuePersonInfo);
		btnContinuePersonInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - btnLastClickTime < 1000) {

					return;
				}
				btnLastClickTime = SystemClock.elapsedRealtime();

				String name = editName.getText().toString();
				String lastName = editLastName.getText().toString();

				if (!Utils.isNullOrEmpty(name)
						&& !Utils.isNullOrEmpty(lastName)) {

					RegisterPrefs.setFirstName(name);
					RegisterPrefs.setLastName(lastName);

				}

				gotoNextFragment();

			}
		});

		return view;
	}

	public void readPhoneOwnerName() {
		Cursor cursor = getActivity().getContentResolver().query(
				ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();

	//	String phoneOwnerName = cursor.getString(cursor
	//			.getColumnIndex("display_name"));
	//	if (phoneOwnerName != null) {

	//		String[] OwnerName = phoneOwnerName.split(" ");
	//		editName.setText(OwnerName[0]);
	//		editLastName.setText(OwnerName[1]);

	//	}

		cursor.close();

	}

	

	private BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			editName.setText("");
			editLastName.setText("");
			readPhoneOwnerName();

		}
	};

	@Override
	public void onPause() {
		super.onPause();
		
		getActivity().unregisterReceiver(br);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
		

		IntentFilter intentFilter = new IntentFilter(
				RegistrationFragChooseInitials.ACTION_REGISTRATION_DONE);
		getActivity().registerReceiver(br, intentFilter);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	protected Fragment getNextFragment() {
		return new RegistrationFragContactInfo();
	}

}
