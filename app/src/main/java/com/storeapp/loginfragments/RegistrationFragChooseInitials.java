package com.storeapp.loginfragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.RegisterPrefs;
import com.storeapp.db.DbManager;
import com.storeapp.db.UsersManager;
import com.storeapp.model.User;
import com.storeapp.util.Utils;

public class RegistrationFragChooseInitials extends BaseFragment {
	private View view;
	private Button btnDone;
	private RadioButton initial1, initial2, initial3, initial4;

	public static final String ACTION_REGISTRATION_DONE = "com.storeapp.registration.done.intent.action";

	UsersManager userManager = DbManager.getDbManager().getUserManager();

	public RegistrationFragChooseInitials() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_login_chooseinitials,
				container, false);

		btnDone = (Button) view.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				RadioGroup initialsBtnGroup = (RadioGroup) view
						.findViewById(R.id.radInitialGroup);
				int selected = initialsBtnGroup.getCheckedRadioButtonId();
				RadioButton btn = (RadioButton) view.findViewById(selected);

				String choosenInitials = btn.getText().toString();
				if (!Utils.isNullOrEmpty(choosenInitials)) {

					User user = new User();

					user.setName(RegisterPrefs.getFirstName() + " "
							+ RegisterPrefs.getLastName());

					user.setEmail(RegisterPrefs.getEmail());
					user.setPhoneNumb(RegisterPrefs.getPhoneNumber());
					user.setPassword(RegisterPrefs.getPassword());
					user.setInitials(choosenInitials);

					int userId = userManager.createUser(user);

					if (userId > 0) {

						Utils.showToastShort("Your Initials are: "
								+ choosenInitials);

						Intent intent = new Intent(ACTION_REGISTRATION_DONE);
						getActivity().sendBroadcast(intent);

						FragmentManager fragmentManager = getFragmentManager();

						gotoNextFragment();

						int count = fragmentManager.getBackStackEntryCount();
						for (int i = 0; i <= count; ++i) {
							fragmentManager.popBackStackImmediate();
						}

					} else {
						Utils.showToastShort("Error. Please Try Again");

					}

				}

			}
		});

		setRadioBtns();
		return view;

	}

	public void setRadioBtns() {
		initial1 = (RadioButton) view.findViewById(R.id.initial1);
		initial2 = (RadioButton) view.findViewById(R.id.initial2);
		initial3 = (RadioButton) view.findViewById(R.id.initial3);
		initial4 = (RadioButton) view.findViewById(R.id.initial4);

		initial1.setVisibility(View.GONE);
		initial2.setVisibility(View.GONE);
		initial3.setVisibility(View.GONE);
		initial4.setVisibility(View.GONE);

		List<String> initials = GenerateIntials(RegisterPrefs.getFirstName(),
				RegisterPrefs.getLastName());

		int len = initials.size();
		if (len > 0) {
			initial1.setText(initials.get(0));
			initial1.setVisibility(View.VISIBLE);
		}
		if (len > 1) {
			initial2.setText(initials.get(1));
			initial2.setVisibility(View.VISIBLE);
		}
		if (len > 2) {
			initial3.setText(initials.get(2));
			initial3.setVisibility(View.VISIBLE);
		}
		if (len > 3) {
			initial4.setText(initials.get(3));
			initial4.setVisibility(View.VISIBLE);
		}
	}

	public List<String> GenerateIntials(String Fn, String Ln) {

		if ((Fn == null || Fn.length() < 2) || (Ln == null || Ln.length() < 2)) {
			return null;
		}

		List<String> initialsList = new ArrayList<String>();
		List<String> initialParts = new ArrayList<String>();

		for (int i = 0; i < Fn.length(); i += 2) {

			if (i + 2 < Fn.length()) {
				String lnPart = Fn.substring(i, i + 1);

				for (int j = 0; j < Ln.length(); j++) {
					String fnPart = Ln.substring(j, j + 1);

					String initial = (lnPart + fnPart).toUpperCase();
					initialParts.add(initial);

				}
			}

		}

		for (int i = 1; i < initialParts.size(); i++) {

			String initialFull = initialParts.get(0).toString()
					+ initialParts.get(i).toString();

			if (!initialsList.contains(initialFull)
					&& !userManager.initialsExists(initialFull)) {
				initialsList.add(initialFull);
			}

		}

		return initialsList;

	}

	@Override
	protected Fragment getNextFragment() {
		return new RegistrationFragPersonInfo();

	}

}
