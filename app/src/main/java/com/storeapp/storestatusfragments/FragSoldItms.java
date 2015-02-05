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
import com.storeapp.storestatusfragments.FragItmsNStock.ItemsAdapter;
import com.storeapp.storestatusfragments.FragItmsNStock.ViewHolder;

public class FragSoldItms extends Fragment {

	private View view;
	private TextView itemTxtViewDesc;
	public ListView listSoldItems;
	ItemsManager itemsManager = DbManager.getDbManager().getItemsManager();

	public FragSoldItms() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_sold_items, container, false);
		listSoldItems = (ListView) view.findViewById(R.id.listSoldItems);

		itemTxtViewDesc = (TextView) view.findViewById(R.id.itemTxtViewDesc);

		return view;
	}

	public void loadSoldItems() {

		Cursor cursor = itemsManager.getSoldItems();

		SoldItemsAdpater soldItemsAdapter = new SoldItemsAdpater(cursor,
				getActivity().getApplicationContext());

		listSoldItems.setAdapter(soldItemsAdapter);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadSoldItems();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public class SoldItemsAdpater extends BaseAdapter {

		private Cursor cursor;
		private Context ctx = getActivity().getApplicationContext();

		public SoldItemsAdpater(Cursor cursor, Context ctx) {

			this.cursor = cursor;
			this.ctx = ctx;

		}

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

			if (view == null) {

				LayoutInflater layoutinflater = LayoutInflater.from(ctx);
				view = layoutinflater.inflate(R.layout.sold_items_row, parent,
						false);
				txtItemDesc = (TextView) view
						.findViewById(R.id.txtItemDescription);

				ViewHolder holder = new ViewHolder();
				holder.txtItem = txtItemDesc;

				view.setTag(holder);

			} else {

				ViewHolder holder = (ViewHolder) view.getTag();
				txtItemDesc = holder.txtItem;

			}

			cursor.moveToPosition(position);
			String itemDesc = cursor.getString(cursor
					.getColumnIndex(ItemsManager.COL_DESCRIPTION));


			txtItemDesc.setText(itemDesc);

			return view;
		}
	}

	public static class ViewHolder {
		public TextView txtItem;

	}

}
