/*
package com.storeapp.BusinessRegistrationLORT;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.storeapp.Activities.BaseFragment;
import com.storeapp.Activities.EmployeeRegistration.RegistrationFragContactInfo2;
import com.storeapp.R;

public class RegistrationBusinessInfo extends BaseFragment {
	private View view;

	private EditText editNameBusiness, editPhoneNumBusiness;
	private ImageButton btnImgContinue;
	private long btnLastClickTime = 0;

	public RegistrationBusinessInfo() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		view = inflater.inflate(R.layout.registration_fragment_business_information, container,
				false);

	//	getActivity().getActionBar().hide();

        editNameBusiness = (EditText) view.findViewById(R.id.editNameBusiness);
        editPhoneNumBusiness = (EditText) view.findViewById(R.id.editPhoneNumBusiness);

        btnImgContinue = (ImageButton) view
				.findViewById(R.id.btnImgContinue);
      */
/*  btnImgContinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - btnLastClickTime < 1000) {

					return;
				}
				btnLastClickTime = SystemClock.elapsedRealtime();

				String name = editNameBusiness.getText().toString();
				String phoneNumber = editPhoneNumBusiness.getText().toString();




					BusinessRegisterPrefs.setName(name);
                    BusinessRegisterPrefs.setPhoneNumber(phoneNumber);

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    Fragment fragment = new RegistrationCvrPass();

                    transaction.replace(R.id.fragmentsRegistCont, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


			}
		});
*//*

		return view;
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
