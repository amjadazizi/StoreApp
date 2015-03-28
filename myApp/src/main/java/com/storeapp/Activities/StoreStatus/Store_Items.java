package com.storeapp.Activities.StoreStatus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.ColorSelector.OnColorSelectedListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.storeapp.Activities.ExternalClasses.FloatingEditText;
import com.storeapp.R;
import com.storeapp.Activities.parse.InventoryItem;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.util.Picture;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class Store_Items extends Activity implements OnColorSelectedListener{

    int backgroundColor = Color.parseColor("#1E88E5");
    FloatingActionButton add1;

private static final int ACTIVITY_PHOTOS = 0;
    private static final String PACKAGE = "spine";

    Uri mCapturedImageURI;
    private int photo_count = 0;
    boolean hasPhotos = false;
    Bitmap bitmap;
    String[] paths;
    String  imageFilePath;
    ImageView  imgItemPic;
    ImageButton btnScanItem;
    String scanBarcode;
    String scanEanType;
    boolean itemExist;
    FloatingEditText editBarcode, editEan, editDecription, editAmount, editPurchaseprice, editSellprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_items);



        add1 = (FloatingActionButton) findViewById(R.id.buttonColorSelector);

        add1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                Intent intent = new Intent(Store_Items.this, Picture.class);
                startActivityForResult(intent, Picture.REQUEST_TAKE_PICTURE);



            }

        });


        editBarcode = (FloatingEditText) findViewById(R.id.editBarcode);
        editEan = (FloatingEditText) findViewById(R.id.editEan);
        editDecription = (FloatingEditText) findViewById(R.id.editDecription);
        editAmount = (FloatingEditText) findViewById(R.id.editAmount);
        editPurchaseprice = (FloatingEditText) findViewById(R.id.editPurchaseprice);
        editSellprice = (FloatingEditText) findViewById(R.id.editSellprice);
        imgItemPic = (ImageView) findViewById(R.id.imgItemPic);
        editBarcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                     // Her Mister den focus:
          itemExists(editBarcode.getText().toString());





                }else if(hasFocus) {


                }
            }
        });

        btnScanItem = (ImageButton) findViewById(R.id.btnScanItem);
        btnScanItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(Store_Items.this);
                scanIntegrator.initiateScan();
            }
        });



        ButtonRectangle btnSaveItem = (ButtonRectangle) findViewById(R.id.btnSaveItem);
        btnSaveItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {





                InventoryItem inventoryItem = new InventoryItem();

                String message =null;

                String barcode = editBarcode.getText().toString();
                String eanType = editEan.getText().toString();
                String description = editDecription.getText().toString();
                String amount = editAmount.getText().toString();
                String purchasePrice = editPurchaseprice.getText().toString();
                String sellPrice =  editSellprice.getText().toString();

                if(!Utils.isNullOrEmpty(barcode) &&
                   !Utils.isNullOrEmpty(description)&&
                   !Utils.isNullOrEmpty(amount)&&
                   !Utils.isNullOrEmpty(purchasePrice)&&
                   !Utils.isNullOrEmpty(sellPrice))

                {
                    inventoryItem.setCvrNumber(Prefs.getBusinessCvr());
                    inventoryItem.setBarcode(barcode);
                    inventoryItem.setEanType(eanType);
                    inventoryItem.setDescription(description);
                    inventoryItem.setAmount(Integer.parseInt(amount));
                    inventoryItem.setPurchasePrice(Double.parseDouble(purchasePrice));
                    inventoryItem.setSellPrice(Double.parseDouble(sellPrice));
                    Drawable itemImage = imgItemPic.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)itemImage).getBitmap();


                    // Convert it to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                    ParseFile file = new ParseFile(editBarcode.getText().toString()+".png", image);
                    // Upload the image into Parse Cloud
                    file.saveInBackground();

                    inventoryItem.setItemImg(file);

                    inventoryItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null){
                                SweetAlerts.showErrorMsg(Store_Items.this, "Error! Item Image Upload Failed!");
                            }

                        }
                    });


                    inventoryItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){

                                SweetAlerts.showSuccessMsg(Store_Items.this, " Success! Item Saved");

                            }else{

                                SweetAlerts.showErrorMsg(Store_Items.this, "Error! Try Again.");
                            }
                        }
                    });



                    SweetAlerts.showSuccessMsg(Store_Items.this, "GODT");


                }else{


                    SweetAlerts.showErrorMsg(Store_Items.this, "BAD");



            }
            }
        });


          //add1 = (Button) findViewById(R.id.add1);
        //    img1 = (ImageView) findViewById(R.id.imgItemPic);


        //add1 = (FloatingActionButton)findViewById(R.id.imageButton1);


    }


    private void resetEditTexts(){

        editAmount.getText().clear();
        editEan.getText().clear();
        editDecription.getText().clear();
        editAmount.getText().clear();
        editPurchaseprice.getText().clear();
        editSellprice.getText().clear();
    }


    private void itemExists(String barcode){

        ParseQuery<InventoryItem> query = InventoryItem.getQuery();
        query.whereEqualTo("barcode", barcode);
       // query.whereEqualTo("cvr", Prefs.getBusinessCvr());
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {

                if (e == null) {
                    if(inventoryItems!=null && inventoryItems.size()>0){

                        int amount = inventoryItems.get(0).getAmount();
                        String description = inventoryItems.get(0).getDescription();
                        double purchasePrice = inventoryItems.get(0).getPurchasePrice();
                        double sellPrice = inventoryItems.get(0).getSellPrice();

                        editDecription.setText(description);
                        editPurchaseprice.setText(Double.toString(purchasePrice));
                        editSellprice.setText(Double.toString(sellPrice));


                    }else{

                        SweetAlerts.showErrorMsg(Store_Items.this, "Items Does Not Exists");
                    }


                }


            }
        });

    }



    private void scanItem(int requestCode, int resultCode, Intent data){
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        scanItem(requestCode,resultCode, data);

        if(resultCode==RESULT_OK){
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
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imgItemPic.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;



            }
        }
    }

    /* //////////////////////////////////////////////////////////////////////////
    private File savebitmap(Bitmap bmp) {
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
    }

    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(Store_Items.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    //pic = f;

                    startActivityForResult(intent, 1);


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

    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                //h=0;
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        //pic = photo;
                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    imgItemPic.setImageBitmap(bitmap);




                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    //p = path;

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        //pic=file;
                        outFile.flush();

                        outFile.close();


                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {



                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));


                Log.w("path of image from gallery......******************.........", picturePath+"");


                imgItemPic.setImageBitmap(thumbnail);

            }

        }

    }

        //////////////////////////////////////////////////////////////////////////*/


    private void setOriginRiple(final LayoutRipple layoutRipple){

        layoutRipple.post(new Runnable() {

            @Override
            public void run() {
                View v = layoutRipple.getChildAt(0);
                layoutRipple.setxRippleOrigin(ViewHelper.getX(v)+v.getWidth()/2);
                layoutRipple.setyRippleOrigin(ViewHelper.getY(v)+v.getHeight()/2);

                layoutRipple.setRippleColor(Color.parseColor("#1E88E5"));

                layoutRipple.setRippleSpeed(30);
            }
        });

    }

    @Override
    public void onColorSelected(int color) {
        backgroundColor = color;
        add1.setBackgroundColor(color);
    }


}
/*
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.storeapp.ActivitiesOld.BaseActivity;
import com.storeapp.R;
import com.storeapp.db.DbManager;
import com.storeapp.db.ItemsManager;
import com.storeapp.model.Item;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.util.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Store_Items extends BaseActivity implements View.OnClickListener {

	public static final String EXTRA_AMOUNT = "extra_amount";
	public static final String EXTRA_BAR_CODE = "extra_bar_code";
	public static final String EXTRA_EAN_TYPE = "extra_ean_type";

	private static final String VIEW_1_VISIBILITY = "view_1_visibility";
	private static final String BNT_STORE_ITEM_VISIBILITY = "btn_store_item_visibility";

	private static final String BTN_REGISTER_VISIBILITY = "btn_register_visibility";
	DateFormat format = DateFormat.getDateInstance();

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();

	ItemsManager itemManager = DbManager.getDbManager().getItemsManager();

	protected static final int DATE_DIALOG_ID_1 = 0;
	private EditText editDescrip, editPurPrice, editSellPrice, editExpDate,
			stAmountTxt, stEdtxtBarcode, stEdtxtEanType;
	private Button btnRegistItem, btnExpDate, scanBtn, storeItem;
	private String selectedDate;
	private String scanBarcode;
	private String scanEanType;
	private String amount, barcode, description, purprice, sellprice, eantype,
			expdate;
	private LinearLayout itmInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_items);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		stEdtxtBarcode = (EditText) findViewById(R.id.stEdtxtBarcode);
		stEdtxtEanType = (EditText) findViewById(R.id.stEdtxtEanType);
		stAmountTxt = (EditText) findViewById(R.id.stAmountTxt);
		editDescrip = (EditText) findViewById(R.id.editDescrip);

		editPurPrice = (EditText) findViewById(R.id.editPurPrice);
		editSellPrice = (EditText) findViewById(R.id.editSellPrice);
		scanBtn = (Button) findViewById(R.id.scanBarcode);
		scanBtn.setOnClickListener(this);
		btnRegistItem = (Button) findViewById(R.id.btnRegistItem);
		btnRegistItem.setVisibility(View.GONE);
		editExpDate = (EditText) findViewById(R.id.editExpDate);
		btnExpDate = (Button) findViewById(R.id.btnExpDate);

		itmInfo = (LinearLayout) findViewById(R.id.itmInfo);
		itmInfo.setVisibility(View.GONE);

		if (savedInstanceState != null) {
			itmInfo.setVisibility(savedInstanceState.getInt(VIEW_1_VISIBILITY));
			btnRegistItem.setVisibility(savedInstanceState
					.getInt(BTN_REGISTER_VISIBILITY));

		}
		storeItem = (Button) findViewById(R.id.storeItem);
		storeItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				barcode = stEdtxtBarcode.getText().toString();
				amount = stAmountTxt.getText().toString();
				boolean itemExists = itemManager.itemExists(barcode);
				if (!Utils.isNullOrEmpty(stAmountTxt.getText().toString())
						&& !Utils.isNullOrEmpty(barcode)) {

					if (itemExists) {

						int oldAmount = itemManager.getItmCount(barcode);
						Utils.showToastShort("Item Already Registered Current Amount= "
								+ oldAmount);

						itemManager.increaseItmAmount(barcode,
								Integer.parseInt(amount));
						int newAmount = itemManager.getItmCount(barcode);

						Utils.showToastShort("New Amount= " + newAmount);

						stEdtxtBarcode.setText("");
						stEdtxtEanType.setText("");
					} else {
						storeItem.setVisibility(View.GONE);
						itmInfo.setVisibility(View.VISIBLE);
						Utils.showToastLong("New Item. Please Register It");

					}
				} else {
					Utils.showToastLong("Please Scan The Item");
				}

			}
		});

		btnExpDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				barcode = stEdtxtBarcode.getText().toString();
				amount = stAmountTxt.getText().toString();
				eantype = stEdtxtEanType.getText().toString();
				description = editDescrip.getText().toString();
				purprice = editPurPrice.getText().toString();
				sellprice = editSellPrice.getText().toString();

				if (!Utils.isNullOrEmpty(stAmountTxt.getText().toString())
						&& !Utils.isNullOrEmpty(barcode)
						&& !Utils.isNullOrEmpty(description)
						&& !Utils.isNullOrEmpty(purprice)
						&& !Utils.isNullOrEmpty(sellprice)) {
					setDate();
					btnRegistItem.setVisibility(View.VISIBLE);

				} else {
					Utils.showToastLong("Please Enter All Info");
				}
			}
		});

		btnRegistItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Item item = new Item();
				expdate = editExpDate.getText().toString();

				item.setAmount(Integer.parseInt(amount));
				item.setEanType(eantype);
				item.setBarcode(barcode);
				item.setDescription(description);
				item.setPurchPrice(Double.parseDouble(purprice));
				item.setSellPrice(Double.parseDouble(sellprice));
				item.setExpDate(expdate);

				int itemId = itemManager.storeItem(item);
				if (itemId > 0) {
					Utils.showToastLong("Item Stored");

					clearAll();
				} else {
					Utils.showToastLong("Error. Please Try Again");
				}

			}
		});

	}

	public void clearAll() {

		stAmountTxt.setText("1");
		stEdtxtEanType.setText("");
		editDescrip.setText("");
		stEdtxtBarcode.setText("");
		editPurPrice.setText("");
		editSellPrice.setText("");
		editExpDate.setText("");
		storeItem.setVisibility(View.VISIBLE);
		itmInfo.setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// overridePendingTransition(R.anim.activity_open_scale,
		// R.anim.activity_close_translate);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case R.id.clearAll:
			clearAll();
			break;

		case android.R.id.home:
			this.finish();
			break;

		}

		return super.onOptionsItemSelected(menuItem);
	}

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			String month = String.format("%02d", monthOfYear + 1);
			String day = String.format("%02d", dayOfMonth);
			selectedDate = String.format("%d-%s-%s", year, month, day);

			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updatedate();

		}
	};

	public void updatedate() {
		editExpDate.setText(format.format(calendar.getTime()));

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// finish();

	}

	public void setDate() {
		new DatePickerDialog(Store_Items.this, d, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.scanBarcode) {
			// scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.store_items_bar, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null) {
			// we have a result

			scanBarcode = scanningResult.getContents();
			scanEanType = scanningResult.getFormatName();
			stEdtxtEanType.setText(scanEanType);
			stEdtxtBarcode.setText(scanBarcode);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(VIEW_1_VISIBILITY, itmInfo.getVisibility());
		outState.putInt(BTN_REGISTER_VISIBILITY, btnRegistItem.getVisibility());
		outState.putInt(BNT_STORE_ITEM_VISIBILITY, storeItem.getVisibility());

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		itmInfo.setVisibility(savedInstanceState.getInt(VIEW_1_VISIBILITY));
		btnRegistItem.setVisibility(savedInstanceState
				.getInt(BTN_REGISTER_VISIBILITY));
		storeItem.setVisibility(savedInstanceState
				.getInt(BNT_STORE_ITEM_VISIBILITY));

	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_store_items;

	}

	public String getFullClassName() {
		return Store_Items.class.getName();
	}

}
*/
