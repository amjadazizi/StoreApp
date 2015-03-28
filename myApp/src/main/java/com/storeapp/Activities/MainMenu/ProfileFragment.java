package com.storeapp.Activities.MainMenu;

/**
 * Created by Amjad on 04-03-2015.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.storeapp.Activities.parse.User;
import com.storeapp.R;
import com.storeapp.util.Picture;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FloatingActionButton btnTakePicture;
    private ImageView imgProfile;
    Button btn_edit;
    TextView txtProPersonName;
    private  View view;
    String  imageFilePath;
    boolean isViewsEdiable;
    String userObjectId;
    boolean editError= false;
    EditText editProEmail, editProPhoneNumber, editProNewPassword, editProRepeatPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        Choose_Task.setTopBarGone();
        btnTakePicture = (FloatingActionButton)   view.findViewById(R.id.btnTakePicture);
        btnTakePicture.setVisibility(View.GONE);
        imgProfile = (ImageView)   view.findViewById(R.id.imgProfile);
        editProEmail = (EditText) view.findViewById(R.id.editProEmail);
        editProPhoneNumber = (EditText) view.findViewById(R.id.editProPhoneNumber);
       // editProCurrentPassword = (EditText) view.findViewById(R.id.editProCurrentPassword);
        editProNewPassword = (EditText) view.findViewById(R.id.editProNewPassword);
        editProRepeatPassword = (EditText) view.findViewById(R.id.editProRepeatPassword);
        txtProPersonName = (TextView) view.findViewById(R.id.txtProPersonName);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        setViewsEditableFalse();


        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(getActivity(), Picture.class);
               startActivityForResult(intent, Picture.REQUEST_TAKE_PICTURE);
               // changePassword();
            }
        });


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_right));
                if(!isViewsEdiable){
                    setViewsEditableTrue();
                    btnTakePicture.setVisibility(View.VISIBLE);

                }else {

                    setViewsEditableFalse();
                    updateUserInfo();
                    btnTakePicture.setVisibility(View.GONE);

                    btn_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_call_white));

                }



            }
        });

        getEmployeeInfo();

        return view;
    }



    private void setViewsEditableFalse(){
          editProPhoneNumber.setFocusable(false);
          editProNewPassword.setFocusable(false);
          editProRepeatPassword.setFocusable(false);
          isViewsEdiable=false;
    }

    private void setViewsEditableTrue(){
        editProPhoneNumber.setFocusableInTouchMode(true);
        editProNewPassword.setFocusableInTouchMode(true);
        editProRepeatPassword.setFocusableInTouchMode(true);
        isViewsEdiable=true;

    }
   /* private void changePassword(){

        String newPassword = editProNewPassword.getText().toString();
        String newPassRepeat = editProRepeatPassword.getText().toString();

        if(!Utils.isNullOrEmpty(newPassword)&&!Utils.isNullOrEmpty(newPassRepeat)){
            return;
        }
        if(!newPassword.equals(newPassRepeat)){
            SweetAlerts.showErrorMsg(getActivity(), "Password Dosnt Match!");
            return;
        }

        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setPassword(editProNewPassword.getText().toString());
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) {
                    //SweetAlerts.showSuccessMsg(getActivity(), "Password changed!");
                } else {
                    editError=true;

                    changePasswordMsg = "Error. Try Again!";
                 //   SweetAlerts.showErrorMsg(getActivity(), );
                }
            }
        });
    }
*/


    private void updateUserInfo(){

        ParseQuery query = new ParseQuery(User.class);
        query.getInBackground(userObjectId, new GetCallback() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    String phoneNumber = editProPhoneNumber.getText().toString();
                    if(Utils.isNullOrEmpty(phoneNumber)){
                        SweetAlerts.showErrorMsg(getActivity(),"Enter Your Phone Number.");
                        return;
                    }
                    Drawable itemImage = imgProfile.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)itemImage).getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                    ParseFile file = new ParseFile(editProEmail.getText().toString()+".png", image);
                    // Upload the image into Parse Cloud
                    //file.saveInBackground();

                    object.put(User.COL_PROFILE_IMAGE, file);

                    object.put(User.COL_PHONE_NUMBER, phoneNumber);



                    object.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {

                            if(e==null){

                                String msg = null;

                                /// Update Password
                                String newPassword = editProNewPassword.getText().toString();
                                String newPassRepeat = editProRepeatPassword.getText().toString();

                                if(Utils.isNullOrEmpty(newPassword)&&Utils.isNullOrEmpty(newPassRepeat)){
                                    SweetAlerts.showSuccessMsg(getActivity(),"Phne Updated og udd ! ");
                                    return;
                                }
                                if(!newPassword.equals(newPassRepeat)){
                                    SweetAlerts.showErrorMsg(getActivity(), "Password Dosnt Match!");
                                    return;
                                }

                                ParseUser parseUser = ParseUser.getCurrentUser();
                                parseUser.setPassword(editProNewPassword.getText().toString());
                                parseUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            getEmployeeInfo();
                                            SweetAlerts.showSuccessMsg(getActivity(), "Info Updated!");
                                        } else {

                                           // changePasswordMsg = "Error. Try Again!";
                                            //   SweetAlerts.showErrorMsg(getActivity(), );
                                        }
                                    }
                                });


                            }

                        }
                    });

                }

                else {
                    e.printStackTrace();
                }

            }
        });

    }


     private void getEmployeeInfo(){
        String email = Prefs.getPrefLogedInUser();
         ParseFile profileImage;

         ParseQuery<User> query = ParseQuery.getQuery(User.class);
         query.whereEqualTo("username",email );
         query.findInBackground(new FindCallback<User>() {
             @Override
             public void done(List<User> users, ParseException e) {
                 if (e == null) {
                    if(users!=null && users.size()>0){

                        ParseFile profileImage =  users.get(0).getProfileImage();
                        if (profileImage != null) {
                            profileImage.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
                                                data.length);
                                        if (bmp != null) {
                                            Utils.showToastShort("parse file ok");

                                            imgProfile.setImageBitmap(bmp);
                                        }
                                    } else {
                                       // Log.e("paser after downloade", " null");
                                    }

                                }
                            });
                        } else {

                            Log.e("parse file", " null");

                            // img.setImageResource(R.drawable.ic_launcher);

                            //img.setPadding(10, 10, 10, 10);
                        }

                        ////////


                        txtProPersonName.setText(users.get(0).getFirstName()+" "+ users.get(0).getLastName());
                        editProEmail.setText(users.get(0).getEmail());
                        editProPhoneNumber.setText(users.get(0).getPhoneNumber());
                        userObjectId = users.get(0).getObjectId();



                    }

                 } else {

                 }
             }
         });







     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==getActivity().RESULT_OK){
            switch (requestCode){
                case Picture.REQUEST_TAKE_PICTURE:
                case Picture.REQUEST_GALLERY:
                    Bundle bundle =  data.getExtras();
                    if(bundle!=null){

                        imageFilePath = bundle.getString(Picture.EXTRA_IMAGE_URI);
                        Uri imageUri = Uri.parse(imageFilePath);

                        // File file = new File(imageFilePath);
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            imgProfile.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;



            }
        }
    }
}