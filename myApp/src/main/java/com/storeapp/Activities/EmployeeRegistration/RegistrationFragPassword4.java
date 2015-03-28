package com.storeapp.Activities.EmployeeRegistration;

import android.os.Bundle;
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

public class RegistrationFragPassword4 extends BaseFragment {
    private View view;
    private ImageButton btnImgContinue;
    private EditText editPassword, editRepeatPassword;
    private long btnLastClickTime = 0;
    EmployeeModel employeeModel;

    public RegistrationFragPassword4() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_registration_password, container,
                false);

        editPassword = (EditText) view.findViewById(R.id.editPassword);
        editRepeatPassword = (EditText) view
                .findViewById(R.id.editRepeatPassword);

        return view;
    }


    @Override
    protected BaseFragment getPrevFragment() {
        return null;
    }

    @Override
    protected BaseFragment getNextFragment() {
        return null;
    }

    @Override
    protected void validate() {

        String message = null;
        int msgResId = 0;

        String password = editPassword.getText().toString();
        String repeatPassword = editRepeatPassword.getText().toString();

        msgResId = msgResId == 0 ? (Utils.isNullOrEmpty(password) ? R.string.error_name_empty : msgResId) : msgResId;
        msgResId = msgResId == 0 ? (!password.equals(repeatPassword) ? R.string.error_name_empty : msgResId) : msgResId;


        if (msgResId == 0) {

            employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
            employeeModel.setPassword(password);
            fireOnValidateFragmentListener(true);

        } else {
            message = getString(msgResId);
            SweetAlerts.showErrorMsg(getActivity(),message);
            fireOnValidateFragmentListener(false);
        }

    }

    @Override
    protected void save() {
        employeeModel.saveInSharedPrefs();
    }
}
