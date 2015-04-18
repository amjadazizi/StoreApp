package com.storeapp.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.storeapp.R;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Amjad on 21-03-2015.
 */
public class PictureDialog extends Activity {

    Button txtChooseFromGallery, txtTakePicture;
    Button btnCancel;
    public static final int REQUEST_TAKE_PICTURE = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_CROP_IMAGE = 3;
    private Uri imageUri;
    public static final String EXTRA_IMAGE_URI = "extra_image_file_name";

    public interface OnPictureTakenListener {
      //  public void pictureTaken(Bitmap bitmap);
    }

    private OnPictureTakenListener pictureTakenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_add_employee_black);
        setContentView(R.layout.take_picture_dialog);
        this.setTitle("Add Picture");
        setTitleColor(Color.BLACK);


        txtChooseFromGallery = (Button) findViewById(R.id.txtChooseFromGallery);
        txtTakePicture = (Button) findViewById(R.id.txtTakePicture);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtChooseFromGallery.setText("Choose from Gallery");
        txtTakePicture.setText("Take Picture");
        btnCancel.setText("Cancel");

        txtTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Take Photo
                Calendar cal = Calendar.getInstance();

                File imgaeFile = new File(Environment.getExternalStorageDirectory(), cal.getTimeInMillis() + ".jpg");
                imageUri = Uri.fromFile(imgaeFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_TAKE_PICTURE);


            }
        });


        txtChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void setPictureTakenListener(OnPictureTakenListener pictureTakenListener) {
        this.pictureTakenListener = pictureTakenListener;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    intent.putExtra(EXTRA_IMAGE_URI, imageUri.toString());
                    break;
                case REQUEST_GALLERY:
                    Uri selectedImage = data.getData();
                    intent.putExtra(EXTRA_IMAGE_URI, selectedImage.toString());
                    break;
                default:
                    finish();
                    return;
            }

            setResult(RESULT_OK, intent);
            finish();

        } else {
            setResult(RESULT_CANCELED);
            finish();
        }

    }

}


