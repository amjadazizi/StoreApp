package com.storeapp.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Activities.EmployeeRegistration.RegistrationFragBusinessInfo1;
import com.storeapp.Activities.EmployeeRegistration.RegistrationFragContactInfo2;
import com.storeapp.Activities.MainMenu.Login;
import com.storeapp.Activities.parse.Business;
import com.storeapp.Activities.parse.NewUser;
import com.storeapp.Activities.parse.User;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.BusinessModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.sweetalert.SweetAlertDialog;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;

import java.util.List;


public class RegistrationActivityFragCont extends BaseActivity implements OnValidateFragmentListener {

    BaseFragment currentFragment;

    @Override
    protected int getActivityTitleResId() {
        return R.id.title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getActionBar().hide();


        int task = Prefs.getTaskType();

        currentFragment = (task == Login.TASK_BUSINESS ? new RegistrationFragBusinessInfo1() : new RegistrationFragContactInfo2());
        currentFragment.setValidateFragmentListener(this);
        currentFragment.setCurrentFragment(getFragmentManager(), false);

        FloatingActionButton btnImgContinue = (FloatingActionButton) findViewById(R.id.btnImgContinue);
        btnImgContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentFragment == null) {
                    return;
                }
                currentFragment.validate();

            }
        });
    }

    @Override
    public void validateFragment(boolean validated) {
        if (validated) {
            currentFragment.save();
            BaseFragment nextFragment = currentFragment.getNextFragment();
            if (nextFragment != null) {
                currentFragment.removeValidateFragmentListener();
                nextFragment.setValidateFragmentListener(this);
                nextFragment.setCurrentFragment(getFragmentManager());
                currentFragment = nextFragment;
            } else {
                register();
            }
        }
    }


    private void register() {

       final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        String cvr = null;
       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        progressDialog.show();*/

        int taskType = Prefs.getTaskType();
        EmployeeModel employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);

        if (taskType == Login.TASK_BUSINESS) {

            BusinessModel businessModel = BaseModel.getFromSharedPrefs(BusinessModel.class);
            Business business = Business.fromModelData(businessModel);
            try {
                business.save();
            } catch (ParseException e) {
            }

            cvr = businessModel.getCvr();

        } else {
            cvr = Prefs.getBusinessCvr();
        }

        User user = new User();
        user.setUsername(employeeModel.getEmail());
        user.setPassword(employeeModel.getPassword());
        user.setEmail(employeeModel.getEmail());

        user.setFirstName( employeeModel.getFirstName());
        user.setLastName( employeeModel.getLastName());
        user.setBusinessCvr(cvr);
        user.setPhoneNumber( employeeModel.getPhoneNumber());
        try {
            user.signUp();

            ParseQuery<NewUser> query = NewUser.getQuery();
            query.whereEqualTo("email", employeeModel.getEmail());
            query.findInBackground(new FindCallback<NewUser>() {
                @Override
                public void done(List<NewUser> newUser, ParseException e) {
                    if (e == null) {
                        if (newUser != null && newUser.size() > 0) {

                            newUser.get(0).deleteInBackground();

                            pDialog.dismiss();
                            //progressDialog.dismiss();
                            SweetAlerts.showSuccessMsg(RegistrationActivityFragCont.this, "You have Beeen Registered!");


                        }
                    }
                    finish();
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}