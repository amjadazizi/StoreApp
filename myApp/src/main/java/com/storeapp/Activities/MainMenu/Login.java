package com.storeapp.Activities.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.storeapp.Activities.BaseActivity;
import com.storeapp.Activities.RegistrationActivityFragCont;
import com.storeapp.Activities.parse.User;
import com.storeapp.Imported.CustomDialog.CustomDialog.ProgressGenerator;
import com.storeapp.R;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.model.Notice;

public class Login extends BaseActivity implements ProgressGenerator.OnCompleteListener, OnClickListener {

    private static final String KEY_ANIMATE_LOGO = "key_animate_logo";
    private static final String KEY_SLIDING_DRAWER_OPEN = "sliding_drawer_open";
    private static final String KEY_CURRENT_FRAGMENT_INDEX = "current_fragment_index";

    private Button slideButton;
    private SlidingDrawer slidingDrawer;
    private ImageView imgviewStoreApp;
    private TextView txtSignUp;

    private Animation animTranslate;
    private EditText liInitials, liPassword;

    private boolean animationDone = false;
    private boolean isSlidDraerOpen = false;
    private int currentFragIndex;
    public static final String EXTRA_TASK = "ekstra_task";

    public static final int TASK_BUSINESS = 1;
    public static final int TASK_EMPLOYEE = 2;


    final Notice LICENSES_DIALOG_NOTICE = new Notice("StoreApp ", "DET_HER_SKAL_VÆRE_EM_URL",
            "BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES " +
                    "BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV " +
                    "DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN " +
                    "LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES \" +\n" +
                    "                    \"BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV \" +\n" +
                    "                    \"DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN \" +\n" +
                    "                    \"LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES \" +\n" +
                    "                    \"BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV \" +\n" +
                    "                    \"DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN \" +\n" +
                    "                    \"LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES \" +\n" +
                    "                    \"BESKRIV DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV \" +\n" +
                    "                    \"DIN LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES BESKRIV DIN \" +\n" +
                    "                    \"LICENSEAFTALE! DET ER EN BARE EN TEST SOM LIGENU ER TOM GRUNDET MANGLENDE LICENSES", null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

    //public void readPhoneOwnerName() {
    /*    Cursor cursor = getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI, null, null, null, null);

        if(cursor.moveToFirst()) {

            String phoneOwnerName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
            String ownerEmail = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

            if (phoneOwnerName != null) {

                String[] OwnerName = phoneOwnerName.split(" ");
                Utils.showToastLong("Email: " + ownerEmail + "       PhoneNumber  " + phoneNumber + "            PhoneOwnerNanem" + phoneOwnerName);

            }
        }

        cursor.close();*/

    //}

		/*if (Prefs.isUserLoggedIn()) {
            Intent intent = new Intent(Login.this, Choose_Task.class);
			startActivity(intent);
			finish();
		}
*/
        // final String name = "LicensesDialog";
        // final String url = "http://psdev.de";
        // final String copyright = "Copyright 2013 Philip Schiffer <admin@psdev.de>";
        // final License license = new ApacheSoftwareLicense20();
        // final Notice notice = new Notice(name, url, copyright, license);


        // do not do sound effect for this activity
        doSoundeffect = false;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (savedInstanceState == null) {
            //	FragmentTransaction transaction = getFragmentManager()
            //			.beginTransaction();
            //Fragment fragment = new RegistrationFragPersonInfo();
            //  Fragment fragment = new RegistrationBusinessInfo();

            //transaction.add(R.id.fragConLay, fragment);
            // transaction.addToBackStack(null);
            //transaction.commit();
        }

        // get fragment count after the first one is added
        currentFragIndex = getFragmentManager().getBackStackEntryCount();
        if (savedInstanceState != null) {
            animationDone = savedInstanceState.getBoolean(KEY_ANIMATE_LOGO);
            isSlidDraerOpen = savedInstanceState
                    .getBoolean(KEY_SLIDING_DRAWER_OPEN);
            currentFragIndex = savedInstanceState
                    .getInt(KEY_CURRENT_FRAGMENT_INDEX);

        }
        final LinearLayout loginBox = (LinearLayout) findViewById(R.id.LoginBox);
        loginBox.setVisibility(View.VISIBLE);

        liInitials = (EditText) findViewById(R.id.liInitials);
        liPassword = (EditText) findViewById(R.id.liPassword);

        getActionBar().hide();


     /*   if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            liLoginBtn.setMode(ActionProcessButton.Mode.ENDLESS);

        }
        } else {
            btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(btnSignIn);
                btnSignIn.setEnabled(false);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);
            }*/

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
                                // liLoginBtn.setEnabled(false);

                                String cvr = parseUser.getString(User.COL_BUSINESS_CVR);
                                Prefs.setBusinessCvr(cvr);
                                Prefs.setLogedInUser(liInitials.getText().toString());



                                // Hooray! The user is logged in.
                                Intent i = new Intent(Login.this, Choose_Task.class);
                                startActivity(i);

                            } else {
                                SweetAlerts.showErrorMsg(Login.this, "Wrong Credentials!");
                                return;

                            }
                        } else {
                        }

                    }
                });
                //Intent i = new Intent(Login.this, Choose_Task.class);
                //  startActivity(i);


            }
        });


        imgviewStoreApp = (ImageView) findViewById(R.id.imgviewStoreApp);
        animTranslate = AnimationUtils.loadAnimation(Login.this,
                R.anim.translate);
        animTranslate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                loginBox.setVisibility(View.VISIBLE);
                //slideButton.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(Login.this,
                        R.anim.fade);
                loginBox.startAnimation(animFade);

                animationDone = true;

				/*if (isSlidDraerOpen) {
					slidingDrawer.animateOpen();
				}*/
            }

        });

        if (animationDone) {
            animTranslate.setDuration(0);
        }

        imgviewStoreApp.startAnimation(animTranslate);

		/*IntentFilter intentFilter = new IntentFilter(
				RegistrationFragChooseInitials.ACTION_REGISTRATION_DONE);
		registerReceiver(br, intentFilter);

*/

////////////////////////////////////////////////////////////////////////////

        //   final View actionB = findViewById(R.id.action_b);

        FloatingActionButton registerEmployee = new FloatingActionButton(getBaseContext());
        registerEmployee.setIcon(R.drawable.ic_employee_add);
        registerEmployee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Prefs.setTaskType(TASK_EMPLOYEE);
                Intent i = new Intent(Login.this, RegistrationActivityFragCont.class);
                startActivity(i);


                Utils.showToastShort("DET ER EMPLOYEE REGIS");


            }
        });
        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(registerEmployee);


        final FloatingActionButton regiterBusiness = (FloatingActionButton) findViewById(R.id.btnFloatBusinessRegist);


        registerEmployee.setIcon(R.drawable.ic_shop);

        regiterBusiness.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.setTaskType(TASK_BUSINESS);

                Intent i = new Intent(Login.this, RegistrationActivityFragCont.class);
                startActivity(i);

                Utils.showToastShort("DET ER BUSINESS REGIST");

            }
        });


        final FloatingActionButton btnInfo = (FloatingActionButton) findViewById(R.id.btnFloatInfo);
        btnInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new LicensesDialog.Builder(Login.this).setNotices(LICENSES_DIALOG_NOTICE).setThemeResourceId(R.style.custom_theme)
                        .setDividerColorId(R.color.custom_divider_color).setTitle("Information").build().show();

            }
        });

    }


    public void resetPassword() {
        String email = liInitials.getText().toString();
        if(Utils.isNullOrEmpty(email)){
            SweetAlerts.showErrorMsg(this, "Enter Your Email.");
            return;
        }

         ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {

                        if (e == null) {

                            SweetAlerts.showSuccessMsg(Login.this,"An email was successfully sent with Instructions");

                        } else {
                            // Something went wrong. Look at the ParseException
                            // to see what's up.
                           // Toast.makeText(getApplicationContext(), getResources().getString(R.string.reset_password_fail), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    @Override
    public void onComplete() {


    }


    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else if (getFragmentManager().getBackStackEntryCount() <= 0) {
            //if (slidingDrawer.isOpened()) {
            //	slidingDrawer.animateClose();

            //} else {
            super.onBackPressed();
            //}

        }
    }

    @Override
    public void onClick(View v) {

    }

	/*private BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			slidingDrawer.animateClose();

		}
	};*/

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregisterReceiver(br);
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
        //isSlidDraerOpen = savedInstanceState
        //		.getBoolean(KEY_SLIDING_DRAWER_OPEN);
        animationDone = savedInstanceState.getBoolean(KEY_ANIMATE_LOGO);
        currentFragIndex = savedInstanceState
                .getInt(KEY_CURRENT_FRAGMENT_INDEX);

    }

    @Override
    protected int getActivityTitleResId() {
        return R.string.title_activity_login;
    }


}