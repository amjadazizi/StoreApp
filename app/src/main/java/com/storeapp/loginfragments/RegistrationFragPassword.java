package com.storeapp.loginfragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.storeapp.SoundEffects;
import com.storeapp.util.Utils;

public class RegistrationFragPassword extends BaseFragment {
	private View view;
	private Button btnContinuePassword;
	private EditText editPassword, editRepeatPassword;
	private long btnLastClickTime = 0;

	public RegistrationFragPassword() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_login_password, container,
				false);

		editPassword = (EditText) view.findViewById(R.id.editPassword);
		editRepeatPassword = (EditText) view
				.findViewById(R.id.editRepeatPassword);

		btnContinuePassword = (Button) view
				.findViewById(R.id.btnContinuePassword);
		btnContinuePassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - btnLastClickTime < 1000) {

					return;
				}
				btnLastClickTime = SystemClock.elapsedRealtime();

				String password = editPassword.getText().toString();
				String repeatPassword = editRepeatPassword.getText().toString();

				if (!Utils.isNullOrEmpty(password)
						&& !Utils.isNullOrEmpty(repeatPassword)) {

					if (password.equalsIgnoreCase(repeatPassword)) {
						RegisterPrefs.setPassword(password);

						gotoNextFragment();

					} else {
						Utils.showToastShort(getResources().getString(
								R.string.text_password_does_not_match));
					}

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
		return new RegistrationFragChooseInitials();
	}

}
