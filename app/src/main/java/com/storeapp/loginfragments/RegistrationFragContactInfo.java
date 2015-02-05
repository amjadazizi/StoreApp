package com.storeapp.loginfragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.RegisterPrefs;
import com.storeapp.util.Utils;

public class RegistrationFragContactInfo extends BaseFragment {
	private View view;
	private Button btnContinueContactInfo;
	private EditText editEmail, editPhoneNum;
	private long btnLastClickTime = 0;

	public RegistrationFragContactInfo() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_login_contactinfo, container,
				false);

		editEmail = (EditText) view.findViewById(R.id.editEmail);
		editPhoneNum = (EditText) view.findViewById(R.id.editPhoneNum);

		btnContinueContactInfo = (Button) view
				.findViewById(R.id.btnContinueContactInfo);
		btnContinueContactInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - btnLastClickTime < 1000) {
					Utils.showToastShort("Returner");

					return;
				}
				btnLastClickTime = SystemClock.elapsedRealtime();

				String email = editEmail.getText().toString();
				String phoneNumber = editPhoneNum.getText().toString();

				if (!Utils.isNullOrEmpty(email)
						&& !Utils.isNullOrEmpty(phoneNumber)) {

					RegisterPrefs.setEmal(email);
					RegisterPrefs.setPhoneNumber(phoneNumber);

					gotoNextFragment();

				} else {
					Utils.showToastShort(getResources().getString(
							R.string.text_alle_fields_required));
				}

			}
		});

		return view;

	}

	@Override
	protected Fragment getNextFragment() {
		return new RegistrationFragPassword();
	}

}
