package com.storeapp.Login_Registration;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseQuery;
import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.parse.NewUser;
import com.storeapp.ui.sweetalert.SweetAlertDialog;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
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
    public BaseFragment getNextFragment() {
        return new RegistrationFragPersonInfo3();
    }

    int msgResId = 0;
    String email;
    String phoneNumber;
    //String message = null;
    NewUser newUser = null;

    @Override
    public void validate() {

        email = editEmail.getText().toString();
        phoneNumber = editPhoneNum.getText().toString();

        msgResId = 0;
        msgResId = msgResId == 0 ? (Utils.isNullOrEmpty(email) ? R.string.error_email_empty : msgResId) : msgResId;
        boolean isEmailValid =  Utils.isEmailFormatValid(email); //Patterns.EMAIL_ADDRESS.matcher(email).matches();
        msgResId = msgResId == 0 ? (!isEmailValid ? R.string.error_email_invalid : msgResId) : msgResId;
        msgResId = msgResId == 0 ? (Utils.isNullOrEmpty(phoneNumber) ? R.string.error_phone_number_empty : msgResId) : msgResId;

        if (msgResId > 0) {
            String message = Utils.getString(msgResId);
            SweetAlerts.showErrorMsg(getActivity(), message);
            return;
        }

        int taskType = Prefs.getTaskType();
        VerifyUserEmailTask task = new VerifyUserEmailTask(getActivity(), taskType, email);
        task.execute();

    }

    private class VerifyUserEmailTask extends AsyncTask<String, Void, NewUser> {
        private int taskType;
        private String email;
        // private ProgressDialog pd;


        public VerifyUserEmailTask(Context context, int taskType, String email) {
            this.taskType = taskType;
            this.email = email;
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected NewUser doInBackground(String... strings) {

            NewUser newUser = null;

            if (taskType == Login.TASK_EMPLOYEE) {
                ParseQuery<NewUser> query = NewUser.getQuery();
                query.whereEqualTo("email", email);
                try {
                    List<NewUser> newUsers = query.find();
                    if (newUsers != null && newUsers.size() > 0) {
                        newUser = newUsers.get(0);
                    }
                } catch (Exception e) {
                    Utils.showToastLong(e.getMessage());
                }
            }
            return newUser;
        }

        @Override
        protected void onPostExecute(NewUser newUserFromParse) {
            pDialog.dismiss();

            newUser = newUserFromParse;
            employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
            employeeModel.setEmail(email);
            employeeModel.setPhoneNumber(phoneNumber);
            employeeModel.saveInSharedPrefs();

            if(taskType == Login.TASK_BUSINESS){
                notifyValidated(true);
            }
            else if (taskType == Login.TASK_EMPLOYEE && newUser != null) {
                Prefs.setBusinessCvr(newUser.getCvr());
                notifyValidated(true);
            } else {
                msgResId = R.string.error_passwords_mismatch;
                Utils.showToastLong(Utils.getString(msgResId));
            }

        }
    }
}
