package com.storeapp.Activities.EmployeeRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.storeapp.Activities.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BusinessModel;
import com.storeapp.util.SweetAlerts;

public class RegistrationFragBusinessInfo1 extends BaseFragment {
	private View view;
	private ImageButton btnImgContinue;
	private EditText editBsuinessEmail, editBusinessPhoneNum,editBusinessCvr,editBusinessName;
	private long btnLastClickTime = 0;

    private BusinessModel businessModel ;

	public RegistrationFragBusinessInfo1() {

	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_registration_businessinfo, container,
				false);



        editBsuinessEmail = (EditText) view.findViewById(R.id.editBsuinessEmail);
        editBusinessPhoneNum = (EditText) view.findViewById(R.id.editBusinessPhoneNum);
        editBusinessCvr = (EditText) view.findViewById(R.id.editBusinessCvr);
        editBusinessName = (EditText) view.findViewById(R.id.editBusinessName);

		return view;

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
    protected void validate() {

        String businessName = editBusinessName.getText().toString();
        String email = editBsuinessEmail.getText().toString();
        String businessPhone = editBusinessPhoneNum.getText().toString();
        String cvr = editBusinessCvr.getText().toString();

         businessModel = new BusinessModel(businessName, cvr, email, businessPhone);
        if(!businessModel.isValid()){
            SweetAlerts.showErrorMsg(getActivity(),"All Fields Required" );
            fireOnValidateFragmentListener(false);
        }else {
            fireOnValidateFragmentListener(true);
        }
    }

    @Override
    protected void save() {
     businessModel.saveInSharedPrefs();
    }
}
