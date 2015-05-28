package com.storeapp.MainMenu.CashRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Db.CartItemsManager;
import com.storeapp.Db.DbManager;
import com.storeapp.R;
import com.storeapp.model.CartItem;
import com.storeapp.parse.InventoryItem;
import com.storeapp.qrreader.IntentIntegrator;
import com.storeapp.qrreader.IntentResult;
import com.storeapp.ui.FloatingEditText;
import com.storeapp.util.Prefs;
import com.storeapp.util.SweetAlerts;
import com.storeapp.util.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by Amjad on 21-03-2015.
 */
public class ScanItemAmountDialog extends Activity {

    ImageButton btnMinus, btnPlus;
    Button btnScanCancel,btnScanItem;
    FloatingEditText editCashDiaAmount;
    int currentItemAmount ;
    private String scanBarcode;
    Random rn = new Random();

    CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.cahsregister_scan_item_amount_dialog);
        this.setTitle("Item Amount");
        setTitleColor(Color.BLACK);

        editCashDiaAmount = (FloatingEditText) findViewById(R.id.editCashDiaAmount);

        btnMinus = (ImageButton) findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isNullOrEmpty(editCashDiaAmount.getText().toString())){
                    SweetAlerts.showBasicMsg(ScanItemAmountDialog.this,"Enter The Amount");
                    return;
                }
                currentItemAmount = Integer.parseInt(editCashDiaAmount.getText().toString());
                if(currentItemAmount<=1){
                    return;
                }
                currentItemAmount--;
                editCashDiaAmount.setText(currentItemAmount+"");
            }
        });

        btnPlus = (ImageButton) findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isNullOrEmpty(editCashDiaAmount.getText().toString())){
                    editCashDiaAmount.setText("1");
                    return;
                }
                currentItemAmount = Integer.parseInt(editCashDiaAmount.getText().toString());
                currentItemAmount++;
                editCashDiaAmount.setText(currentItemAmount + "");
            }
        });

        btnScanItem = (Button) findViewById(R.id.btnScanItem);
        btnScanItem.setText("Scan");
        btnScanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Utils.isNullOrEmpty(editCashDiaAmount.getText().toString())){
                    currentItemAmount = Integer.parseInt(editCashDiaAmount.getText().toString());
                    if(currentItemAmount<1 ){
                        SweetAlerts.showBasicMsg(ScanItemAmountDialog.this,"Enter The Amount");
                        return;
                    }

                    IntentIntegrator scanIntegrator = new IntentIntegrator(ScanItemAmountDialog.this);
                    scanIntegrator.initiateScan();

                }else {
                    SweetAlerts.showBasicMsg(ScanItemAmountDialog.this,"Enter The Amount");

                }
            }
        });


        btnScanCancel = (Button) findViewById(R.id.btnScanCancel);
        btnScanCancel.setText("Cancel");
        btnScanCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        if (scanningResult != null) {
            // we have a result
            scanBarcode = scanningResult.getContents();
            fetchItemInStore(scanBarcode);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void fetchItemInStore(String barcode) {

        final int colorItem = rn.nextInt(Utils.COLORFUL_COLORS.length- 1 + 1) + 0;

        ParseQuery<InventoryItem> query = InventoryItem.getQuery(Prefs.getBusinessCvr());
        query.whereEqualTo("barcode", barcode);
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {
                if (e == null) {
                    if (inventoryItems != null && inventoryItems.size() > 0) {

                       int itemScannedAmount =  Integer.parseInt(editCashDiaAmount.getText().toString());
                        if (inventoryItems.get(0).getAmount() >= itemScannedAmount) {
                            if (!cartItemsManager.itemExists(scanBarcode)) {
                                InventoryItem ii = inventoryItems.get(0);
                                CartItem cartItem = new CartItem();
                                cartItem.setParseInventoryItemId(ii.getObjectId());
                                cartItem.setBarcode(ii.getBarcode());
                                cartItem.setDescription(ii.getDescription());
                                cartItem.setAmount(itemScannedAmount);
                                cartItem.setSellPrice(ii.getSellPrice());
                                cartItem.setItemColor(colorItem);
                                int rowId = -1;
                                rowId = cartItemsManager.addItemToCart(cartItem);
                                if (rowId != -1) {
                                    Utils.showToastShort("Item Added!");
                                    finish();

                                } else {
                                    Utils.showToastShort("Error");

                                }
                            } else {

                                if (inventoryItems.get(0).getAmount() >= (cartItemsManager.getItmCount(scanBarcode) + itemScannedAmount)) {

                                    int rowsAcffected = 0;
                                    rowsAcffected = cartItemsManager.updateCartItemAmount(scanBarcode, itemScannedAmount);
                                    if (rowsAcffected > 0) {

                                        Utils.showToastShort("Item Updated! NEW COUNT");
                                           finish();
                                    }
                                } else {
                                    Utils.showToastShort("Not Enough In Store");
                                }

                            }

                        } else {

                            Utils.showToastShort(getResources().getString(
                                    R.string.text_not_enough)
                                    + inventoryItems.get(0).getAmount());

                        }


                    }
                }
            }
        });

    }

}


