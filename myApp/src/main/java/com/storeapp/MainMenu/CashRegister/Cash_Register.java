package com.storeapp.MainMenu.CashRegister;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.storeapp.BaseActivity;
import com.storeapp.Db.CartItemsManager;
import com.storeapp.Db.DbManager;
import com.storeapp.R;
import com.storeapp.model.CartItem;
import com.storeapp.ui.FloatingEditText;
import com.storeapp.ui.sweetalert.SweetAlertDialog;
import com.storeapp.util.ParseNumber;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;
import java.util.Random;

public class Cash_Register extends BaseActivity {

    private ListView itemsListView;
    ImageButton btnCancelExp, btnbackArrow;
    private static FloatingEditText ediTxtTotal, ediTxtrecieved;
    Random rn = new Random();
    public static final String TOTAL_TO_PAY_AMOUNT = "totaltopayamount";
    public static final String RECEIVED_AMOUNT = "receivedamouny";
    CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_register);

        getActionBar().hide();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        itemsListView = (ListView) findViewById(R.id.lvItems);

        View header = getLayoutInflater().inflate(
                R.layout.cash_register_list_item_header, null);
        itemsListView.addHeaderView(header);
        TextView txtCartEmpty = (TextView) findViewById(R.id.empty);

        itemsListView.setEmptyView(txtCartEmpty);
        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {

                final int itemId = (int) id;

                new SweetAlertDialog(Cash_Register.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No,cancel plx!")
                        .setConfirmText("Yes,delete it!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("Sell it :)")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);


                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                int deleted = cartItemsManager.deleteItem(itemId);
                                if (deleted > 0) {
                                    sDialog.setTitleText("Deleted!")
                                            .setContentText("Item Successfully deleted!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    loadItems();
                                }

                            }
                        })
                        .show();


                return true;
            }
        });


        // Edittexts
        ediTxtTotal = (FloatingEditText) findViewById(R.id.ediTxtTotal);
        ediTxtrecieved = (FloatingEditText) findViewById(R.id.ediTxtrecieved);
        ediTxtrecieved.setText("0");


        btnbackArrow = (ImageButton) findViewById(R.id.btnbackArrow);
        btnbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cancelExpedition();
        loadItems();

    }


    private void cancelExpedition(){
        btnCancelExp = (ImageButton) findViewById(R.id.btnCancelExp);
        btnCancelExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbManager.getDbManager().getCartItemsManager().clearCart();
                clearFields();
                loadItems();
            }
        });

    }

    public void ctrlButtons(View v) {

        Button clickedButton = (Button) v;
        double curRecieved = 0;

        switch (clickedButton.getId()) {

            case R.id.btnPayment:

                String totalToPay= ediTxtTotal.getText().toString();
                String receivedAmount = ediTxtrecieved.getText().toString();
                if(Utils.isNullOrEmpty(totalToPay) || Utils.isNullOrEmpty(receivedAmount)){

                    SweetAlerts.showBasicMsg(Cash_Register.this, "Enter The Received Amount");
                    return;
                }


                double totalPayment = Double.parseDouble(totalToPay);
                double receivedPayment = Double.parseDouble(receivedAmount);
                if(totalPayment>0){
                    if(receivedPayment>=totalPayment){

                        Intent intent = new Intent(Cash_Register.this, PaymentDialog.class);
                        intent.putExtra(TOTAL_TO_PAY_AMOUNT, totalPayment);
                        intent.putExtra(RECEIVED_AMOUNT, receivedPayment);
                        startActivity(intent);

                    }else {

                        SweetAlerts.showErrorMsg(Cash_Register.this,"Not Enough Received Amount!");

                    }

                }else{
                    SweetAlerts.showErrorMsg(Cash_Register.this, "Cart Is Empty");
                }




                break;


            case R.id.btnScanItem:

                if (v.getId() == R.id.btnScanItem) {
                    // scan

                   Intent intent = new Intent(Cash_Register.this, ScanItemAmountDialog.class);
                   startActivity(intent);

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


    public void clearFields(){
        ediTxtTotal.setText("0.0");
        ediTxtrecieved.setText("0");
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


    @Override
    protected void onPause() {
        super.onPause();

    }

    public  void loadItems() {
        Cursor cursor = cartItemsManager.getItemsInCart();

        ItemsAdapter itemsAdapter = new ItemsAdapter(cursor, this);
        itemsListView.setAdapter(itemsAdapter);

        double currentTotal=0.0;
        final List<CartItem> itemsInCartList = cartItemsManager.getAllItemsInCart();
        if (itemsInCartList.size() > 0) {

            for (CartItem item : itemsInCartList) {

                currentTotal += item.getAmount() * item.getSellPrice();
            }
        }
        ediTxtTotal.setText(currentTotal+"");
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
            return cursor.getLong(cursor.getColumnIndex(CartItemsManager.COL_ID));
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
                    .getColumnIndex(CartItemsManager.COL_AMOUNT));
            String desc = cursor.getString(cursor
                    .getColumnIndex(CartItemsManager.COL_DESCRIPTION));
            double price = cursor.getDouble(cursor
                    .getColumnIndex(CartItemsManager.COL_SELL_PRICE));

            txtCount.setText(Integer.toString(count));
            txtDescription.setText(desc);

            txtPrice.setText(Double.toString(price * count));
            int colorItem = rn.nextInt(Utils.COLORFUL_COLORS.length- 1 + 1) + 0;
            convertView.setBackgroundColor(Utils.COLORFUL_COLORS[colorItem]);
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView txtCount, txtDescription, txtPrice;

    }


}
