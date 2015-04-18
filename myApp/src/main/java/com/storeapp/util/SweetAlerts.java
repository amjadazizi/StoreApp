package com.storeapp.util;

import android.content.Context;
import android.widget.Toast;

import com.storeapp.AppContextProvider;
import com.storeapp.ui.sweetalert.SweetAlertDialog;

/**
 * Created by Amjad on 16-03-2015.
 */
public class SweetAlerts {

    //TODO refactor this class to use method overloading

  //  A basic message：

    public static void showBasicMsg(Context ctx,String msg){
        if(ctx==null){
            Toast.makeText(AppContextProvider.getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

         new SweetAlertDialog(ctx)
                .setTitleText(msg)
                .show();
    }




 //   A title with a text under：
    public static  void showBasicMsgWithTtile(Context ctx, String title ,String msg){

        if(ctx==null){
            Toast.makeText(AppContextProvider.getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

        new SweetAlertDialog(ctx)
                .setTitleText(title)
                .setContentText(msg)
                .show();
    }

 //   A error message：

    public static  void showErrorMsg(Context ctx, String msg){

        if(ctx==null){
            Toast.makeText(AppContextProvider.getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

        new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(msg)
                .show();
    }

 //   A warning message：

    public static  void showBasicMsg(Context ctx, String title, String content, String msg){

        if(ctx==null){
            Toast.makeText(AppContextProvider.getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

        new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(msg)
                .show();

    }



 //   A success message：

    public static  void showSuccessMsg(Context ctx, String msg){

        if(ctx==null){
            Toast.makeText(AppContextProvider.getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText(msg)
                .show();

    }

    //  A message with a custom icon：

   /*         new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
    .setTitleText("Sweet!")
    .setContentText("Here's a custom image.")
    .setCustomImage(R.drawable.custom_img)
    .show();
*/


 //   Bind the listener to confirm button：

   /*         new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
    .setTitleText("Are you sure?")
    .setContentText("Won't be able to recover this file!")
    .setConfirmText("Yes,delete it!")
    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sDialog) {
            sDialog.dismissWithAnimation();
        }
    })
            .show();
*/
  //  Show the cancel button and bind listener to it：
/*
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
    .setTitleText("Are you sure?")
    .setContentText("Won't be able to recover this file!")
    .setCancelText("No,cancel plx!")
    .setConfirmText("Yes,delete it!")
    .showCancelButton(true)
    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sDialog) {
            sDialog.cancel();
        }
    })
            .show();*/
}




