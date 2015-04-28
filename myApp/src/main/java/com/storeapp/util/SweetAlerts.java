package com.storeapp.util;

import android.content.Context;

import com.storeapp.ui.sweetalert.SweetAlertDialog;

/**
 * Created by Amjad on 16-03-2015.
 */
public class SweetAlerts {


  //  A basic message：

    public static void showBasicMsg(Context ctx,String msg){


         new SweetAlertDialog(ctx)
                .setTitleText(msg)
                .show();
    }

 //   A error message：

    public static  void showErrorMsg(Context ctx, String msg){

        new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(msg)
                .show();
    }


 //   A success message：

    public static  void showSuccessMsg(Context ctx, String msg){

        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText(msg)
                .show();

    }


}




