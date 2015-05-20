package com.storeapp.Login_Registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.storeapp.MainMenu.LeftMenu.Choose_Task;
import com.storeapp.R;
import com.storeapp.parse.Admin;
import com.storeapp.parse.User;
import com.storeapp.ui.CustomDialog.ProgressGenerator;
import com.storeapp.ui.sweetalert.SweetAlertDialog;
import com.storeapp.util.ConnectionUtil;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.model.Notice;

public class Login extends Activity implements ProgressGenerator.OnCompleteListener {

    private TextView imgviewStoreApp;
    private Animation animTranslate;

    private EditText liInitials, liPassword;

    private boolean animationDone = false;
    public static final int TASK_BUSINESS = 1;
    public static final int TASK_EMPLOYEE = 2;
  String a = "This app is for those who have a small business where they would like to have a mobile cash register" +
        " and have better overview of stock and items in store. The app contains a cash register, a store items function," +
        " a list of all employees and a statistics function where you can see a statistics of products and turnover. " +
        "There is also a profile where users can view and modify their information or change the profile picture." +
        " The app is open source. Source code is available to anyone who would like to use parts of it or develop it" +
        " (See the above link to Github). The app uses some 3rd party open source libraries. " +
        "Android Advanced semester project developed by: Amjad Azizi s127817  Fahima Azizi s081961";

    final Notice LICENSES_DIALOG_NOTICE = new Notice("StoreApp ", "https://github.com/amjadazizi/StoreApp",a, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (Prefs.isUserLoggedIn()) {
            Intent intent = new Intent(Login.this, Choose_Task.class);
            startActivity(intent);
            finish();
        }

        final LinearLayout loginBox = (LinearLayout) findViewById(R.id.LoginBox);
        liInitials = (EditText) findViewById(R.id.liInitials);
        liPassword = (EditText) findViewById(R.id.liPassword);


        TextView txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        final ProgressGenerator progressGenerator = new ProgressGenerator(this);

        final ActionProcessButton liLoginBtn = (ActionProcessButton) findViewById(R.id.liLoginBtnmm);
        liLoginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
          progressGenerator.start(liLoginBtn);

                liLoginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
                final String email = liInitials.getText().toString();
                String password = liPassword.getText().toString();

                String message = Utils.isNullOrEmpty(email) ? "Email Is Required" :
                       Utils.isNullOrEmpty(password) ? "Password is Required" : null;


                if (message != null) {

                    SweetAlerts.showErrorMsg(Login.this, message);
                    return;
                }


                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {

                        if (e == null) {
                            if (parseUser != null) {
                                String cvr = parseUser.getString(User.COL_BUSINESS_CVR);
                                Prefs.setBusinessCvr(cvr);
                                Prefs.setUserLoggedIn(true);
                                Prefs.setLogedInUser(liInitials.getText().toString());
                                checkIfAdmin();
                               progressGenerator.stop();

                            } else {
                                progressGenerator.stop();

                                SweetAlerts.showErrorMsg(Login.this, "Wrong Credentials!");
                                return;

                            }
                        } else {
                            liLoginBtn.setBackgroundColor(getResources().getColor(R.color.button_normal));
                            progressGenerator.stop();
                            SweetAlerts.showErrorMsg(Login.this, "Try Again!!");
                            return;
                        }

                    }
                });
              }
        });


        imgviewStoreApp = (TextView) findViewById(R.id.imgviewStoreApp);
        animTranslate = AnimationUtils.loadAnimation(Login.this,
               R.anim.translate); // Add Translate Animation
        animTranslate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                RelativeLayout storeLooAnimation = (RelativeLayout) findViewById(R.id.storeLooAnimation);
                //storeLooAnimation.setVisibility(View.GONE);

                loginBox.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(Login.this,
                        R.anim.fade);
                loginBox.startAnimation(animFade);
                animationDone = true;


                if(ConnectionUtil.getConnectivityStatusInt(Login.this)==0){

                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Warning!")
                        .setContentText("No Internet Access. It is Required!")
                        .setConfirmText("Try Again")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                            }
                        })
                        .show();
            }
            }

        });

        if (animationDone) {
            animTranslate.setDuration(0);
        }

        imgviewStoreApp.startAnimation(animTranslate);



        final FloatingActionsMenu multiple_actions =(FloatingActionsMenu) findViewById(R.id.multiple_actions);

        final FloatingActionButton btnFloatEmployeeRegis = (FloatingActionButton) findViewById(R.id.btnFloatEmployeeRegis);
        btnFloatEmployeeRegis.setColorNormalResId(R.color.button_normal);
        btnFloatEmployeeRegis.setColorPressedResId(R.color.button_pressed);
        btnFloatEmployeeRegis.setIcon(R.drawable.ic_add_employee_white2);

        btnFloatEmployeeRegis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Prefs.setTaskType(TASK_EMPLOYEE);
                Intent i = new Intent(Login.this, RegistrationActivity.class);
                startActivity(i);
                int pixel=getWindowManager().getDefaultDisplay().getWidth();
                int dp = pixel/(int)getResources().getDisplayMetrics().density;
                Utils.showToastShort(dp+"");


            }
        });


        final FloatingActionButton regiterBusiness = (FloatingActionButton) findViewById(R.id.btnFloatBusinessRegist);
        regiterBusiness.setColorNormalResId(R.color.button_normal);
        regiterBusiness.setColorPressedResId(R.color.button_pressed);
        regiterBusiness.setIcon(R.drawable.ic_add_business);


        regiterBusiness.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.setTaskType(TASK_BUSINESS);

                Intent i = new Intent(Login.this, RegistrationActivity.class);
                startActivity(i);

            }
        });


        final FloatingActionButton btnInfo = (FloatingActionButton) findViewById(R.id.btnFloatInfo);
        btnInfo.setColorNormalResId(R.color.button_normal);
        btnInfo.setColorPressedResId(R.color.button_pressed);
        btnInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new LicensesDialog.Builder(Login.this).setNotices(LICENSES_DIALOG_NOTICE).setThemeResourceId(R.style.custom_theme)
                        .setDividerColorId(R.color.custom_divider_color).setTitle("StoreApp Info").build().show();

            }
        });

    }

    private void checkIfAdmin(){

        ParseQuery<Admin> adminParseQuery = Admin.getQuery(liInitials.getText().toString());
        adminParseQuery.findInBackground(new FindCallback<Admin>() {
            @Override
            public void done(List<Admin> admins, ParseException e) {
                if(e==null){
                    if(admins!=null && admins.size()>0){
                        Prefs.setUserAsAdmin(true);
                    }else {
                        Prefs.setUserAsAdmin(false);
                    }
                    Intent i = new Intent(Login.this, Choose_Task.class);
                    startActivity(i);
                }
            }
        });

    }

    public void resetPassword() {
        String email = liInitials.getText().toString();
        if(Utils.isNullOrEmpty(email)){
            SweetAlerts.showErrorMsg(Login.this, "Enter Your Email.");
            return;
        }

         ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {

                        if (e == null) {

                            SweetAlerts.showSuccessMsg(Login.this, "An email was successfully sent with Instructions");

                        } else {


                         }
                    }
                }
        );
    }




    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else if (getFragmentManager().getBackStackEntryCount() <= 0) {
            super.onBackPressed();
        }
    }



    @Override
    public void onComplete() {

    }
}