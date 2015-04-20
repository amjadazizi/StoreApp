package com.storeapp.MainMenu.StoreStatus;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.MainMenu.StoreStatus.fragments_store_status.FragItmsNStock;
import com.storeapp.MainMenu.StoreStatus.fragments_store_status.FragProfitStatus;
import com.storeapp.MainMenu.StoreStatus.fragments_store_status.FragSoldItems;
import com.storeapp.MainMenu.StoreStatus.fragments_store_status.FragTop10Itms;
import com.storeapp.R;
import com.storeapp.parse.Business;
import com.storeapp.util.Prefs;

import java.util.List;

import static android.view.Gravity.START;


public class Store_Status extends Activity implements
        AdapterView.OnItemClickListener {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerListView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerListner;
    private StoreStatusAdapter storeStatusAdapter;
    static int listPosiCounter= -1;
    FragmentManager fragmentManager = getFragmentManager();
    TextView textViewTitle = null;
    TextView txtTitleStoreStatus,txtShopName, txtShopCvrNumber, txtShopPhonenumber, txtShopEmalAdd ;
    String CURRENT_FRAGMENT="current_fragment";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__status);




        getActionBar().hide();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);




            final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
        imageView.setImageDrawable(drawerArrowDrawable);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });



        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });




       drawerListView = (ListView) findViewById(R.id.drawerList);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_store_status_listview, drawerListView , false);
        drawerListView.addHeaderView(header, null, false);


        storeStatusAdapter = new StoreStatusAdapter(this);
        drawerListView.setAdapter(storeStatusAdapter);

        drawerListView.setOnItemClickListener(this);
        drawerListner = new ActionBarDrawerToggle(this, drawer,
                R.drawable.ic_statistic, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        };

        shopInfomation();

        txtTitleStoreStatus = (TextView) findViewById(R.id.txtTitleStoreStatus);

    }



    private void shopInfomation(){
        txtShopName  = (TextView) findViewById(R.id.txtShopName);
        txtShopCvrNumber  = (TextView) findViewById(R.id.txtShopCvrNumber);
        txtShopPhonenumber  = (TextView) findViewById(R.id.txtShopPhonenumber);
        txtShopEmalAdd  = (TextView) findViewById(R.id.txtShopEmalAdd);

        ParseQuery<Business> businessParseQuery = Business.getQuery(Prefs.getBusinessCvr());
        businessParseQuery.findInBackground(new FindCallback<Business>() {
            @Override
            public void done(List<Business> businesses, ParseException e) {
                if(e==null){
                    if(businesses!=null && businesses.size()>0){
                        Business bus = businesses.get(0);
                        txtShopName.setText(bus.getName());
                        txtShopCvrNumber.setText(bus.getCvr());
                        txtShopPhonenumber.setText(bus.getPhoneNumber());
                        txtShopEmalAdd.setText(bus.getEmail());
                    }
                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListner.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.home:
                this.finish();
                break;

        }

        if (drawerListner.onOptionsItemSelected(menuItem)) {
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,
                R.anim.activity_close_translate);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        listPosiCounter = position;

             Fragment fragment = null;

        String title = null;

        switch (position) {

            case 1:
                listPosiCounter = position;
                selectItem(position);
                fragment = new FragItmsNStock();
                title = getResources().getString(R.string.text_items_in_stock);

                break;
            case 2:
                listPosiCounter = position;

                selectItem(position);
                fragment = new FragSoldItems();
                title = getResources().getString(R.string.text_sold_items);

                break;



            case 3:
                listPosiCounter = position;

                selectItem(position);
                fragment = new FragTop10Itms();
                title = getResources().getString(R.string.text_top_10_items);

                break;

            case 4:
                selectItem(position);
                listPosiCounter = position;
                fragment = new FragProfitStatus();
                title = getResources().getString(R.string.text_profit_status);

                break;

            case 5:
                 selectItem(position);
                finish();

                break;


            default:
                return;
        }

        if (fragment != null ) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.mainContent, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
            closeDrawer();
            txtTitleStoreStatus.setText(title);
        }


    }


    @Override
    protected void onStart() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragItmsNStock fragment = new FragItmsNStock();
        transaction.add(R.id.mainContent, fragment, getResources().getString(R.string.text_items_in_stock));
        // transaction.addToBackStack(null);
        txtTitleStoreStatus.setText(getResources().getString(R.string.text_items_in_stock));
        transaction.commit();
        closeDrawer();
        super.onStart();
    }

    public void closeDrawer() {
        drawer.closeDrawers();
    }

    public void selectItem(int position) {
        drawerListView.setItemChecked(position, true);
    }

    public class StoreStatusAdapter extends BaseAdapter {

        private Context context;

        private String[] storeTasks;

        int[] listIcons = { R.drawable.ic_items_in_stock,
                R.drawable.ic_items_sold, R.drawable.ic_top_10_items,
                           R.drawable.ic_statistic, R.drawable.ic_home_grey };

        public StoreStatusAdapter(Context context) {
            this.context = context;

            storeTasks = context.getResources().getStringArray(
                    R.array.storeTasks);

        }

        @Override
        public int getCount() {
            return storeTasks.length;

        }

        @Override
        public Object getItem(int position) {
            return storeTasks[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setTitle(String title) {
            getActionBar().setTitle(title);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ImageView imageView = null;

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.drawerlist_row, parent,
                        false);

                textViewTitle = (TextView) convertView
                        .findViewById(R.id.dratxtView);
                imageView = (ImageView) convertView
                        .findViewById(R.id.draListImageview);

                ViewHolder holder = new ViewHolder();
                holder.txtTitle = textViewTitle;
                holder.image = imageView;

                convertView.setTag(holder);

            } else {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                textViewTitle = holder.txtTitle;
                imageView = holder.image;

            }

            textViewTitle.setText(storeTasks[position]);
           imageView.setImageResource(listIcons[position]);

            /*if (position == Store_Status.listPosiCounter) {
                convertView.setBackgroundColor(getResources().getColor(
                        R.color.bckColor));
            } else {
                convertView.setBackgroundColor(getResources().getColor(
                        R.color.sand));
            }*/
            return convertView;
        }

    }

    private static class ViewHolder {
        public TextView txtTitle;
        public ImageView image;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();

        } else {

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();

            } else {
                super.onBackPressed();
                 finish();
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT,getFragmentManager().getBackStackEntryCount());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}

