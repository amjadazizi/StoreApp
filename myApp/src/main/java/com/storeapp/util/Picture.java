package com.storeapp.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.storeapp.R;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Amjad on 21-03-2015.
 */
public class Picture extends Activity {

    Button txtChooseFromGallery, txtTakePicture;
    Button btnCancel;
    public static final int REQUEST_TAKE_PICTURE = 1;
    public static final int REQUEST_GALLERY = 2;
    private Uri imageUri;
    public static final String EXTRA_IMAGE_URI = "extra_image_file_name";

    public interface OnPictureTakenListener {
        public void pictureTaken(Bitmap bitmap);
    }

    private OnPictureTakenListener pictureTakenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                //dialog.dismiss();
                finish();
            }
        });

    }

    public void setPictureTakenListener(OnPictureTakenListener pictureTakenListener) {
        this.pictureTakenListener = pictureTakenListener;
    }


    /*private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }*/

  /*  private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(AppContextProvider.getContext());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);

                         //   startActivityForResult(intent, 1);

                   // startActivityForResult();


                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }*/


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


