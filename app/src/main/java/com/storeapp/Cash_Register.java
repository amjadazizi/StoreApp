package com.storeapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.storeapp.db.CartManager;
import com.storeapp.db.DbManager;
import com.storeapp.db.ItemsManager;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.util.ParseNumber;
import com.storeapp.util.Utils;

public class Cash_Register extends BaseActivity {

	ArrayList<String> itemsArray = new ArrayList<String>();

	private String scanBarcode;
	private double totalValue;
	private double recievedValue;
	private String amountItems;

	private LinearLayout layoutItmScanBtns, layoutRecieved, layoutPaymentBtns,
			layoutAmountBtns;
	private ListView itemsListView;

	private TextView txtTotal;
	private EditText ediTxtTotal, ediTxtrecieved, ediTxtNumItems;

	CartManager cartManager = DbManager.getDbManager().getCartManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_register);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		itemsListView = (ListView) findViewById(R.id.lvItems);

		View header = getLayoutInflater().inflate(
				R.layout.cash_register_list_item_header, null);
		itemsListView.addHeaderView(header);
		TextView txtCartEmpty = (TextView) findViewById(R.id.empty);

		itemsListView.setEmptyView(txtCartEmpty);

		// Edittexts
		ediTxtTotal = (EditText) findViewById(R.id.ediTxtTotal);
		ediTxtrecieved = (EditText) findViewById(R.id.ediTxtrecieved);
		ediTxtNumItems = (EditText) findViewById(R.id.ediTxtNumItems);

		// TextViews
		txtTotal = (TextView) findViewById(R.id.txtTotal);

		// Layouts
		layoutItmScanBtns = (LinearLayout) findViewById(R.id.layoutItmScanBtns);
		layoutRecieved = (LinearLayout) findViewById(R.id.layoutRecieved);
		layoutAmountBtns = (LinearLayout) findViewById(R.id.layoutAmountBtns);
		layoutPaymentBtns = (LinearLayout) findViewById(R.id.layoutPaymentBtns);
	}

	private void clearCart() {
		DbManager.getDbManager().getCartManager().clearCart();
		loadItems();
	}

	public void ctrlButtons(View v) {

		Button clickedButton = (Button) v;
		double curRecieved = 0;

		switch (clickedButton.getId()) {
		case R.id.btnEksped:
			layoutItmScanBtns.setVisibility(View.GONE);
			layoutRecieved.setVisibility(View.VISIBLE);
			layoutAmountBtns.setVisibility(View.VISIBLE);
			layoutPaymentBtns.setVisibility(View.VISIBLE);
			break;

		case R.id.btn50:
			curRecieved = 50;
			break;

		case R.id.btn100:
			curRecieved = 100;
			break;

		case R.id.btn200:
			curRecieved = 200;
			break;

		case R.id.btn500:
			curRecieved = 500;
			break;

		case R.id.btnCCard:
			if (!Utils.isNullOrEmpty(ediTxtrecieved.getText().toString())) {

				calcExpedtion();
			}

			break;

		case R.id.btnCheck:
			if (!Utils.isNullOrEmpty(ediTxtrecieved.getText().toString())) {
				calcExpedtion();

			}
			break;

		case R.id.btnCash:

			if (!Utils.isNullOrEmpty(ediTxtrecieved.getText().toString())) {
				calcExpedtion();
			}
			break;

		case R.id.btnNewScan:

			if (layoutItmScanBtns.getVisibility() == View.VISIBLE) {
				layoutItmScanBtns.setVisibility(View.GONE);
			} else {
				layoutItmScanBtns.setVisibility(View.VISIBLE);

			}
			break;

		case R.id.btnScanItem:

			if (v.getId() == R.id.btnScanItem) {
				// scan
				amountItems = ediTxtNumItems.getText().toString();
				IntentIntegrator scanIntegrator = new IntentIntegrator(this);
				scanIntegrator.initiateScan();
				if (layoutRecieved.getVisibility() == View.GONE) {
					layoutRecieved.setVisibility(View.VISIBLE);
				}
				ediTxtTotal.setText("");
			}
			break;

		case R.id.btnClearAll:

			if (v.getId() == R.id.btnClearAll) {

				ediTxtTotal.setText("");
				txtTotal.setText(getResources().getString(R.string.text_total));
				clearCart();
				newsSale();
				layoutRecieved.setVisibility(View.VISIBLE);

			}
			break;

		}

		// Update received amount
		double totalAmountReceived = curRecieved;
		ParseNumber parseNumber = new ParseNumber();
		if (parseNumber.tryParseDouble(ediTxtrecieved.getText().toString())) {
			totalAmountReceived += parseNumber.getValue();
		}
		ediTxtrecieved.setText(Double.toString(totalAmountReceived));
	}

	public void calcExpedtion() {

		totalValue = Double.parseDouble(ediTxtTotal.getText().toString());
		recievedValue = Double.parseDouble(ediTxtrecieved.getText().toString());

		if (recievedValue >= totalValue) {

			ediTxtTotal.setText(Double.toString(recievedValue - totalValue));

			txtTotal.setText(getResources()
					.getString(R.string.text_to_pay_back));
			clearCart();
			newsSale();
			SoundEffects.playCashRegisterSound();

		} else {
			Utils.showToastShort(getResources().getString(
					R.string.text_customer_has_to_pay_more));

		}
	}

	public void newsSale() {

		ediTxtrecieved.setText("");
		ediTxtNumItems.setText("1");
		layoutItmScanBtns.setVisibility(View.VISIBLE);
		layoutRecieved.setVisibility(View.GONE);
		layoutAmountBtns.setVisibility(View.GONE);
		layoutPaymentBtns.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {

		case android.R.id.home:
			finish();
			break;

		}

		return (super.onOptionsItemSelected(menuItem));
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve scan result

		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null) {
			// we have a result
			scanBarcode = scanningResult.getContents();
			ItemsManager itemManager = DbManager.getDbManager()
					.getItemsManager();

			int itemCount = itemManager.getItmCount(scanBarcode);
			if (itemCount >= Integer.parseInt(ediTxtNumItems.getText()
					.toString())) {

				if (!cartManager.itemExists(scanBarcode)) {

					int rowId = -1;
					rowId = cartManager.addItemToCart(scanBarcode, Integer
							.parseInt(ediTxtNumItems.getText().toString()));
					loadItems();
					if (rowId != -1) {
						// Utils.showToastShort("Item Added!");
						ediTxtNumItems.setText("1");

					} else {
						Utils.showToastShort("Error");

					}
				} else {

					int rowsAcffected = 0;
					rowsAcffected = cartManager.updtaeCartItem(scanBarcode,
							Integer.parseInt(ediTxtNumItems.getText()
									.toString()));
					loadItems();
					if (rowsAcffected > 0) {

						Utils.showToastShort(getResources().getString(
								R.string.text_item_already_updated));
						ediTxtNumItems.setText("1");

					}
					Utils.showToastShort(getResources().getString(
							R.string.text_item_already_scanned));
					ediTxtNumItems.setText("1");

				}

				ItemsManager itemsManager = DbManager.getDbManager()
						.getItemsManager();

				itemsManager.decreaseItemAmount(scanBarcode,
						Integer.parseInt(amountItems));

			} else {

				Utils.showToastShort(getResources().getString(
						R.string.text_not_enough)
						+ itemCount);

			}

		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private void loadItems() {
		Cursor cursor = cartManager.getItemsInCart();

		ItemsAdapter itemsAdapter = new ItemsAdapter(cursor, this);
		itemsListView.setAdapter(itemsAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadItems();
	}

	private class ItemsAdapter extends BaseAdapter {

		private Cursor cursor;
		private Context ctx;

		public ItemsAdapter(Cursor cursor, Context ctx) {
			this.cursor = cursor;
			this.ctx = ctx;
		}

		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			cursor.moveToPosition(position);
			return cursor.getLong(cursor.getColumnIndex(CartManager.COL_ID));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView txtCount, txtDescription, txtPrice;

			if (convertView == null) {
				LayoutInflater inf = LayoutInflater.from(ctx);
				convertView = inf.inflate(R.layout.cash_register_list_item,
						parent, false);

				txtCount = (TextView) convertView.findViewById(R.id.txtCount);
				txtDescription = (TextView) convertView
						.findViewById(R.id.txtDescription);
				txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

				ViewHolder holder = new ViewHolder();
				holder.txtCount = txtCount;
				holder.txtDescription = txtDescription;
				holder.txtPrice = txtPrice;

				convertView.setTag(holder);

			} else {
				ViewHolder holder = (ViewHolder) convertView.getTag();
				txtCount = holder.txtCount;
				txtDescription = holder.txtDescription;
				txtPrice = holder.txtPrice;
			}

			cursor.moveToPosition(position);
			int count = cursor.getInt(cursor
					.getColumnIndex(CartManager.COL_AMOUNT));
			String desc = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_DESCRIPTION));
			double price = cursor.getDouble(cursor
					.getColumnIndex(ItemsManager.COL_SELLPRICE));

			txtCount.setText(Integer.toString(count));
			txtDescription.setText(desc);

			txtPrice.setText(Double.toString(price * count));

			if (!Utils.isNullOrEmpty(ediTxtTotal.getText().toString())) {
				double currentTotal = Double.parseDouble(ediTxtTotal.getText()
						.toString());
				String totalPrice = Double.toString((price * count)
						+ currentTotal);

				ediTxtTotal.setText(totalPrice);
			} else {
				String totalPrice2 = Double.toString((price * count));

				ediTxtTotal.setText(totalPrice2);
			}
			return convertView;
		}
	}

	public static class ViewHolder {
		public TextView txtCount, txtDescription, txtPrice;

	}

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_cash_register;
	}
}
