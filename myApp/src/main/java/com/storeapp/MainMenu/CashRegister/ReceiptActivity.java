package com.storeapp.MainMenu.CashRegister;

        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
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

    TextView txtReceipt,txtStoreInfoReceipt;
    double totalcartPrice = 0;
    String shopInfo;
    String paymentMethod;
    ImageButton btnShareReceipt,btnbackArrowReceipt;
    LinearLayout receipt_layout;
    boolean animationDone=false;
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

        final LinearLayout ll = (LinearLayout) findViewById(R.id.receipt_layout);
        Animation translateAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.receipt);

        if(!animationDone){
            ll.startAnimation(translateAnimation);
            animationDone=true;
        }

        txtStoreInfoReceipt = (TextView) findViewById(R.id.txtStoreInfoReceipt);
        txtReceipt = (TextView) findViewById(R.id.txtReceipt);

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

    private void generateReceipt(){

        CartItemsManager cartItemsManager = DbManager.getDbManager().getCartItemsManager();
        List<CartItem> cartItemList = cartItemsManager.getAllItemsInCart();

        String tab = getString(R.string.tab) ;
        String tabs10 = tab+tab+tab+tab+tab+tab+tab+tab+tab+tab;
        String tabs5 = tab+tab+tab+tab+tab;

        String text = "Amount"+tabs5+"Description"+tabs5+"Price\n\n";


        for(CartItem cartItem :cartItemList ){
            text += cartItem.getAmount()+tabs10+cartItem.getDescription()+tabs10+cartItem.getAmount()*cartItem.getSellPrice()+"\n";
            totalcartPrice += cartItem.getAmount()*cartItem.getSellPrice();
        }
        String total;
        if(paymentMethod=="Cash") {
            total = ""+tabs10+"Total: "+tabs10+totalcartPrice+"\n";

        }{
            total =  ""+tabs10+"Total: "+tabs10+totalcartPrice+"\n"
                    +""+tabs10+paymentMethod+tabs10+totalcartPrice+"\n" +
                    "\n";
        }
        txtReceipt.setText(text+total);
        txtStoreInfoReceipt.setText(shopInfo);

    }


    private void shopInfomation(){

        ParseQuery<Business> businessParseQuery = Business.getQuery(Prefs.getBusinessCvr());
        businessParseQuery.findInBackground(new FindCallback<Business>() {
            @Override
            public void done(List<Business> businesses, ParseException e) {
                if(e==null){
                    if(businesses!=null && businesses.size()>0){
                        Business bus = businesses.get(0);
                        shopInfo = bus.getName()+"\n"+ bus.getCvr()+"\n"+bus.getPhoneNumber()+"\n"+bus.getEmail();
                        generateReceipt();
                    }
                }
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ANIMATION_DONE,animationDone);

    }
}



