package com.storeapp.MainMenu.StoreStatus.fragments_store_status;


import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.List;

public class FragItmsNStock extends Fragment implements AdapterView.OnItemClickListener {

    Picasso  picasso ;
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int largeMemoryInMegaBytes = ((ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;


       Picasso.Builder pb = new Picasso.Builder(getActivity());
       picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        picasso.setIndicatorsEnabled(true);


        View view = inflater.inflate(R.layout.fragmen_items_in_stock, container,
                false);

        gridView = (GridView) view.findViewById(R.id.gridview);

        loadItems();
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Intent intent = new Intent(getActivity(), StoreStatusDialogActivity.class);
        //  startActivity(intent);
    }


    private void loadItems() {

        ParseQuery<InventoryItem> query = InventoryItem.getQuery(Prefs.getBusinessCvr());
        query.whereEqualTo("cvr", Prefs.getBusinessCvr());
        query.whereGreaterThan("amount", 0);
        query.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(final List<InventoryItem> inventoryItems, ParseException e) {
                if (e == null) {
                    if (inventoryItems != null && inventoryItems.size() > 0) {


                        ItemsInStocAdapter itemsAdapter = new ItemsInStocAdapter(getActivity(), inventoryItems);
                        gridView.setAdapter(itemsAdapter);
                    }
                }
            }

        });
    }

    private class ItemsInStocAdapter extends BaseAdapter {
        public CustomImageView itemImage;

        Context ctx;
        List<InventoryItem> itemsList;


        public ItemsInStocAdapter(Context ctx, List<InventoryItem> itemsList) {
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
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            TextView itemName = null;

            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(ctx);
                view = layoutInflater.inflate(R.layout.items_instock_grid_item, null);

                itemName = (TextView) view.findViewById(R.id.txtItemName);
                itemImage = (CustomImageView) view.findViewById(R.id.itemImage);

                ViewHolder holder = new ViewHolder();
                holder.itemDescription = itemName;
                  holder.itemPicture = itemImage;


                view.setTag(holder);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                itemName = holder.itemDescription;
                 itemImage = holder.itemPicture;
            }


            InventoryItem items = itemsList.get(position);


            itemName.setText(items.getDescription());

            itemImage.setImageResource(R.drawable.ic_cash_register_white);
            ParseFile pf = items.getItemImg();
            if(pf!=null){
                picasso.load(pf.getUrl()).resize(140, 140).placeholder(R.drawable.ic_image_white).into(itemImage);
            }


            return view;
        }
    }

    public static class ViewHolder {

        public TextView itemDescription;
         public CustomImageView itemPicture;


    }


}
