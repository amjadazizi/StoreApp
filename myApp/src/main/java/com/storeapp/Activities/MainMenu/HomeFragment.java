package com.storeapp.Activities.MainMenu;

/**
 * Created by Amjad on 04-03-2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.special.ResideMenu.ResideMenu;
import com.storeapp.Activities.EmployeeList.Emp_List;
import com.storeapp.Activities.StoreStatus.Store_Items;
import com.storeapp.Activities.StoreStatus.Store_Status;
import com.storeapp.R;
import com.storeapp.storestatusfragments.BarChartActivityExample;


public class HomeFragment extends Fragment {

    private View view;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
       // setUpViews();





            //	if (!Prefs.isUserLoggedIn()) {
            //		startActivity(new Intent(this, Login.class));
            //	}

           // startService(new Intent(this, MaintenanceService.class));


           /* final Button btnEmpInfo = (Button) view.findViewById(R.id.btnEmpInfo);

            btnEmpInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getActivity(), Emp_Info.class));

                }
            });*/

          /*  Button ctLogOut = (Button) view.findViewById(R.id.ctLogOut);
            ctLogOut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //logout();

                }
            });*/

            Button ctCashRegBtn = (Button) view.findViewById(R.id.ctCashRegBtn);
            ctCashRegBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                  //  SweetAlerts.showBasicMsg(getActivity(),"This is  Activity a Msg");
                    Intent intent = new Intent(getActivity(),
                            BarChartActivityExample.class);
                    startActivity(intent);

                }
            });

            Button ctStoreStatus = (Button) view.findViewById(R.id.ctStoreStatus);
            ctStoreStatus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), Store_Status.class);
                    startActivity(intent);

                }
            });

            Button ctStoreItemBtn = (Button) view.findViewById(R.id.ctStoreItemBtn);
            ctStoreItemBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    startActivity(new Intent(getActivity(), Store_Items.class));
                    //startActivity(new Intent(Choose_Task.this, Emp_List.class));


                }
            });

            Button ctEmplList = (Button) view.findViewById(R.id.ctEmplList);
            ctEmplList.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), Emp_List.class));
                }
            });

        return view;

    }




      /*  private void logout() {
            if (Prefs.isUserLoggedIn()) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.text_log_out)
                        .setMessage(R.string.text_want_log_out)
                        .setPositiveButton(R.string.text_yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Prefs.setUserLoggedIn(false);
                                        startActivity(new Intent(getActivity(),
                                                Login.class));
                                        getActivity().finish();

                                    }
                                }).setNegativeButton(R.string.text_no, null).show();

            }

        }*/

       /* @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            } else {
                //return super.onKeyUp(keyCode, event);
            }*/







   /* private void setUpViews() {
        Choose_Task parentActivity = (Choose_Task) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }*/


}