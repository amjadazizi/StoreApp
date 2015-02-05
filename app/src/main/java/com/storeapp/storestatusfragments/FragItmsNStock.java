package com.storeapp.storestatusfragments;

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