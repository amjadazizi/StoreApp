package com.storeapp.MainMenu.StoreStatus.fragments_store_status;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.storeapp.MainMenu.EmployeeList.CustomImageView;
import com.storeapp.R;
import com.storeapp.parse.InventoryItem;
import com.storeapp.util.Prefs;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Amjad on 20-03-2015.
 */
public class FragSoldItems extends Fragment {
    private View view;
    GridView soldItemsGridview;
    HashMap<String, ParseFile> imageMap = new HashMap<String, ParseFile>();
    Picasso picasso;


    public FragSoldItems() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmen_items_sold_out, container,
                false);
        soldItemsGridview = (GridView) view.findViewById(R.id.soldItemsGridview);

        int largeMemoryInMegaBytes = ((ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;


        Picasso.Builder pb = new Picasso.Builder(getActivity());
        picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        picasso.setIndicatorsEnabled(true);




        loadSoldItems();
        return view;
    }

    public void loadSoldItems() {

        ParseQuery<InventoryItem> query = InventoryItem.getQuery(Prefs.getBusinessCvr());
        query.whereEqualTo(InventoryItem.COL_AMOUNT,0);
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> list, ParseException e) {

                if (e == null) {
                    if (list != null && list.size() > 0) {
                        SoldItemsAdapter soldItemsAdapter = new SoldItemsAdapter(getActivity(), list);
                        soldItemsGridview.setAdapter(soldItemsAdapter);


                    }
                }
            }
        });

    }





    public class SoldItemsAdapter extends BaseAdapter {

        List<InventoryItem> listSoldItem;
        Context ctx;
        public SoldItemsAdapter(Context ctx, List<InventoryItem> listSoldItem) {
            this.ctx = ctx;
            this.listSoldItem = listSoldItem;
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
            CustomImageView itemImage;

            if (view == null) {

                LayoutInflater layoutInflater = LayoutInflater.from(ctx);
                view = layoutInflater.inflate(R.layout.items_instock_grid_item, null);

                itemName = (TextView) view.findViewById(R.id.txtItemName);
                itemImage = (CustomImageView) view.findViewById(R.id.itemImage);


                ViewHolder holder = new ViewHolder();
                holder.itemDecription = itemName;
                holder.itemPicture = itemImage;

                view.setTag(holder);

            } else {

                ViewHolder holder = (ViewHolder) view.getTag();
                itemName = holder.itemDecription;
                itemImage = holder.itemPicture;

            }


            InventoryItem soldItem = listSoldItem.get(position);
            String barcode = soldItem.getBarcode();
            itemName.setText(barcode);

            itemImage.setImageResource(R.drawable.ic_cash_register_white);
            ParseFile pf = soldItem.getItemImg();
            if(pf!=null){
                picasso.load(pf.getUrl()).resize(140, 140).placeholder(R.drawable.ic_image_white).into(itemImage);
            }

            return view;
        }
    }


    public static class ViewHolder {

        public TextView itemDecription;
        public CustomImageView itemPicture;


    }

}
