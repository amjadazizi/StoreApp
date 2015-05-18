package com.storeapp.MainMenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.storeapp.R;
import com.storeapp.parse.NewUser;
import com.storeapp.ui.FloatingEditText;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;

/**
 * Created by Amjad on 21-03-2015.
 */
public class AddNewEmployeeDialog extends Activity {

    Button btnCasgDiaCancel,btnCasgDiaAccept;
    FloatingEditText newUserEmail;
    String msg = null;
    int msgColor = Color.RED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);

        setContentView(R.layout.add_new_employee_custom_dialog);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_add_employee_black);
        this.setTitle("Add Employee");
        setTitleColor(Color.BLACK);
        final TextView message = (TextView) findViewById(R.id.dialog_custom_content);
        message.setVisibility(View.INVISIBLE);

        final ProgressBar checkEmailPb = (ProgressBar) findViewById(R.id.checkEmailExistsProgressBar);
        checkEmailPb.setVisibility(View.INVISIBLE);



        btnCasgDiaAccept = (Button) findViewById(R.id.btnCashDiaAccept);
        btnCasgDiaAccept.setText("Add");
        btnCasgDiaAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                newUserEmail = (FloatingEditText) findViewById(R.id.newUserEmail);
                final String email = newUserEmail.getText().toString();
                if(!Utils.isEmailFormatValid(email)){
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
                                        if(e==null){
                                            finish();
                                            SweetAlerts.showSuccessMsg(AddNewEmployeeDialog.this, "Employee with email registered successfully");
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
        });


        btnCasgDiaCancel = (Button) findViewById(R.id.btnCasgDiaCancel);
        btnCasgDiaCancel.setText("Cancel");
        btnCasgDiaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }



}


