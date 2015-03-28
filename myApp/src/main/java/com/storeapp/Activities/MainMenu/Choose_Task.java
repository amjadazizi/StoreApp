package com.storeapp.Activities.MainMenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.storeapp.Activities.ExternalClasses.FloatingEditText;
import com.storeapp.Activities.parse.NewUser;
import com.storeapp.Imported.CustomDialog.CustomDialog.BaseDialog;
import com.storeapp.Imported.CustomDialog.CustomDialog.CustomDialog;
import com.storeapp.R;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;

public class Choose_Task extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private Choose_Task mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemLogOut;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemAddNewEmployee;
    FloatingEditText newUserEmail;
    static FrameLayout topBar;

    String msg = null;
    int msgColor = Color.RED;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_task_dashboard);
        mContext = this;
        setUpMenu();
        changeFragment(new HomeFragment());
        getActionBar().hide();
        topBar = (FrameLayout) findViewById(R.id.topBar);


    }

    public  static void setTopBarGone(){

        topBar.setVisibility(View.GONE);

    }


    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //set background of menu
        resideMenu.setBackground(R.color.clouds);

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        String a = "Home";

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.ic_action,"Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.ic_action,  "Profile");
        itemAddNewEmployee = new ResideMenuItem(this, R.drawable.ic_employee, "Add New Employee");
        itemSettings = new ResideMenuItem(this, R.drawable.ic_settings, "Settings");
        itemLogOut = new ResideMenuItem(this, R.drawable.ic_log_out, "Log Out");




        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemAddNewEmployee.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemLogOut.setOnClickListener(this);


        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAddNewEmployee, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogOut, ResideMenu.DIRECTION_LEFT);


        // You can disable a direction by setting ->
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

       findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
           }
       });
       /* findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });*/
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
            changeFragment(new ProfileFragment());
        }else if (view == itemLogOut){
            changeFragment(new CalendarFragment());
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment());
        }else if(view == itemAddNewEmployee){

            // Create the builder with required paramaters - Context, Title, Positive Text
            CustomDialog.Builder builder = new CustomDialog.Builder(this, "Add New Employee", "Accept");

            // Now we can any of the following methods.
            //builder.content(getResources().getString(R.string.text_whats_your_phone_email));
            builder.negativeText("Cancel");
            builder.darkTheme(false);
         //   builder.typeface(Typeface.DEFAULT);
        //    builder.titleTextSize(28);
          //  builder.contentTextSize(18);
            //builder.buttonTextSize(20);
            builder.titleAlignment(BaseDialog.Alignment.LEFT); // Use either Alignment.LEFT, Alignment.CENTER or Alignment.RIGHT
//            builder.titleColor(getResources().getColor(R.color.black)); // int res, or int colorRes parameter versions available as well.
  //          builder.contentColor(getResources().getColor(R.color.black)); // int res, or int colorRes parameter versions available as well.
    //        builder.positiveColor(getResources().getColor(R.color.black)); // int res, or int colorRes parameter versions available as well.
      //      builder.negativeColor(getResources().getColor(R.color.black)); // int res, or int colorRes parameter versions available as well.
            // builder.positiveBackground(Drawable drawable); // int res parameter version also available.
           // builder.rightToLeft(true); // Enables right to left positioning for languages that may require so.
            builder.buttonAlignment(BaseDialog.Alignment.RIGHT);
            final CustomDialog customDialog = builder.build();

            final TextView message = (TextView) customDialog.getView().findViewById(R.id.dialog_custom_content);
            message.setVisibility(View.INVISIBLE);

            final ProgressBar checkEmailPb = (ProgressBar) customDialog.getView().findViewById(R.id.checkEmailExistsProgressBar);
            checkEmailPb.setVisibility(View.INVISIBLE);

// Show the dialog.

            customDialog.setClickListener(new CustomDialog.ClickListener() {
                @Override
                public void onConfirmClick() {

                    newUserEmail = (FloatingEditText) customDialog.getView().findViewById(R.id.newUserEmail);
                    final String email = newUserEmail.getText().toString();
                    if(!Utils.isEmailValid(email)){
                       Utils.showToastShort("Invalid Email");
                        return;
                    }


                    checkEmailPb.setVisibility(View.VISIBLE);
                  ParseQuery<NewUser> query = NewUser.getQuery();
                  query.whereEqualTo("email", email);
                  query.findInBackground(new FindCallback<NewUser>() {
                      @Override
                      public void done(List<NewUser> newUsers, ParseException e) {

                          checkEmailPb.setVisibility(View.INVISIBLE);


                          if(e==null)
                          {
                              if(newUsers != null && newUsers.size() > 0){
                                 msg = "The email is already registered";
                                  msgColor = Color.RED;

                              }
                              else{

                                  NewUser newUser = new NewUser();
                                  newUser.setCvr(Prefs.getBusinessCvr());
                                  newUser.setEmail(email);
                                  newUser.saveInBackground(new SaveCallback() {
                                      @Override
                                      public void done(ParseException e) {
                                          //customDialog.dismiss();
                                          if(e==null){
                                             // msg = "Employee with email registered successfully";

                                              customDialog.dismiss();
                                               SweetAlerts.showSuccessMsg(Choose_Task.this, "Employee with email registered successfully");
                                              msgColor = Color.GREEN;
                                          }
                                        else{
                                              msg = "Failed to register employee";
                                          }
                                          message.setText(msg);
                                          message.setVisibility(View.VISIBLE);
                                          message.setTextColor(msgColor);
                                      }
                                  });


                              }
                          }
                          else{
                              msg = "An error occurred: " + e.getMessage();
                          }

                          if(msg != null){
                              message.setText(msg);
                              message.setVisibility(View.VISIBLE);
                              message.setTextColor(msgColor);
                          }



                      }
                  });


                }

                @Override
                public void onCancelClick() {
                Utils.showToastShort("DET ER CANNECEL");

                }
            });

            customDialog.show();
        }
        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    // What good method is to access resideMenuï¼Ÿ
    public ResideMenu getResideMenu(){
        return resideMenu;
    }



  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

//        getCurrentFragment().onActivityResult(requestCode, resultCode, data);



        //Fragment fragment = getSupportFragmentManager().findFragmentByTag("profile");
       // fragment.onActivityResult(requestCode, resultCode, data);
        Utils.showToastShort("OnActiviResultChooseTask");

    }*/
}
/*
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.storeapp.ActivitiesOld.BaseActivity;
import com.storeapp.ActivitiesOld.Cash_Register;
import com.storeapp.ActivitiesOld.Emp_Info;
import com.storeapp.ActivitiesOld.MaintenanceService;
import com.storeapp.ActivitiesOld.Prefs;
import com.storeapp.ActivitiesOld.Settings;
import com.storeapp.R;
import com.storeapp.RegistrationFragmentBusiness.Activities.EmployeeList.Emp_List;
import com.storeapp.RegistrationFragmentBusiness.Activities.StoreStatus.Store_Status;

public class Choose_Task extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_choose_task_dashboard);

	//	if (!Prefs.isUserLoggedIn()) {
	//		startActivity(new Intent(this, Login.class));
	//	}

		startService(new Intent(this, MaintenanceService.class));

		actionBar.hide();

		final Button btnEmpInfo = (Button) findViewById(R.id.btnEmpInfo);

		btnEmpInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Choose_Task.this, Emp_Info.class));

			}
		});

		Button ctLogOut = (Button) findViewById(R.id.ctLogOut);
		ctLogOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//logout();

			}
		});

		Button ctCashRegBtn = (Button) findViewById(R.id.ctCashRegBtn);
		ctCashRegBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Choose_Task.this,
						Cash_Register.class);
				startActivity(intent);

			}
		});

		Button ctStoreStatus = (Button) findViewById(R.id.ctStoreStatus);
		ctStoreStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Choose_Task.this, Store_Status.class);
				startActivity(intent);

			}
		});

		Button ctStoreItemBtn = (Button) findViewById(R.id.ctStoreItemBtn);
		ctStoreItemBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Choose_Task.this, Store_Items.class));
                //startActivity(new Intent(Choose_Task.this, Emp_List.class));


            }
		});

		Button ctEmplList = (Button) findViewById(R.id.ctEmplList);
		ctEmplList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Choose_Task.this, Emp_List.class));
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		//logout();
	}

	private void logout() {
		if (Prefs.isUserLoggedIn()) {
			new AlertDialog.Builder(Choose_Task.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.text_log_out)
					.setMessage(R.string.text_want_log_out)
					.setPositiveButton(R.string.text_yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Prefs.setUserLoggedIn(false);
									startActivity(new Intent(Choose_Task.this,
											Login.class));
									finish();

								}
							}).setNegativeButton(R.string.text_no, null).show();

		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			startActivity(new Intent(Choose_Task.this, Settings.class));
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_choose_task;
	}

}
*/
