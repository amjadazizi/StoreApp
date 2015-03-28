package com.storeapp.storestatusfragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.storeapp.Activities.parse.InventoryItem;
import com.storeapp.Activities.parse.SoldItem;
import com.storeapp.R;
import com.storeapp.util.Prefs;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Amjad on 20-03-2015.
 */
public class FragSoldItems extends Fragment{
    private View view;
    GridView soldItemsGridview;
    HashMap<String, ParseFile>  imageMap = new HashMap<String, ParseFile>();


    public FragSoldItems() {}

   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmen_items_sold_out, container,
                false);
       soldItemsGridview = (GridView) view.findViewById(R.id.soldItemsGridview);

       loadSoldItems();
        return view;
   }

    public void loadSoldItems(){



        ParseQuery<SoldItem> query = SoldItem.getQuery();
        query.whereEqualTo("cvr", Prefs.getBusinessCvr());
        query.findInBackground(new FindCallback<SoldItem>() {
            @Override
            public void done(List<SoldItem> soldItems, ParseException e) {
                if(e==null){
                    if(soldItems!=null && soldItems.size()>0){
                        SoldItemsAdapter soldItemsAdapter = new SoldItemsAdapter(getActivity(),soldItems );
                        soldItemsGridview.setAdapter(soldItemsAdapter);



                    }


                }

            }
        });


    }


    public void itemImage(){



        ParseQuery<InventoryItem> query = InventoryItem.getQuery();
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {

                if(e==null){
                    if(inventoryItems!=null && inventoryItems.size()>0){
                        for(int i =0; i<= inventoryItems.size();i++){
                            String   barcode = inventoryItems.get(i).getBarcode();
                            ParseFile imageFile = inventoryItems.get(i).getItemImg();
                           // hm.put(barcode, imageFile);

                        }

                    }
                }
            }
        });


    }



   /* private ParseFile getItemImage(String barcode){

        ParseFile imageFile = null;

        try {
            ParseQuery<InventoryItem> query = InventoryItem.getQuery();
            query.whereEqualTo("itemImage",barcode);
           imageFile = (ParseFile) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return imageFile;

       *//* query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> inventoryItems, ParseException e) {

                if(e==null){
                    if(inventoryItems!=null && inventoryItems.size()>0){
                 imageFile = (ParseFile) inventoryItems.get(0).getItemImg();
                    }
                }
            }
        });
*//*


    }*/


    public class SoldItemsAdapter extends BaseAdapter{

        List<SoldItem> listSoldItem;
         Context ctx;
        HashMap<String,ParseFile> imageFilesMap;

        public SoldItemsAdapter(Context ctx, List<SoldItem> listSoldItem){
            this.ctx=ctx;
            this.listSoldItem=listSoldItem;
        }

        @Override
        public int getCount() {
            return listSoldItem.size();
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

            TextView itemName;
            ImageView itemImage;

            if(view==null){

                LayoutInflater layoutInflater = LayoutInflater.from(ctx);
               view=  layoutInflater.inflate(R.layout.items_instock_grid_item, null);

                itemName = (TextView) view.findViewById(R.id.txtItemName);
                itemImage = (ImageView) view.findViewById(R.id.itemImage);


                ViewHolder holder = new ViewHolder();
                    holder.itemDecription = itemName;
                    holder.itemPicture = itemImage;

                view.setTag(holder);

            }else {

                ViewHolder holder = (ViewHolder) view.getTag();
                itemName = holder.itemDecription;
               itemImage = holder.itemPicture;

            }



            SoldItem soldItem = listSoldItem.get(position);
            String barcode= soldItem.getBarcode();
            itemName.setText(barcode);
/*

            try {
           //     ParseFile imageFile = getItemImage(barcode);
                byte[] imageBytes = imageFile.getData();
                imageBytes = imageFile.getData();
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                itemImage.setImageBitmap(imageBitmap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/




            return view;
        }
    }


    public  static  class  ViewHolder{

       public TextView itemDecription;
        public ImageView itemPicture;


    }

}
