package com.storeapp.MainMenu.CashRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Db.CartItemsManager;
import com.storeapp.Db.DbManager;
import com.storeapp.R;
import com.storeapp.model.CartItem;
import com.storeapp.parse.InventoryItem;
import com.storeapp.parse.ParseDateComparing;
import com.storeapp.parse.SoldItem;
import com.storeapp.parse.TodaysTurnover;
import com.storeapp.util.Prefs;
import com.storeapp.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PaymentDialog extends Activity {

    Button btnPayCash, btnPayCreditCard, btnPaymentCancel;
    TextView txtInfoPayback;
    double totalToPayAmount;
    double receivedAmount;
    String  paymentMethod;
    Cash_Register cash_register = new Cash_Register();
    public static final String ACTION_PAYMENT_DONE = "com.storeapp.payment.done.intent.action";
    public static final String PAYMENT_METHOD = "payment_method";
    ParseDateComparing pdc= new ParseDateComparing();
    CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.cashregister_payemnt_dialog);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_monye_black);
        this.setTitle("Payment");
        setTitleColor(Color.BLACK);



        txtInfoPayback = (TextView) findViewById(R.id.txtInfoPayback);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            totalToPayAmount = bundle.getDouble(Cash_Register.TOTAL_TO_PAY_AMOUNT);
            receivedAmount = bundle.getDouble(Cash_Register.RECEIVED_AMOUNT);


        }



        btnPayCash = (Button) findViewById(R.id.btnPayCash);
        btnPayCash.setText("Cash");
        btnPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCashPayment(totalToPayAmount);
                endExpedition();

            }
        });

        btnPayCreditCard = (Button) findViewById(R.id.btnPayCreditCard);
        btnPayCreditCard.setText("Credit Card");
        btnPayCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerCreditPayemnt(totalToPayAmount);
                endExpedition();

            }
        });

        btnPaymentCancel = (Button) findViewById(R.id.btnPaymentCancel);
        btnPaymentCancel.setText("Cancel");
        btnPaymentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }





    private void registerCashPayment(final double cash){

        registerPaymentMethod(cash,0);

    }

    private void registerCreditPayemnt(final double creditCard){

        registerPaymentMethod(0, creditCard);

    }

    private void registerPaymentMethod(final double cash, final double creditCard){


        final ParseQuery<TodaysTurnover> todaysTurnoverParseQuery = TodaysTurnover.getQuery(Prefs.getBusinessCvr());
        if(creditCard>0){
             paymentMethod="Credit Card";
        }else {
            paymentMethod="Cash";
        }


        todaysTurnoverParseQuery.whereGreaterThanOrEqualTo (TodaysTurnover.TODAYS_TURNOVER_DATE , pdc.getFromDate() );
        todaysTurnoverParseQuery.whereLessThanOrEqualTo (TodaysTurnover.TODAYS_TURNOVER_DATE , pdc.getToDate() );

        todaysTurnoverParseQuery.findInBackground(new FindCallback<TodaysTurnover>() {
            @Override
            public void done(List<TodaysTurnover> todaysTurnovers, ParseException e) {
                if (e == null) {
                    if (todaysTurnovers != null && todaysTurnovers.size() > 0) {
                        todaysTurnovers.get(0).increment(TodaysTurnover.TODAYS_TURNOVER_CASH, cash);
                        todaysTurnovers.get(0).increment(TodaysTurnover.TODAYS_TURNOVER_CREDIR_CARD, creditCard);

                    }else{

                        Date d = Utils.getCurrentDate();

                        TodaysTurnover todaysTurnover = new TodaysTurnover();
                        todaysTurnover.setDate(new Date());
                        todaysTurnover.setCvr(Prefs.getBusinessCvr());
                        todaysTurnover.setCreditCard(creditCard);
                        todaysTurnover.setCash(cash);
                        todaysTurnover.saveInBackground();

                    }
                }else {

                }
                TodaysTurnover.saveAllInBackground(todaysTurnovers);

                Intent intent = new Intent(PaymentDialog.this, ReceiptActivity.class);
                intent.putExtra(PAYMENT_METHOD,paymentMethod );
                startActivity(intent);
                finish();


            }
        });

    }


    private void endExpedition() {

        final List<CartItem> itemsInCartList = cartItemsManager.getAllItemsInCart();
        if (itemsInCartList.size() <= 0) {
            return;
        }

        final HashMap<String, CartItem> hashMap = new HashMap<String, CartItem>();

        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>(itemsInCartList.size());
        for (CartItem item : itemsInCartList) {
            InventoryItem ii = InventoryItem.createWithoutData(InventoryItem.class, item.getParseInventoryItemId());
            inventoryItems.add(ii);

            //Save which cart item corresponds to which inventory item
            hashMap.put(item.getParseInventoryItemId(), item);
        }

        ParseQuery<SoldItem> siQuery = SoldItem.getQuery(Prefs.getBusinessCvr());
        siQuery.whereContainedIn(SoldItem.COL_INVENTORY_ITEM, inventoryItems);
        siQuery.findInBackground(new FindCallback<SoldItem>() {
            @Override
            public void done(List<SoldItem> soldItems, ParseException e) {
               if(soldItems.size() > 0) {
                   for (SoldItem soldItem : soldItems) {
                       CartItem cartItem = hashMap.get(soldItem.getInventoryItem().getObjectId());
                       //Decrement inventory item amount
                       soldItem.getInventoryItem().increment(InventoryItem.COL_AMOUNT, -cartItem.getAmount());
                       //Increment inventory item amount
                       soldItem.increment(soldItem.COL_AMOUNT, cartItem.getAmount());

                       //The ones we update here are removed from the original list so that we know that these
                       //must be added later below
                       itemsInCartList.remove(cartItem);
                   }
                   //Save updated items
                   SoldItem.saveAllInBackground(soldItems);
               }

                if(itemsInCartList.size() > 0) {
                    //Create new sold items and their inventory items
                    List<SoldItem> newSoldItems = new ArrayList<SoldItem>();
                    for (CartItem cartItem : itemsInCartList) {

                        InventoryItem ii = InventoryItem.createWithoutData(InventoryItem.class, cartItem.getParseInventoryItemId());
                        //Decrement inventory item amount
                        ii.increment(InventoryItem.COL_AMOUNT, -cartItem.getAmount());

                        SoldItem soldItem = new SoldItem();
                        soldItem.setAmount(cartItem.getAmount());
                        soldItem.setInventoryItem(ii);

                        newSoldItems.add(soldItem);
                    }
                    SoldItem.saveAllInBackground(newSoldItems);
                }


            }
        });


    }


}


