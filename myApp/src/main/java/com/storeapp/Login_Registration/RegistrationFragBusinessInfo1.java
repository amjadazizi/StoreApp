package com.storeapp.Login_Registration;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BusinessModel;
import com.storeapp.parse.Business;
import com.storeapp.ui.sweetalert.SweetAlertDialog;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;

public class RegistrationFragBusinessInfo1 extends BaseFragment {
    private View view;
    private ImageButton btnImgContinue;
    private EditText editBsuinessEmail, editBusinessPhoneNum, editBusinessCvr, editBusinessName;
    SweetAlertDialog pDialog;


    private BusinessModel businessModel;

    public RegistrationFragBusinessInfo1() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_registration_businessinfo, container,
                false);

        editBsuinessEmail = (EditText) view.findViewById(R.id.editBsuinessEmail);
        editBsuinessEmail.setText("jajaja@hahah.xom");
        editBusinessPhoneNum = (EditText) view.findViewById(R.id.editBusinessPhoneNum);
        editBusinessPhoneNum.setText("7337378");
        editBusinessCvr = (EditText) view.findViewById(R.id.editBusinessCvr);
        editBusinessCvr.setText("828289");
        editBusinessName = (EditText) view.findViewById(R.id.editBusinessName);
        editBusinessName.setText("ajjaja");

        return view;

    }

    @Override
    public BaseFragment getNextFragment() {
        return new RegistrationFragContactInfo2();
    }

    @Override
    public void validate() {

        String businessName = editBusinessName.getText().toString();
        String email = editBsuinessEmail.getText().toString();
        if (!Utils.isEmailFormatValid(email)) {
            SweetAlerts.showErrorMsg(getActivity(), "Invalid Email");
            return;
        }

        String businessPhone = editBusinessPhoneNum.getText().toString();
        String cvr = editBusinessCvr.getText().toString();
        businessModel = new BusinessModel(businessName, cvr, email, businessPhone);

        if (!businessModel.isValid()) {
            SweetAlerts.showErrorMsg(getActivity(), "All Fields Required");
            return;
        }

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ParseQuery<Business> businessParseQuery = ParseQuery.getQuery(Business.class);
        businessParseQuery.whereEqualTo(Business.BUSINESS_CVR, cvr);
        businessParseQuery.findInBackground(new FindCallback<Business>() {
            @Override
            public void done(List<Business> businesses, ParseException e) {
                if (e == null) {
                    if (businesses != null && businesses.size() > 0) {
                        pDialog.dismiss();
                        SweetAlerts.showErrorMsg(getActivity(), "This Business Already Exists!");
                    } else {
                        pDialog.dismiss();
                        businessModel.saveInSharedPrefs();
                        notifyValidated(true);
                    }
                }
            }
        });


    }


}
