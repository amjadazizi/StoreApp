package com.storeapp.Login_Registration;

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

import com.storeapp.AppContextProvider;
import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

public class RegistrationFragPersonInfo3 extends BaseFragment {
    private View view;

	private EditText editName, editLastName;
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

		return view;
	}

 public void readPhoneOwnerName() {

     Cursor cursor = AppContextProvider.getContext().getContentResolver().query(
             ContactsContract.Profile.CONTENT_URI, null, null, null, null);
   if( cursor.moveToFirst()){

       String phoneOwnerName = cursor.getString(cursor
               .getColumnIndex("display_name"));
       if (phoneOwnerName != null) {

           String[] OwnerName = phoneOwnerName.split(" ");
           editName.setText(OwnerName[0]);
           editLastName.setText(OwnerName[1]);

       }

   }

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
    public BaseFragment getNextFragment() {
        return new RegistrationFragPassword4();
    }

    @Override
    public void validate() {
        String message = null;
        int msgResId = 0;

        String firstName = editName.getText().toString();
        String lastName = editLastName.getText().toString();

        msgResId= msgResId ==0? (Utils.isNullOrEmpty(firstName) ? R.string.error_name_empty: msgResId) : msgResId;
        msgResId= msgResId ==0? (Utils.isNullOrEmpty(lastName) ? R.string.error_last_name_empty: msgResId) : msgResId;

        if(msgResId ==0){
            employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
            employeeModel.setFirstName(firstName);
            employeeModel.setLastName(lastName);
            employeeModel.saveInSharedPrefs();
            notifyValidated(true);
        }
        else
        {
            message = Utils.getString(msgResId);
            SweetAlerts.showErrorMsg(getActivity(), message);
        }
    }

}
