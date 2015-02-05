package com.storeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.storeapp.db.DbManager;
import com.storeapp.db.UsersManager;

public class Emp_List extends BaseActivity {

	public static final String EXTRA_EMPLOYEE_ID = "extra_employee_id";

	private ListView listEmployees;
	private SearchView searchView;

	private int lvFirstVisibleChildViewIndex;
	private int lvFirstVisibleChildViewTop;

	private static final String LV_CURRENT_POSITION = "lv_current_position";
	private static final String Y_CURRENT_POSITION = "y_current_position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			readSate(savedInstanceState);

		}

		setContentView(R.layout.activity_emp_list);

		listEmployees = (ListView) findViewById(R.id.lvEmployees);

		listEmployees.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				view.setSelected(true);

				Intent intent = new Intent(Emp_List.this, Emp_Details.class);
				intent.putExtra(EXTRA_EMPLOYEE_ID, (int) id);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.emp_list_bar, menu);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				search(newText);
				return true;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();

		switch (itemId) {
		case android.R.id.home:
			this.finish();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			startActivity(new Intent(Emp_List.this, Settings.class));
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	private void search(String searchTerm) {

		UsersManager um = DbManager.getDbManager().getUserManager();
		Cursor cursor = um.getAllEmpNames(searchTerm);
		EmployeeAdapter adapter = new EmployeeAdapter(this, cursor);
		listEmployees.setAdapter(adapter);

	}

	private void loadEmployees() {

		UsersManager um = DbManager.getDbManager().getUserManager();
		Cursor cursor = um.getAllEmployees();
		EmployeeAdapter adapter = new EmployeeAdapter(this, cursor);
		listEmployees.setAdapter(adapter);

		listEmployees.setSelectionFromTop(lvFirstVisibleChildViewIndex,
				lvFirstVisibleChildViewTop);

	}

	@Override
	protected void onPause() {
		super.onPause();

		prepareSaveStateData();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadEmployees();
	}

	private class EmployeeAdapter extends BaseAdapter {
		private Context ctx;
		private Cursor cursor;

		public EmployeeAdapter(Context ctx, Cursor cursor) {
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
			return cursor.getLong(cursor.getColumnIndex(UsersManager.COL_ID));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;

			TextView txtEmployeeName = null;
			ImageView imgEmpPic = null;

			if (view == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(ctx);
				view = layoutInflator.inflate(R.layout.employee_list_row, null);

				txtEmployeeName = (TextView) view
						.findViewById(R.id.txtEmployeeName);
				imgEmpPic = (ImageView) view.findViewById(R.id.imgEmpPic);

				ViewHolder holder = new ViewHolder();
				holder.txtEmpName = txtEmployeeName;

				// holder.imgEmpPic = imgEmpPic;

				view.setTag(holder);
			} else {
				ViewHolder holder = (ViewHolder) view.getTag();
				txtEmployeeName = holder.txtEmpName;
				// imgEmpPic = holder.imgEmpPic;
			}

			cursor.moveToPosition(position);
			String name = cursor.getString(cursor
					.getColumnIndex(UsersManager.COL_NAME));

			txtEmployeeName.setText(name);

			return view;
		}
	}

	public static class ViewHolder {

		public ImageView imgEmpPic;
		public TextView txtEmpName;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		prepareSaveStateData();
		saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		readSate(savedInstanceState);

	}

	private void prepareSaveStateData() {
		lvFirstVisibleChildViewIndex = listEmployees.getFirstVisiblePosition();
		View v = listEmployees.getChildAt(0);
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

	@Override
	public int getActivityTitleResId() {
		return R.string.title_activity_employee_list;
	}

}
