package com.storeapp.MainMenu.LeftMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.storeapp.Login_Registration.Login;
import com.storeapp.MainMenu.AddNewEmployeeDialog;
import com.storeapp.MainMenu.CashRegister.LogoutDialog;
import com.storeapp.R;
import com.storeapp.util.Prefs;

public class Choose_Task extends FragmentActivity {


    private ResideMenu resideMenu;
    private Choose_Task mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemLogOut;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemAddNewEmployee;
    ImageButton btnEditProfilePen;
    boolean isProfileEidtable=false;
    ProfileFragment profileFragment = new ProfileFragment();
    TextView actionBarHometxt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_task_dashboard);
        mContext = this;
        setUpMenu();
        changeFragment(new HomeFragment());
        getActionBar().hide();

        if (!Prefs.isUserLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        actionBarHometxt = (TextView) findViewById(R.id.actionBarHometxt);

        btnEditProfilePen = (ImageButton) findViewById(R.id.btnEditProfile);
        btnEditProfilePen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isProfileEidtable){
                    profileFragment.startEditingProfile();
                    isProfileEidtable = true;
                    btnEditProfilePen.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_nike_white));


                }else{
                    profileFragment.endEditingProfile();
                    isProfileEidtable = false;
                    btnEditProfilePen.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_pen_white));

                }

            }
        });

        itemHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    itemHome.setIcon(R.drawable.ic_home_grey);
                    changeFragment(new HomeFragment());
                    btnEditProfilePen.setVisibility(View.GONE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    itemHome.setIcon(R.drawable.ic_home_white);
                    resideMenu.closeMenu();

                }

                return true;
            }
        });


        itemProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    itemProfile.setIcon(R.drawable.ic_profile_grey);
                    changeFragment(profileFragment);
                    actionBarHometxt.setText("Profile");
                    btnEditProfilePen.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    itemProfile.setIcon(R.drawable.ic_profile_white);
                    resideMenu.closeMenu();

                }

                return true;
            }
        });

        itemAddNewEmployee.setVisibility(View.GONE);

        if(Prefs.isUserAdmin()){
            itemAddNewEmployee.setVisibility(View.VISIBLE);

        }


        itemAddNewEmployee.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    itemAddNewEmployee.setIcon(R.drawable.ic_add_employee_black);

                    btnEditProfilePen.setVisibility(View.GONE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(new Intent(Choose_Task.this, AddNewEmployeeDialog.class));

                    itemAddNewEmployee.setIcon(R.drawable.ic_add_employee_white);

                }

                return true;
            }
        });

        /*itemSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    itemSettings.setIcon(R.drawable.ic_setting_white);
                    btnEditProfilePen.setVisibility(View.GONE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    changeFragment(new SettingsFragment());
                    resideMenu.closeMenu();

                    itemSettings.setIcon(R.drawable.ic_setting_white);

                }

                return true;
            }
        });*/

        itemLogOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    itemLogOut.setIcon(R.drawable.ic_log_out_grey);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(new Intent(Choose_Task.this, LogoutDialog.class));
                    itemLogOut.setIcon(R.drawable.ic_log_out_white);
                }

                return true;
            }
        });


    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //set background of menu
        resideMenu.setBackground(R.color.dark_gray);

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.ic_home_white,"Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.ic_profile_white,  "Profile");
        itemAddNewEmployee = new ResideMenuItem(this, R.drawable.ic_add_employee_white, "Add New Employee");
       // itemSettings = new ResideMenuItem(this, R.drawable.ic_setting_white, "Settings");
        itemLogOut = new ResideMenuItem(this, R.drawable.ic_log_out_white, "Log Out");

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAddNewEmployee, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogOut, ResideMenu.DIRECTION_LEFT);


        // You can disable a direction by setting ->
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

       findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
           }
       });



    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(Choose_Task.this, LogoutDialog.class));
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
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

    public ResideMenu getResideMenu(){
        return resideMenu;
    }



}
