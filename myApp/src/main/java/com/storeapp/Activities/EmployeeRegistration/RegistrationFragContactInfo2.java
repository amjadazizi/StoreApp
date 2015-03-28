package com.storeapp.Activities.EmployeeRegistration;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Activities.BaseFragment;
import com.storeapp.Activities.MainMenu.Login;
import com.storeapp.Activities.parse.NewUser;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.sweetalert.SweetAlertDialog;
import com.storeapp.util.Prefs;
import com.storeapp.util.Utils;

import java.util.List;

public class RegistrationFragContactInfo2 extends BaseFragment {
    private View view;
    private ImageButton btnImgContinue;
    private EditText editEmail, editPhoneNum;
    private long btnLastClickTime = 0;
    EmployeeModel employeeModel;
     SweetAlertDialog pDialog;

    public RegistrationFragContactInfo2() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_registration_contactinfo, container, false);


        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editPhoneNum = (EditText) view.findViewById(R.id.editPhoneNum);

        return view;

    }


/*    private void verifyRegistrationEmail(String email){
        ParseQuery<NewUser> query = new ParseQuery(NewUser.class);
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<NewUser>() {
            @Override
            public void done(List<NewUser> newUsers, ParseException e) {
                if(e==null){
                    if(newUsers.size()>0){

                    }
                }
            }
        });



    }*/


    @Override
    protected BaseFragment getPrevFragment() {


        return null;
    }

    @Override
    protected BaseFragment getNextFragment() {
        return new RegistrationFragPersonInfo3();
    }

    int msgResId = 0;
    String email;
    String phoneNumber;
    //String message = null;
    NewUser newUser = null;

    @Override
    protected void validate() {


        email = editEmail.getText().toString();
        phoneNumber = editPhoneNum.getText().toString();

        msgResId=0;
        msgResId = msgResId == 0 ? (Utils.isNullOrEmpty(email) ? R.string.error_name_empty : msgResId) : msgResId;
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        msgResId = msgResId == 0 ? (!isEmailValid ? R.string.error_name_empty : msgResId) : msgResId;
        msgResId = msgResId == 0 ? (Utils.isNullOrEmpty(phoneNumber) ? R.string.error_name_empty : msgResId) : msgResId;
        // phoneNumberValue = Integer.parseInt(phoneNumber);

        if (msgResId > 0) {
           String message = getString(msgResId);
            Utils.showToastLong(message);
            fireOnValidateFragmentListener(false);
        }

        int taskType = Prefs.getTaskType();
        if (taskType == Login.TASK_EMPLOYEE) {
            VerifyUserEmailTask task = new VerifyUserEmailTask(getActivity(), email);
            task.execute();
        }
    }

    @Override
    protected void save() {
        employeeModel.saveInSharedPrefs();
    }

    private class VerifyUserEmailTask extends AsyncTask<String, Void, NewUser> {

        private String email;
       // private ProgressDialog pd;



        public VerifyUserEmailTask(Context context, String email) {
            this.email = email;

            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
        /*    pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setMessage("Verifying email...");*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
            //pd.show();
        }

        @Override
        protected NewUser doInBackground(String... strings) {

            NewUser newUser = null;

            ParseQuery<NewUser> query = NewUser.getQuery();
            query.whereEqualTo("email", email);
            try {
                List<NewUser> newUsers = query.find();
                if (newUsers != null && newUsers.size() > 0) {
                    newUser = newUsers.get(0);
                }

            } catch (ParseException e) {

            }

            return newUser;
        }

        @Override
        protected void onPostExecute(NewUser newUserFromParse) {
            pDialog.dismiss();

            newUser = newUserFromParse;
            if (newUser != null) {
                Prefs.setBusinessCvr(newUser.getCvr());
                employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
                employeeModel.setEmail(email);
                employeeModel.setPhoneNumber(phoneNumber);
                fireOnValidateFragmentListener(true);
            } else {
                msgResId = R.string.error_passwords_mismatch;
                Utils.showToastLong(getString(msgResId));
                fireOnValidateFragmentListener(false);
            }

        }
    }
}
