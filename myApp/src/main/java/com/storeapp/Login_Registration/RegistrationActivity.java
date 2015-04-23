package com.storeapp.Login_Registration;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.storeapp.BaseActivity;
import com.storeapp.BaseFragment;
import com.storeapp.R;
import com.storeapp.model.BaseModel;
import com.storeapp.model.BusinessModel;
import com.storeapp.model.EmployeeModel;
import com.storeapp.parse.Admin;
import com.storeapp.parse.Business;
import com.storeapp.parse.NewUser;
import com.storeapp.parse.User;
import com.storeapp.ui.sweetalert.SweetAlertDialog;
import com.storeapp.util.Prefs;
import com.storeapp.util.Utils;

import java.util.List;


public class RegistrationActivity extends BaseActivity implements BaseFragment.OnValidateFragmentListener {
    FloatingActionButton btnImgContinue;


    @Override
    protected int getActivityTitleResId() {
        return R.id.title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getActionBar().hide();
        int taskType = Prefs.getTaskType();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();

        BaseFragment startFragment = null;
        if (taskType == Login.TASK_BUSINESS) {
            startFragment = new RegistrationFragBusinessInfo1();
        } else {
            startFragment = new RegistrationFragContactInfo2();
        }
        startFragment.setValidateFragmentListener(this);
        transaction.add(R.id.fragmentsRegistCont, startFragment, null);
        //transaction.addToBackStack(null);
        transaction.commit();

         btnImgContinue = (FloatingActionButton) findViewById(R.id.btnImgContinue);
        btnImgContinue.setColorNormalResId(R.color.button_normal);
        btnImgContinue.setColorPressedResId(R.color.button_pressed);
        btnImgContinue.setIcon(R.drawable.ic_arrow_right);

        btnImgContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseFragment curFragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.fragmentsRegistCont);
                if (curFragment == null) {
                    return;
                }

                curFragment.validate();
            }
        });
    }

    

    @Override
    public void validated(boolean validated) {

        if (validated) {
            BaseFragment curFragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.fragmentsRegistCont);
            if (curFragment == null) {
                return;
            }

            BaseFragment nextFragment = curFragment.getNextFragment();
            if (nextFragment != null) {
                nextFragment.setValidateFragmentListener(this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_out_to_right,R.anim.slide_in_from_left);
                ft.replace(R.id.fragmentsRegistCont, nextFragment, null);
                ft.addToBackStack(null);
                ft.commit();
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

        final int taskType = Prefs.getTaskType();
        EmployeeModel employeeModel = BaseModel.getFromSharedPrefs(EmployeeModel.class);
        Admin admin = new Admin();

        if (taskType == Login.TASK_BUSINESS) {
            BusinessModel businessModel = BaseModel.getFromSharedPrefs(BusinessModel.class);
            Business business = Business.fromModelData(businessModel);
            admin.setAdminEmail(employeeModel.getEmail());
            admin.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Utils.showToastShort("Admin Saved");
                    }
                }
            });
            cvr = businessModel.getCvr();

        } else {
            cvr = Prefs.getBusinessCvr();
        }

        User user = new User();
        user.setUsername(employeeModel.getEmail());
        user.setPassword(employeeModel.getPassword());
        user.setEmail(employeeModel.getEmail());

        user.setFirstName(employeeModel.getFirstName());
        user.setLastName(employeeModel.getLastName());
        user.setBusinessCvr(cvr);
        user.setPhoneNumber(employeeModel.getPhoneNumber());
        try {

            user.signUp();

            if (taskType == Login.TASK_EMPLOYEE) {

                ParseQuery<NewUser> query = NewUser.getQuery();
                query.whereEqualTo("email", employeeModel.getEmail());
                query.findInBackground(new FindCallback<NewUser>() {
                    @Override
                    public void done(List<NewUser> newUser, ParseException e) {
                        if (e == null) {
                            if (newUser != null && newUser.size() > 0) {

                                newUser.get(0).deleteInBackground();
                                pDialog.dismiss();

                                new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText("Welcome! You have Beeen Registered")
                                        .setConfirmText("Ok")

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                finish();
                                            }
                                        })
                                        .show();

                            }
                        }
                        finish();
                    }
                });
            }

            if (taskType == Login.TASK_BUSINESS) {

                Business.fromModelData(BusinessModel.getFromSharedPrefs(BusinessModel.class)).saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            pDialog.dismiss();

                            new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Good job!")
                                    .setContentText("Welcome! Your Business Registered.")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    }
                });

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}