package com.storeapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
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

import com.storeapp.db.DbManager;
import com.storeapp.db.ItemsManager;
import com.storeapp.model.Item;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.util.Utils;

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
