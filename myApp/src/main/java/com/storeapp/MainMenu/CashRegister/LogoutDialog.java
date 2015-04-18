package com.storeapp.MainMenu.CashRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.storeapp.Login_Registration.Login;
import com.storeapp.R;
import com.storeapp.util.Prefs;


public class LogoutDialog extends Activity {

    Button btnLogOut, btnLogOutCancel;
    TextView txtWantToLogOut;
    public static final String ACTION_PAYMENT_DONE = "com.storeapp.payment.done.intent.action";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.log_out_dialog);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_log_out_grey);
        this.setTitle("Log Out ?");
        setTitleColor(Color.BLACK);



        txtWantToLogOut = (TextView) findViewById(R.id.txtWantToLogOut);
        txtWantToLogOut.setText(R.string.text_want_log_out);






        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setText("LOG OUT");
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Prefs.setUserLoggedIn(false);
                startActivity(new Intent(LogoutDialog.this,
                        Login.class));
                Prefs.setUserAsAdmin(false);
                finish();

            }
        });

        btnLogOutCancel = (Button) findViewById(R.id.btnPaymentCancel);
        btnLogOutCancel.setText("Cancel");
        btnLogOutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}


