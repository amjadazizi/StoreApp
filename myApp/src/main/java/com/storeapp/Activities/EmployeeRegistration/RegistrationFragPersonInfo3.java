package com.storeapp.Activities.EmployeeRegistration;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.storeapp.Activities.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

public class RegistrationFragPersonInfo3 extends BaseFragment {
	private View view;

	private EditText editName, editLastName;
	private ImageButton btnImgContinue;
	private long btnLastClickTime = 0;
    EmployeeModel employeeModel;
	public RegistrationFragPersonInfo3() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_registration_personinfo, container,
				false);

		getActivity().getActionBar().hide();

		editName = (EditText) view.findViewById(R.id.txtName);
		editLastName = (EditText) view.findViewById(R.id.txtLastName);

//		readPhoneOwnerName();
    /*    btnImgContinue = (ImageButton) view
				.findViewById(R.id.btnImgContinue );
        btnImgContinue.setOnClickListener(new View.OnClickListener() {

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

					//RegisterPrefsOLD.setFirstName(name);
					//RegisterPrefsOLD.setLastName(lastName);
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    Fragment fragment = new RegistrationFragContactInfo2();

                    transaction.replace(R.id.fragmentsRegistCont, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    Utils.showToastShort("Okok !");
				}


			}
		});*/

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
		
		//getActivity().unregisterReceiver(br);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
		

		/*IntentFilter intentFilter = new IntentFilter(
				RegistrationFragChooseInitials.ACTION_REGISTRATION_DONE);
		getActivity().registerReceiver(br, intentFilter);*/

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}


    @Override
    protected BaseFragment getPrevFragment() {
        return new RegistrationFragContactInfo2();
    }

    @Override
    protected BaseFragment getNextFragment() {
        return new RegistrationFragPassword4();
    }

    @Override
    protected void validate() {
        String message = null;
        int msgResId = 0;

        String firstName = editName.getText().toString();
        String lastName = editLastName.getText().toString();

        msgResId= msgResId ==0? (Utils.isNullOrEmpty(firstName) ? R.string.error_name_empty: msgResId) : msgResId;
        msgResId= msgResId ==0? (Utils.isNullOrEmpty(lastName) ? R.string.error_name_empty: msgResId) : msgResId;

        if(msgResId ==0){
            employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
            employeeModel.setFirstName(firstName);
            employeeModel.setLastName(lastName);
            fireOnValidateFragmentListener(true);
        }
        else
        {
            message = getString(msgResId);
            fireOnValidateFragmentListener(false);
            SweetAlerts.showErrorMsg(getActivity(), message);
        }
    }



    @Override
    protected void save() {
        employeeModel.saveInSharedPrefs();
    }
}
