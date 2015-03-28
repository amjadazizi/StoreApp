package com.storeapp.storestatusfragments;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.storeapp.Activities.parse.InventoryItem;
import com.storeapp.R;

import java.util.List;

public class FragItmsNStock extends Fragment implements AdapterView.OnItemClickListener {
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


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

 /*   private ArrayList<ParseFile> getImageParsefile() {
        ArrayList<ParseFile> imageBitmapArrayList = new ArrayList<ParseFile>();

        for (int i = 0; i >= itemsInStock.size(); i++) {
            imageBitmapArrayList.add(itemsInStock.get(i).getItemImg());

        }

        return imageBitmapArrayList;
    }*/

    private void loadItems() {
        // List<InventoryItem> itemsInStock;

        ParseQuery<InventoryItem> query = InventoryItem.getQuery();
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
        public ImageView itemImage;

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
                itemImage = (ImageView) view.findViewById(R.id.itemImage);

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

        /*    ParseFile pf = items.getItemImg();
            pf.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if(e==null){
                        if(bytes!=null && bytes.length>0){

                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            itemImage.setImageBitmap(bitmap);


                        }



                    }
                }
            });
*/

            try {
                ParseFile pf = items.getItemImg();
                byte[] imageBytes =  pf.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                itemImage.setImageBitmap(bitmap);
                String a= "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            // itemImage.setImageBitmap(bitmap1);

            return view;
        }
    }

    public static class ViewHolder {

        public TextView itemDescription;
         public ImageView itemPicture;


    }


}








/*
package com.storeapp.storestatusfragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.storeapp.Activities.ExternalClasses.SquareImageView;
import com.storeapp.Activities.StoreStatus.StoreStatusDialogActivity;
import com.storeapp.R;

import java.util.ArrayList;
import java.util.List;

public class FragItmsNStock extends Fragment implements AdapterView.OnItemClickListener
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragmen_items_in_stock, container,
                false);


           // View header = inflater.inflate(
             //       R.layout.items_in_stock_listview_header_row, null);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(getActivity()));
        gridView.setOnItemClickListener(this);


        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), StoreStatusDialogActivity.class);
        startActivity(intent);
    }


    private class MyAdapter extends BaseAdapter
    {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;

        public MyAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);

            items.add(new Item("Image 1", R.drawable.ic_call_48));
            items.add(new Item("Image 2", R.drawable.ic_call_48));
            items.add(new Item("Image 3", R.drawable.ic_email_32));
            items.add(new Item("Image 4", R.drawable.ic_cash_register));
            items.add(new Item("Image 5", R.drawable.ic_arrow_right));
            items.add(new Item("Image 1", R.drawable.ic_call_48));
            items.add(new Item("Image 2", R.drawable.ic_call_48));
            items.add(new Item("Image 3", R.drawable.ic_email_32));
            items.add(new Item("Image 4", R.drawable.ic_cash_register));
            items.add(new Item("Image 5", R.drawable.ic_arrow_right));
            items.add(new Item("Image 1", R.drawable.ic_call_48));
            items.add(new Item("Image 2", R.drawable.ic_call_48));
            items.add(new Item("Image 3", R.drawable.ic_email_32));
            items.add(new Item("Image 4", R.drawable.ic_cash_register));
            items.add(new Item("Image 5", R.drawable.ic_arrow_right));
            items.add(new Item("Image 1", R.drawable.ic_call_48));
            items.add(new Item("Image 2", R.drawable.ic_call_48));
            items.add(new Item("Image 3", R.drawable.ic_email_32));
            items.add(new Item("Image 4", R.drawable.ic_cash_register));
            items.add(new Item("Image 5", R.drawable.ic_arrow_right));
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View v = view;
            SquareImageView picture;
            TextView name;

            if(v == null)
            {
             //   v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
              //  v.setTag(R.id.picture, v.findViewById(R.id.picture));
              //  v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            //picture = (SquareImageView)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(i);

          //  picture.setImageResource(item.drawableId);
          //  name.setText(item.name);

            return v;
        }

        private class Item
        {
            final String name;
            final int drawableId;

            Item(String name, int drawableId)
            {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }

}







*/










/*

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.storeapp.R;
import com.storeapp.db.DbManager;
import com.storeapp.db.ItemsManager;

public class FragItmsNStock extends Fragment {

	ItemsManager itemsManager = DbManager.getDbManager().getItemsManager();
	public ListView listViewItmsInStock;
	private static final String LV_CURRENT_POSITION = "lv_current_position";
	private static final String Y_CURRENT_POSITION = "y_current_position";
	private int lvFirstVisibleChildViewIndex;
	private int lvFirstVisibleChildViewTop;
	private View view;

	public FragItmsNStock() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			readSate(savedInstanceState);

		} else {
			view = inflater.inflate(R.layout.fragmen_items_in_stock, container,
					false);
			listViewItmsInStock = (ListView) view
					.findViewById(R.id.listViewItmsInStock);

			View header = inflater.inflate(
					R.layout.items_in_stock_listview_header_row, null);

			listViewItmsInStock.addHeaderView(header);

		}

		return view;
	}

	public void loadItems() {

		Cursor cursor = itemsManager.getAllItemsInStock();
		ItemsAdapter itemsAdapter = new ItemsAdapter(getActivity()
				.getApplicationContext(), cursor);
		listViewItmsInStock.setAdapter(itemsAdapter);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		loadItems();
	}

	public class ItemsAdapter extends BaseAdapter {

		private final int INVALID = -1;
		protected int DELETE_POS = -1;


		private Cursor cursor;
		private Context ctx = getActivity().getApplicationContext();

		public ItemsAdapter(Context ctx, Cursor cursor) {
			this.ctx = ctx;
			this.cursor = cursor;

		}

		public void onSwipeItem(boolean isRight, int position) {
			if (isRight == false) {
				DELETE_POS = position;
			} else if (DELETE_POS == position) {
				DELETE_POS = INVALID;
			}
			notifyDataSetChanged();
		}

		public void deleteItem(int pos) {
			//
			listViewItmsInStock.removeViewAt(pos);
			DELETE_POS = INVALID;
			notifyDataSetChanged();
		}

		// ---------------

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
			return cursor.getLong(cursor.getColumnIndex(itemsManager.COL_ID));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;
			TextView txtItemDesc = null;
			TextView itemTxtViewCount = null;
			TextView itemTxtPurPrice = null;
			TextView itemTxtSellPrice = null;

			if (view == null) {

				LayoutInflater layoutinflater = LayoutInflater.from(ctx);
				view = layoutinflater.inflate(
						R.layout.items_in_stock_listview_row, parent,
						false);
				txtItemDesc = (TextView) view
						.findViewById(R.id.itemTxtViewDesc);
				itemTxtViewCount = (TextView) view
						.findViewById(R.id.itemTxtViewCount);

				itemTxtPurPrice = (TextView) view
						.findViewById(R.id.itemTxtPurPrice);
				itemTxtSellPrice = (TextView) view
						.findViewById(R.id.itemTxtSellPrice);

				ViewHolder holder = new ViewHolder();
				holder.txtItem = txtItemDesc;
				holder.itemTxtViewCount = itemTxtViewCount;
				holder.itemTxtPurPrice = itemTxtPurPrice;
				holder.itemTxtSellPrice = itemTxtSellPrice;
				view.setTag(holder);

			} else {

				ViewHolder holder = (ViewHolder) view.getTag();
				txtItemDesc = holder.txtItem;
				itemTxtViewCount = holder.itemTxtViewCount;

				itemTxtPurPrice = holder.itemTxtPurPrice;
				itemTxtSellPrice = holder.itemTxtSellPrice;

			}

			cursor.moveToPosition(position);
			String itemDesc = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_DESCRIPTION));
			String itemCount = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_AMOUNT));

			String itemPurPrice = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_PURCHPRICE));

			String itemSellPrice = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_SELLPRICE));

			txtItemDesc.setText(itemDesc);
			itemTxtViewCount.setText(itemCount);
			itemTxtPurPrice.setText(itemPurPrice);
			itemTxtSellPrice.setText(itemSellPrice);

			return view;
		}
	}

	public static class ViewHolder {
		public TextView txtItem, itemTxtViewCount, itemTxtPurPrice,
				itemTxtSellPrice;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		prepareSaveStateData();
		saveState(outState);
	}


	private void prepareSaveStateData() {
		lvFirstVisibleChildViewIndex = listViewItmsInStock
				.getFirstVisiblePosition();
		View v = listViewItmsInStock.getChildAt(0);
		lvFirstVisibleChildViewTop = (v == null ? 0 : v.getTop());
	}

	private void saveState(Bundle outState) {

		outState.putInt(LV_CURRENT_POSITION, lvFirstVisibleChildViewIndex);
		outState.putInt(Y_CURRENT_POSITION, lvFirstVisibleChildViewTop);
	}

	private void readSate(Bundle savedInstanceState) {
		lvFirstVisibleChildViewIndex = savedInstanceState
				.getInt(LV_CURRENT_POSITION);
		lvFirstVisibleChildViewTop = savedInstanceState
				.getInt(Y_CURRENT_POSITION);
	}

}
*/