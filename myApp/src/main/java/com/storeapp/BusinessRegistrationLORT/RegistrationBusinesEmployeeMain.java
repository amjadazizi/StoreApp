/*
package com.storeapp.RegistrationFragmentBusiness.RegistMain;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.storeapp.R;
import com.storeapp.RegistrationFragmentBusiness.Activities.RegistrationActivityFragCont;
import com.storeapp.RegistrationFragmentBusiness.RegistrationBusinessInfo;
import com.storeapp.util.Utils;

public class RegistrationBusinesEmployeeMain extends Activity {

    ImageButton btnImgBusiness, btnImgEmployee;
    public static String EXTRA_ACTIVITY_CONTENT = "ekstra_activity_content";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_busines_employee_main);


        btnImgBusiness = (ImageButton) findViewById(R.id.btnImgBusiness);


        btnImgBusiness.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startRegistActivity(1);



            }
        });

        btnImgEmployee = (ImageButton) findViewById(R.id.btnImgEmployee);


        btnImgEmployee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startRegistActivity(2);

            }
        });

    }



    private void startRegistActivity(int task)
    {
        Intent i = new Intent(this, RegistrationActivityFragCont.class);
        i.putExtra(EXTRA_ACTIVITY_CONTENT,task);
        startActivity(i);
    }



}
*/
