package com.storeapp.MainMenu.CashRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.storeapp.util.Utils;

import java.util.List;

/**
 * Created by Amjad on 21-03-2015.
 */
public class ScanItemAmountDialog extends Activity {

    Button btnMinus, btnPlus;
    Button btnCasgDiaCancel,btnCasgDiaAccept;
    FloatingEditText editCashDiaAmount;
    int currentItemAmount ;
    private String scanBarcode;
    CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.cahsregister_scan_item_amount_dialog);
        this.setTitle("Item Amount");
        setTitleColor(Color.BLACK);

        editCashDiaAmount = (FloatingEditText) findViewById(R.id.editCashDiaAmount);

        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItemAmount = Integer.parseInt(editCashDiaAmount.getText().toString());
                currentItemAmount--;
                editCashDiaAmount.setText(currentItemAmount+"");
            }
        });

        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItemAmount = Integer.parseInt(editCashDiaAmount.getText().toString());
                currentItemAmount++;
                editCashDiaAmount.setText(currentItemAmount + "");
            }
        });

        btnCasgDiaAccept = (Button) findViewById(R.id.btnCashDiaAccept);
        btnCasgDiaAccept.setText("Scan");
        btnCasgDiaAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator scanIntegrator = new IntentIntegrator(ScanItemAmountDialog.this);
                scanIntegrator.initiateScan();

            }
        });


        btnCasgDiaCancel = (Button) findViewById(R.id.btnCasgDiaCancel);
        btnCasgDiaCancel.setText("Cancel");
        btnCasgDiaCancel.setOnClickListener(new View.OnClickListener() {
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

        ParseQuery<InventoryItem> query = InventoryItem.getQuery(Prefs.getBusinessCvr());
        query.whereEqualTo("barcode", barcode);
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {

                if (e == null) {
                    if (inventoryItems != null && inventoryItems.size() > 0) {

                        //int itemAmount = itemScannedAmount;
                       int itemScannedAmount =  Integer.parseInt(editCashDiaAmount.getText().toString());
                        if (inventoryItems.get(0).getAmount() >= itemScannedAmount) {

                            if (!cartItemsManager.itemExists(scanBarcode)) {

                                InventoryItem ii = inventoryItems.get(0);

                                CartItem cartItem = new CartItem();
                                cartItem.setParseIventoryItemId(ii.getObjectId());
                                cartItem.setBarcode(ii.getBarcode());
                                cartItem.setDescription(ii.getDescription());
                                cartItem.setAmount(itemScannedAmount);
                                cartItem.setSellPrice(ii.getSellPrice());


                                int rowId = -1;
                                rowId = cartItemsManager.addItemToCart(cartItem);
                               // loadItems();
                                if (rowId != -1) {
                                    Utils.showToastShort("Item Added!");
                                    // ediTxtNumItems.setText("1");
                                    finish();

                                } else {
                                    Utils.showToastShort("Error");

                                }
                            } else {

                                if (inventoryItems.get(0).getAmount() >= (cartItemsManager.getItmCount(scanBarcode) + itemScannedAmount)) {

                                    int rowsAcffected = 0;
                                    rowsAcffected = cartItemsManager.updateCartItemAmount(scanBarcode, itemScannedAmount);
                                   // loadItems();
                                    if (rowsAcffected > 0) {

                                        Utils.showToastShort("Item Updated! NEW COUNT" + cartItemsManager.getItmCount(scanBarcode));
                                        //  ediTxtNumItems.setText("1");

                                    } else {
                                        Utils.showToastShort("Item Not Updated!");

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

