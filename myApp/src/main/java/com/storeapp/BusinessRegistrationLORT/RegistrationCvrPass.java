/*
package com.storeapp.BusinessRegistrationLORT;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.storeapp.Activities.BaseFragment;
import com.storeapp.Activities.EmployeeRegistration.RegistrationFragContactInfo2;
import com.storeapp.R;
import com.storeapp.Activities.parse.Business;
import com.storeapp.util.Utils;

public class RegistrationCvrPass extends BaseFragment {
	private View view;

	private EditText editCVR, editPasswordBusiness, editPasswordRepeatBusiness;
	private ImageButton btnRegisterBusiness;
	private long btnLastClickTime = 0;

	public RegistrationCvrPass() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.registration_fragment_business_cvr_password, container,
				false);

		getActivity().getActionBar().hide();

        editCVR = (EditText) view.findViewById(R.id.editCVR);
        editPasswordBusiness = (EditText) view.findViewById(R.id.editPasswordBusiness);
        editPasswordRepeatBusiness = (EditText) view.findViewById(R.id.editPasswordRepeatBusiness);


//		readPhoneOwnerName();
        btnRegisterBusiness = (ImageButton) view
				.findViewById(R.id.btnRegisterBusiness);
        btnRegisterBusiness.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - btnLastClickTime < 1000) {

					return;
				}
				btnLastClickTime = SystemClock.elapsedRealtime();

				String cvr = editCVR.getText().toString();
				String password = editPasswordBusiness.getText().toString();
                String repeatpassword = editPasswordRepeatBusiness.getText().toString();


                if (!Utils.isNullOrEmpty(cvr)
						&& !Utils.isNullOrEmpty(password) && password.equals(repeatpassword)) {

                    Business business = new Business();
                    //business.setName(RegisterPrefs.getName());
                  //  business.setPhoneNumb(RegisterPrefs.getPhoneNumber());
                    business.setCvr(cvr);
                    business.setPassword(password);
                    business.saveInBackground();

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    //Fragment fragment = new RegistrationFragPersonInfo();
                    Fragment fragment = new RegistrationBusinessInfo();

                    transaction.replace(R.id.fragmentsRegistCont, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

				}else{

                    Utils.showToastShort("Passwords Mismatch! or Empty !");
                }



			}
		});

		return view;
	}

	public void readPhoneOwnerName() {
		Cursor cursor = getActivity().getContentResolver().query(
				ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();



		cursor.close();

	}

	


	@Override
	public void onPause() {
		super.onPause();
		
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();


	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

    @Override
    protected BaseFragment getPrevFragment() {
        return null;
    }

    @Override
	protected BaseFragment getNextFragment() {
		return new RegistrationFragContactInfo2();
	}

    @Override
    protected String validate() {
        return null;
    }

    @Override
    protected void save() {

    }

}
*/
