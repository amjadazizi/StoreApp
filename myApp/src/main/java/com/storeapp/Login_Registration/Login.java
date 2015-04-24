package com.storeapp.Login_Registration;

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
import com.storeapp.BaseActivity;
import com.storeapp.MainMenu.LeftMenu.Choose_Task;
import com.storeapp.R;
import com.storeapp.parse.Admin;
import com.storeapp.parse.User;
import com.storeapp.ui.CustomDialog.ProgressGenerator;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.model.Notice;

public class Login extends BaseActivity implements ProgressGenerator.OnCompleteListener, OnClickListener {

    private static final String KEY_ANIMATE_LOGO = "key_animate_logo";
    private static final String KEY_SLIDING_DRAWER_OPEN = "sliding_drawer_open";
    private static final String KEY_CURRENT_FRAGMENT_INDEX = "current_fragment_index";

    private TextView imgviewStoreApp;
    private Animation animTranslate;

    private EditText liInitials, liPassword;

    private boolean animationDone = false;
    private boolean isSlidDraerOpen = false;
    private int currentFragIndex;
    public static final int TASK_BUSINESS = 1;
    public static final int TASK_EMPLOYEE = 2;


    final Notice LICENSES_DIALOG_NOTICE = new Notice("StoreApp ", "DET_HER_SKAL_VÆRE_EM_URL",
            "BESKRIV EN LICENSEAFTALE! BESKRIV EN LICENSEAFTALE! BESKRIV EN LICENSEAFTALE! BESKRIV EN LICENSEAFTALE!", null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getActionBar().hide();
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


                Utils.showToastShort("DET ER EMPLOYEE REGIS");


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

                Utils.showToastShort("DET ER BUSINESS REGIST");

            }
        });


        final FloatingActionButton btnInfo = (FloatingActionButton) findViewById(R.id.btnFloatInfo);
        btnInfo.setColorNormalResId(R.color.button_normal);
        btnInfo.setColorPressedResId(R.color.button_pressed);
        btnInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new LicensesDialog.Builder(Login.this).setNotices(LICENSES_DIALOG_NOTICE).setThemeResourceId(R.style.custom_theme)
                        .setDividerColorId(R.color.custom_divider_color).setTitle("Information").build().show();

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANIMATE_LOGO, animationDone);
        outState.putBoolean(KEY_SLIDING_DRAWER_OPEN, isSlidDraerOpen);

        currentFragIndex = getFragmentManager().getBackStackEntryCount();
        outState.putInt(KEY_CURRENT_FRAGMENT_INDEX, currentFragIndex);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        animationDone = savedInstanceState.getBoolean(KEY_ANIMATE_LOGO);
        currentFragIndex = savedInstanceState
                .getInt(KEY_CURRENT_FRAGMENT_INDEX);

    }

    @Override
    protected int getActivityTitleResId() {
        return R.string.title_activity_login;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onComplete() {

    }
}