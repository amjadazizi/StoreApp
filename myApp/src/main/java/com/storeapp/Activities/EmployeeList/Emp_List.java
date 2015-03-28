package com.storeapp.Activities.EmployeeList;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.storeapp.Activities.parse.User;
import com.storeapp.R;

import java.util.List;


public class Emp_List extends Activity {

    public static final String EXTRA_EMPLOYEE_ID = "extra_employee_id";

    private ListView empoyeeList;
    private SearchView searchView;

    private int lvFirstVisibleChildViewIndex;
    private int lvFirstVisibleChildViewTop;

    private static final String LV_CURRENT_POSITION = "lv_current_position";
    private static final String Y_CURRENT_POSITION = "y_current_position";
    private static final String PARSE_CACHE_BUSINESS_USERS_LABLE = "parse_cache_business_users_lable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            readSate(savedInstanceState);

        }

        setContentView(R.layout.activity_emp_list);

        empoyeeList = (ListView) findViewById(R.id.employeeList);
        getActionBar().hide();

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
                //  search(newText);
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

            //startActivity(new Intent(Emp_List.this, Settings.class));
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

	/*private void search(String searchTerm) {

		UsersManager um = DbManager.getDbManager().getUserManager();
		Cursor cursor = um.getAllEmpNames(searchTerm);
		EmployeeAdapter adapter = new EmployeeAdapter(this, cursor);
		listEmployees.setAdapter(adapter);

	}*/


    private void downloadBusinessUsers(String businessCvr) {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(final List<User> usersList, ParseException e) {

                if (e != null) {
                    return;
                }

                ParseObject.unpinAllInBackground(PARSE_CACHE_BUSINESS_USERS_LABLE, usersList, new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e != null) {
                            return;
                        }
                        ParseObject.pinAllInBackground(PARSE_CACHE_BUSINESS_USERS_LABLE, usersList);

                        users = usersList;
                        refreshUsers();
                    }
                });
            }
        });

    }

    private List<User> users;

    private void fetchBusinessUsers() {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
       // query.fromLocalDatastore();

        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> usersList, ParseException e) {
                if (e != null) {
                    return;
                }
               users = usersList;
                if(users == null || users.size()==0 ){

                    downloadBusinessUsers(PARSE_CACHE_BUSINESS_USERS_LABLE);
                }
                else{
                    refreshUsers();
                }
            }
        });


    }

    private void refreshUsers() {

        EmployeeAdapter adapter = new EmployeeAdapter(this, users);

        empoyeeList.setAdapter(adapter);

        empoyeeList.setSelectionFromTop(lvFirstVisibleChildViewIndex,
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

        fetchBusinessUsers();
    }



    private class EmployeeAdapter extends BaseAdapter {
        private Context ctx;
        private List<User> users;

        public EmployeeAdapter(Context context, List<User> users) {
            this.ctx = context;
            this.users= users;

        }


        @Override
        public int getCount() {
            return users.size();
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

            TextView firstName = null;
            TextView lastName = null;
            TextView Email = null;
            TextView phoneNumber = null;

            if (view == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(ctx);
                view = layoutInflator.inflate(R.layout.employee_list_row, null);

                firstName = (TextView) view
                        .findViewById(R.id.firstName);
                lastName = (TextView) view
                        .findViewById(R.id.lastName);
                Email = (TextView) view
                        .findViewById(R.id.Email);
                phoneNumber = (TextView) view
                        .findViewById(R.id.phoneNumber);

                ViewHolder holder = new ViewHolder();
                holder.fname = firstName;
                holder.lname = lastName;
                holder.empEmail = Email;
                holder.empPhoneNumber = phoneNumber;


                view.setTag(holder);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                firstName = holder.fname;
                lastName = holder.lname;
                Email = holder.empEmail;
                phoneNumber = holder.empPhoneNumber;
            }

            User user = users.get(position);

            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            Email.setText(user.getEmail());
            phoneNumber.setText(user.getPhoneNumber());


            return view;

        }


    }

    public static class ViewHolder {

        public ImageView imgEmpPic;
        public TextView fname,lname, empEmail, empPhoneNumber;

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
        lvFirstVisibleChildViewIndex = empoyeeList.getFirstVisiblePosition();
        View v = empoyeeList.getChildAt(0);
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

