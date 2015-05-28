package com.storeapp.MainMenu.CashRegister;

        import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Db.CartItemsManager;
import com.storeapp.Db.DbManager;
import com.storeapp.R;
import com.storeapp.model.CartItem;
import com.storeapp.parse.Business;
import com.storeapp.util.Prefs;

import java.io.OutputStream;
import java.util.List;


public class ReceiptActivity extends Activity {

    double totalcartPrice;
    String paymentMethod;
    ImageButton btnShareReceipt,btnbackArrowReceipt;
    LinearLayout receipt_layout;
    boolean animationDone=false;
    TextView txtStoreName, txtStoreCvr, txtStorePhoneNumber, txtStoreEmail,
            txtReceiptPaymentMethod,txtReceiptTotalPrice, txtReceiptTotalReceived, txtReceiptReturnAmount;
    ListView listviewReceipt;
    ProgressBar receiptProgress;
    private static final String ANIMATION_DONE = "animation_done";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        getActionBar().hide();

        if(savedInstanceState!=null){
            animationDone = savedInstanceState.getBoolean(ANIMATION_DONE);
        }

        Bundle bundle  = getIntent().getExtras();
        if(bundle!=null){
            paymentMethod =   bundle.getString(PaymentDialog.PAYMENT_METHOD);
        }
         receiptProgress = (ProgressBar) findViewById(R.id.receiptProgress);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.receipt_layout);
        Animation translateAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.receipt);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                receiptProgress.setVisibility(View.GONE);
                btnShareReceipt.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(!animationDone){
            ll.startAnimation(translateAnimation);
            animationDone=true;
        }


        listviewReceipt = (ListView) findViewById(R.id.listviewReceipt);
        listviewReceipt.setSelector(android.R.color.transparent);
        txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        txtStoreCvr = (TextView) findViewById(R.id.txtStoreCvr);
        txtStorePhoneNumber = (TextView) findViewById(R.id.txtStorePhoneNumber);
        txtStoreEmail = (TextView) findViewById(R.id.txtStoreEmail);

        txtReceiptPaymentMethod = (TextView) findViewById(R.id.txtReceiptPaymentMethod);
        txtReceiptTotalPrice = (TextView) findViewById(R.id.txtReceiptTotalPrice);
        txtReceiptTotalReceived = (TextView) findViewById(R.id.txtReceiptTotalReceived);
        txtReceiptReturnAmount = (TextView) findViewById(R.id.txtReceiptReturnAmount);

        btnbackArrowReceipt = (ImageButton) findViewById(R.id.btnbackArrowReceipt);
        btnbackArrowReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbManager.getDbManager().getCartItemsManager().clearCart();
                finish();
            }
        });



        receipt_layout = (LinearLayout) findViewById(R.id.receipt_layout);
        btnShareReceipt = (ImageButton) findViewById(R.id.btnShareReceipt);
        btnShareReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap receipt = getBitmapFromView(receipt_layout);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    receipt.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));


            }
        });

        shopInfomation();
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.BLUE);
        view.draw(canvas);
        return returnedBitmap;
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DbManager.getDbManager().getCartItemsManager().clearCart();

    }


    private void shopInfomation(){

        ParseQuery<Business> businessParseQuery = Business.getQuery(Prefs.getBusinessCvr());
        businessParseQuery.findInBackground(new FindCallback<Business>() {
            @Override
            public void done(List<Business> businesses, ParseException e) {
                if(e==null){
                    if(businesses!=null && businesses.size()>0){
                        Business bus = businesses.get(0);
                        txtStoreName.setText(bus.getName());
                        txtStoreCvr.setText(bus.getCvr());
                        txtStorePhoneNumber.setText(bus.getPhoneNumber());
                        txtStoreEmail.setText(bus.getEmail());
                        generateReceipt();

                    }
                }
            }
        });

    }



    public void generateReceipt(){

        CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();
        List<CartItem> cartItemList = cartItemsManager.getAllItemsInCart();
        ReceiptAdapter receiptAdapter = new ReceiptAdapter(this, cartItemList);
        listviewReceipt.setAdapter(receiptAdapter);

        if(paymentMethod.equals("Cash")) {
            txtReceiptPaymentMethod.setText("Cash: ");
        }else {
            txtReceiptPaymentMethod.setText("Credit Card: ");

        }
        txtReceiptTotalReceived.setText(Prefs.getTotalReceviedAmount());
    }

    private class ReceiptAdapter extends BaseAdapter{

        Context ctx;
        List<CartItem> itemsList;

        public ReceiptAdapter(Context ctx,List<CartItem> itemsList ){

            this.ctx = ctx;
            this.itemsList = itemsList;

        }


        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            TextView itemsAmount, itemsDescription, itemsPrice;

            if(view == null){
                LayoutInflater layoutInflator = LayoutInflater.from(ctx);
                view = layoutInflator.inflate(R.layout.receipt_list_item, null);

                itemsAmount = (TextView) view
                        .findViewById(R.id.txtReceiptItemAmount);
                itemsDescription = (TextView) view
                        .findViewById(R.id.txtReceiptItemDescription);
                itemsPrice = (TextView) view
                        .findViewById(R.id.txtReceiptItemPrice);
                ViewHolder holder = new ViewHolder();
                holder.itemAmount = itemsAmount;
                holder.itemDescription = itemsDescription;
                holder.itemPrice = itemsPrice;
                view.setTag(holder);

            }else  {

            ViewHolder holder =(ViewHolder) view.getTag();
            itemsAmount = holder.itemAmount;
            itemsDescription = holder.itemDescription;
            itemsPrice = holder.itemPrice;

            }

            CartItem item = itemsList.get(position);
            itemsAmount.setText(item.getAmount()+"");
            itemsDescription.setText(item.getDescription());
            double itemTPrice = item.getSellPrice()*item.getAmount();
            totalcartPrice +=itemTPrice;
            itemsPrice.setText(itemTPrice+"");
            txtReceiptTotalPrice.setText(totalcartPrice+"");
            txtReceiptReturnAmount.setText("" +(totalcartPrice-(Double.parseDouble(Prefs.getTotalReceviedAmount()))));
            return view;

        }

    }




    public  static class ViewHolder{
        public TextView itemAmount, itemDescription, itemPrice;


    }
}



