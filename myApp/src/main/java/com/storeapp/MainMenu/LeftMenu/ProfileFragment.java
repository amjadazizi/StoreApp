package com.storeapp.MainMenu.LeftMenu;

/**
 * Created by Amjad on 04-03-2015.
 */

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.storeapp.R;
import com.storeapp.parse.User;
import com.storeapp.util.PictureDialog;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FloatingActionButton btnTakePicture;
    ImageView imgProfile;
    Button btn_edit;
    TextView txtProPersonName;
    private  View view;
    String  imageFilePath;
    boolean isViewsEdiable;
    String userObjectId;
    EditText editProEmail, editProPhoneNumber, editProNewPassword, editProRepeatPassword;
    private User user;
    Picasso picasso;
    Bitmap bitmap;
    Uri imageUri;
    String passwordChangedMsg = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        btnTakePicture = (FloatingActionButton)   view.findViewById(R.id.btnTakePicture);
        btnTakePicture.setVisibility(View.GONE);
        imgProfile  = (ImageView)   view.findViewById(R.id.imgProfile);
        editProEmail = (EditText) view.findViewById(R.id.editProEmail);
        editProPhoneNumber = (EditText) view.findViewById(R.id.editProPhoneNumber);
        editProNewPassword = (EditText) view.findViewById(R.id.editProNewPassword);
        editProRepeatPassword = (EditText) view.findViewById(R.id.editProRepeatPassword);
        txtProPersonName = (TextView) view.findViewById(R.id.txtProPersonName);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        setViewsEditableFalse();

        int largeMemoryInMegaBytes = ((ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;

        Picasso.Builder pb = new Picasso.Builder(getActivity());
        picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        picasso.setIndicatorsEnabled(true);

        btnTakePicture.setColorNormalResId(R.color.button_normal);
        btnTakePicture.setColorPressedResId(R.color.button_pressed);
        btnTakePicture.setIcon(R.drawable.ic_camera_white);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(getActivity(), PictureDialog.class);
               startActivityForResult(intent, PictureDialog.REQUEST_TAKE_PICTURE);
            }
        });
        getEmployeeInfo();
        return view;
    }


    public  void startEditingProfile(){
        setViewsEditableTrue();
        btnTakePicture.setVisibility(View.VISIBLE);
    }

    public void endEditingProfile(){
        setViewsEditableFalse();
        updateUserInfo();
        btnTakePicture.setVisibility(View.GONE);
        editProNewPassword.setText("");
        editProRepeatPassword.setText("");
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


    private String changePassword(){

        String newPassword = editProNewPassword.getText().toString();
        String newPassRepeat = editProRepeatPassword.getText().toString();


        if(!newPassword.equals(newPassRepeat)){
            passwordChangedMsg=  "Password Dosnt Match!";
        }else{

            ParseUser parseUser = ParseUser.getCurrentUser();
            parseUser.setPassword(editProNewPassword.getText().toString());
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (null == e) {
                    } else {
                        passwordChangedMsg = "Error. Try Again!";
                    }
                }
            });

        }


        return  passwordChangedMsg;
    }



private void updateUserInfo(){

    String email = editProEmail.getText().toString();
    String phoneNumber = editProPhoneNumber.getText().toString();
    String newPassword = editProNewPassword.getText().toString();
    String repeatPassword = editProRepeatPassword.getText().toString();

    if(!Utils.isNullOrEmpty(newPassword) && !Utils.isNullOrEmpty(repeatPassword)){
        String msg = changePassword();
        if(msg!=null){
            SweetAlerts.showErrorMsg(getActivity(), msg);
            return;
        }
    }


    String message = null;

    if (!Utils.isNullOrEmpty(email) &&
            !Utils.isNullOrEmpty(phoneNumber))

    {

        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        if(bitmap!=null){
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();

            String imageContentType = getImageContentType(imageUri);

            // Create the ParseFile
            String imageName = txtProPersonName.getText().toString() + "." + imageContentType;
            ParseFile file = new ParseFile(imageName, imageBytes, imageContentType);
            // Upload the image into Parse Cloud
            file.saveInBackground();

            user.setProfileImage(file);
        }


        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    SweetAlerts.showSuccessMsg(getActivity(), " Success! Info Updated");

                } else {

                    SweetAlerts.showErrorMsg(getActivity(), "Error! Try Again.");
                }
            }
        });

    } else {
        SweetAlerts.showErrorMsg(getActivity(),"ERROR");
    }

}

    private String getImageContentType(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String imageContentType = mime.getExtensionFromMimeType(cr.getType(uri));
        return imageContentType;
    }

     private void getEmployeeInfo(){
        String email = Prefs.getPrefLogedInUser();
         ParseQuery<User> query = ParseQuery.getQuery(User.class);
         query.whereEqualTo("username",email );
         query.findInBackground(new FindCallback<User>() {
             @Override
             public void done(List<User> users, ParseException e) {
                 if (e == null) {
                    if(users!=null && users.size()>0){

                        user = users.get(0);
                        txtProPersonName.setText(user.getFirstName()+" "+ users.get(0).getLastName());
                        editProEmail.setText(user.getEmail());
                        editProPhoneNumber.setText(user.getPhoneNumber());
                        userObjectId = user.getObjectId();


                        ParseFile pf = user.getProfileImage();
                        if(pf!=null){
                            picasso.load(pf.getUrl()).into(imgProfile);
                        }
                    }

                 } else {

                 }
             }
         });


     }



    private void cropImage(Uri sourceUri) {

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(sourceUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 2);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 160);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, PictureDialog.REQUEST_CROP_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==getActivity().RESULT_OK){
            Bundle bundle = data.getExtras();

            switch (requestCode){
                case PictureDialog.REQUEST_TAKE_PICTURE:
                case PictureDialog.REQUEST_GALLERY:
                    if (bundle != null) {

                        imageFilePath = bundle.getString(PictureDialog.EXTRA_IMAGE_URI);
                        imageUri = Uri.parse(imageFilePath);


                        cropImage(imageUri);
                    }
                    break;

                case PictureDialog.REQUEST_CROP_IMAGE:

                    try {

                        if (bitmap != null) {
                            bitmap.recycle();
                        }

                        bitmap = bundle.getParcelable("data");

                        imgProfile.setImageBitmap(bitmap);

                        SweetAlerts.showBasicMsg(getActivity(), bitmap.getWidth() + ", " + bitmap.getHeight());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    }
}