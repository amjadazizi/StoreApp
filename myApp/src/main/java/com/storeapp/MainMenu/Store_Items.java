package com.storeapp.MainMenu;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.gc.materialdesign.widgets.ColorSelector.OnColorSelectedListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.storeapp.BaseActivity;
import com.storeapp.R;
import com.storeapp.parse.InventoryItem;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.ui.FloatingEditText;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class Store_Items extends BaseActivity implements OnColorSelectedListener {

    FloatingActionButton btnAddImage;
    Bitmap bitmap;
    String imageFilePath;
    Uri imageUri;
    ImageView imgItemPic;
    Button btnScanItem;
    String scanBarcode;
    String scanEanType;
    FloatingEditText editBarcode, editEan, editDecription, editAmount,editCurAmountNstock, editPurchaseprice, editSellprice;
    Picasso picasso;
    boolean itemImageExists = false;
    private InventoryItem inventoryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_items);


        int largeMemoryInMegaBytes = ((ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;

        Picasso.Builder pb = new Picasso.Builder(this);
        picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        picasso.setIndicatorsEnabled(true);
        btnAddImage = (FloatingActionButton) findViewById(R.id.btnAddImage);
        btnAddImage.setColorNormalResId(R.color.button_normal);
        btnAddImage.setColorPressedResId(R.color.button_pressed);
        btnAddImage.setIcon(R.drawable.ic_camera_white);

        btnAddImage.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent intent = new Intent(Store_Items.this, PictureDialog.class);
                startActivityForResult(intent, PictureDialog.REQUEST_TAKE_PICTURE);
            }

        });

        editBarcode = (FloatingEditText) findViewById(R.id.editBarcode);
        editEan = (FloatingEditText) findViewById(R.id.editEan);
        editDecription = (FloatingEditText) findViewById(R.id.editDecription);
        editAmount = (FloatingEditText) findViewById(R.id.editAmount);
        editCurAmountNstock = (FloatingEditText) findViewById(R.id.editCurAmountNstock);
        editCurAmountNstock.setFocusable(false);
        editPurchaseprice = (FloatingEditText) findViewById(R.id.editPurchaseprice);
        editSellprice = (FloatingEditText) findViewById(R.id.editSellprice);
        imgItemPic = (ImageView) findViewById(R.id.imgItemPic);
        editBarcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    itemExistsCount(editBarcode.getText().toString());

                } else if (hasFocus) {

                }
            }
        });

        btnScanItem = (Button) findViewById(R.id.btnScanItem);
        btnScanItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(Store_Items.this);
                scanIntegrator.initiateScan();
            }
        });


        Button btnSaveItem = (Button) findViewById(R.id.btnSaveItem);
        btnSaveItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String barcode = editBarcode.getText().toString();
                String eanType = editEan.getText().toString();
                String description = editDecription.getText().toString();
                String amount = editAmount.getText().toString();
                String purchasePrice = editPurchaseprice.getText().toString();
                String sellPrice = editSellprice.getText().toString();

                if(inventoryItem==null){
                    inventoryItem = new InventoryItem();
                }

                    if (!Utils.isNullOrEmpty(barcode) &&
                            !Utils.isNullOrEmpty(description) &&
                            !Utils.isNullOrEmpty(amount) &&
                            !Utils.isNullOrEmpty(purchasePrice) &&
                            !Utils.isNullOrEmpty(sellPrice))

                    {
                        inventoryItem.setCvrNumber(Prefs.getBusinessCvr());
                        inventoryItem.setBarcode(barcode);
                        inventoryItem.setEanType(eanType);
                        inventoryItem.setDescription(description);
                        inventoryItem.setAmount(Integer.parseInt(amount));
                        inventoryItem.setPurchasePrice(Double.parseDouble(purchasePrice));
                        inventoryItem.setSellPrice(Double.parseDouble(sellPrice));

                        if(bitmap!=null){

                            // Convert it to byte
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            //Compress image to lower quality scale 1 - 100

                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] imageBytes = stream.toByteArray();

                            String imageContentType = getImageContentType(imageUri);

                            // Create the ParseFile
                            String imageName = editBarcode.getText().toString() + "." + imageContentType;


                        ParseFile file = new ParseFile(imageName, imageBytes, imageContentType);

                        // Upload the image into Parse Cloud
                        file.saveInBackground();

                        inventoryItem.setItemImg(file);

                        }

                        inventoryItem.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    resetEditTexts();
                                    SweetAlerts.showSuccessMsg(Store_Items.this, " Success! Item Saved");

                                } else {

                                    SweetAlerts.showErrorMsg(Store_Items.this, "Error! Try Again.");
                                }
                            }
                        });

                    } else {
                        SweetAlerts.showErrorMsg(Store_Items.this, "BAD");
                    }




            }
        });

    }


    private String getImageContentType(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String imageContentType = mime.getExtensionFromMimeType(cr.getType(uri));
        return imageContentType;
    }


    private void resetEditTexts() {

        editAmount.getText().clear();
        editEan.getText().clear();
        editDecription.getText().clear();
        editAmount.getText().clear();
        editCurAmountNstock.getText().clear();
        editPurchaseprice.getText().clear();
        editSellprice.getText().clear();
    }


    private void itemExistsCount(String barcode) {

        ParseQuery<InventoryItem> query = InventoryItem.getQuery(Prefs.getBusinessCvr());
        query.whereEqualTo("cvr", Prefs.getBusinessCvr());
        query.whereEqualTo("barcode", barcode);
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {

                if (e == null) {
                    if (inventoryItems != null && inventoryItems.size() > 0) {

                        inventoryItem = inventoryItems.get(0);

                        int amount = inventoryItem.getAmount();
                        String description = inventoryItem.getDescription();
                        double purchasePrice = inventoryItem.getPurchasePrice();
                        double sellPrice = inventoryItem.getSellPrice();
                        //ParseFile imageFile = inventoryItem.getItemImg();

                        editDecription.setText(description);
                        editPurchaseprice.setText(Double.toString(purchasePrice));
                        editSellprice.setText(Double.toString(sellPrice));
                        editCurAmountNstock.setText(amount+"");


                        ParseFile pf = inventoryItems.get(0).getItemImg();
                        if(pf!=null){
                            picasso.load(pf.getUrl()).into(imgItemPic);
                            itemImageExists=true;
                        }

                    } else {

                        //SweetAlerts.showErrorMsg(Store_Items.this, "Items Does Not Exists");
                    }

                }


            }
        });

    }


    private void scanItem(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if (scanningResult != null) {

            scanBarcode = scanningResult.getContents();
            scanEanType = scanningResult.getFormatName();
            editEan.setText(scanEanType);
            editBarcode.setText(scanBarcode);
            editDecription.requestFocus();

        } else {

        }
    }



    private void cropImage(Uri sourceUri) {

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(sourceUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 2);
        cropIntent.putExtra("aspectY", 2);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 160);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, PictureDialog.REQUEST_CROP_IMAGE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
       scanItem(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
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

                        imgItemPic.setImageBitmap(bitmap);

                        SweetAlerts.showBasicMsg(Store_Items.this, bitmap.getWidth() + ", " + bitmap.getHeight());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    @Override
    public void onColorSelected(int i) {

    }
}

