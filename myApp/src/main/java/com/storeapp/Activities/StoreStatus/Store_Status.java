package com.storeapp.Activities.StoreStatus;


import android.app.ActionBar;
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

import com.storeapp.R;
import com.storeapp.storestatusfragments.FragExpiredItms;
import com.storeapp.storestatusfragments.FragItmsNStock;
import com.storeapp.storestatusfragments.FragItmsToOrder;
import com.storeapp.storestatusfragments.FragProfitStatus;
import com.storeapp.storestatusfragments.FragSoldItems;
import com.storeapp.storestatusfragments.FragTop10Itms;
import com.storeapp.util.SweetAlerts;

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
    private String[] storeTasks;
    ActionBar actionBar;
    static int listPosiCounter;
    FragmentManager fragmentManager;



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

      /*  final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.text_amount) //
                        : resources.getString(R.string.text_to_pay_back));

                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.peter_river));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });*/


        //ListView channelsList = (ListView) findViewById(R.id.channelsList);
        //LayoutInflater inflater = getLayoutInflater();

      //  ViewGroup header = (ViewGroup)inflater.inflate(R.layout.map_snapshot, channelsList , false);
       // channelsList .addHeaderView(header, null, false);


       drawerListView = (ListView) findViewById(R.id.drawerList);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_store_status_listview, drawerListView , false);
        drawerListView.addHeaderView(header, null, false);


        storeStatusAdapter = new StoreStatusAdapter(this);
        drawerListView.setAdapter(storeStatusAdapter);

        drawerListView.setOnItemClickListener(this);

        //drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListner = new ActionBarDrawerToggle(this, drawer,
                R.drawable.ic_drawer_opne_close, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        };

       // drawer.setDrawerListener(drawerListner);
        /*getActionBar().hide();

                setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'> "
                + getResources()
                .getString(R.string.title_activity_store_status)
                + "</font>"));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(
                android.R.color.transparent)));

        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#ADD8E6")));*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListner.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_status_bar, menu);
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
        //listPosiCounter = 0;

    }

    // @Override
    // public boolean onKeyUp(int keyCode, KeyEvent event) {
    //
    // if (keyCode == KeyEvent.KEYCODE_MENU) {
    // startActivity(new Intent(Store_Status.this, Settings.class));
    // return true;
    // } else {
    // return super.onKeyUp(keyCode, event);
    // }
    // }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
//         drawerListner.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        listPosiCounter = position;

             Fragment fragment = null;
       // String title = null;
        //String titleColor = "000000";

        switch (position) {

            case 1:
                selectItem(position);
                fragment = new FragItmsNStock();
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);
                //Utils.showToastShort("CLICKED!");
               //
                //title = getResources().getString(R.string.text_items_in_stock);
                // SoundEffects.playActivityChangeSound();

                break;
            case 2:
                selectItem(position);
                fragment = new FragSoldItems();
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);

              //  title = getResources().getString(R.string.text_sold_items);
               // SoundEffects.playActivityChangeSound();
                break;



            case 3:
                selectItem(position);
                fragment = new FragTop10Itms();
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);

               //
              //  title = getResources().getString(R.string.text_top_10_items);
               // SoundEffects.playActivityChangeSound();

                break;

            case 4:
                selectItem(position);
                fragment = new FragExpiredItms();
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);

      //
    //            title = getResources().getString(R.string.text_expired_items);
               // SoundEffects.playActivityChangeSound();

                break;

            case 5:
                selectItem(position);
                fragment = new FragItmsToOrder();
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);

//
  //              title = getResources().getString(R.string.text_items_to_order);
             //   SoundEffects.playActivityChangeSound();

                break;
            case 6:
                fragment = new FragProfitStatus();
                selectItem(position);
                SweetAlerts.showBasicMsg(Store_Status.this, "Det er Position: "+position);

               //
               // title = getResources().getString(R.string.text_profit_status);
                //SoundEffects.playActivityChangeSound();

                break;

            default:
                return;
        }

        if (fragment != null ) {
          // changeFragemnt(fragment);
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.mainContent, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

    }
/*
    private void changeFragemnt(Fragment fragment) {


        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.mainContent, fragment);
        transaction.addToBackStack(null);

        transaction.commit();

      //  closeDrawer();
    }*/

    @Override
    protected void onStart() {
        /*FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragItmsNStock fragment = new FragItmsNStock();
        String title = "Items In Stock";
        transaction.replace(R.id.mainContent, fragment, title);
        // transaction.addToBackStack(null);
        transaction.commit();
        closeDrawer();*/
        super.onStart();
    }

    public void closeDrawer() {
       // drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
    }

    public void selectItem(int position) {
        drawerListView.setItemChecked(position, true);
       // setTitle(storeTasks[position]);

    }

    public class StoreStatusAdapter extends BaseAdapter {

        private Context context;

        private String[] storeTasks;

        int[] listIcons = { R.drawable.ic_items_in_stock,
                R.drawable.ic_items_sold, R.drawable.ic_top_10_items,
                R.drawable.ic_expired_items, R.drawable.ic_items_to_order,
                R.drawable.ic_profit_status };

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

            TextView textViewTitle = null;
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

           // if (position == Store_Status.listPosiCounter) {
             //   convertView.setBackgroundColor(getResources().getColor(
               //         R.color.bckColor));
            //} else {
              //  convertView.setBackgroundColor(getResources().getColor(
                //        R.color.sand));
           // }
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
                //SoundEffects.playActivityChangeSound();

            } else {
                super.onBackPressed();
                // finish();
            }

        }

    }




}

/*

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
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
import android.widget.TextView;

import com.storeapp.ActivitiesOld.BaseFragmentActivity;
import com.storeapp.ActivitiesOld.SoundEffects;
import com.storeapp.R;
import com.storeapp.storestatusfragments.FragExpiredItms;
import com.storeapp.storestatusfragments.FragItmsNStock;
import com.storeapp.storestatusfragments.FragItmsToOrder;
import com.storeapp.storestatusfragments.FragProfitStatus;
import com.storeapp.storestatusfragments.FragSoldItms;
import com.storeapp.storestatusfragments.FragTop10Itms;

public class Store_Status extends BaseFragmentActivity implements
		OnItemClickListener {

	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerListner;
	private StoreStatusAdapter storeStatusAdapter;
	private String[] storeTasks;
	ActionBar actionBar;
	static int listPosiCounter;

	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store__status);

		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
		fragmentManager = getFragmentManager();
		storeTasks = getResources().getStringArray(R.array.storeTasks);
		actionBar = getActionBar();

		drawerListView = (ListView) findViewById(R.id.drawerList);
		storeStatusAdapter = new StoreStatusAdapter(this);
		drawerListView.setAdapter(storeStatusAdapter);

		drawerListView.setOnItemClickListener(this);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerListner = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer_opne_close, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {

			}

			@Override
			public void onDrawerClosed(View drawerView) {

			}
		};

		drawerLayout.setDrawerListener(drawerListner);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(Html.fromHtml("<font color='#000000'> "
				+ getResources()
						.getString(R.string.title_activity_store_status)
				+ "</font>"));
		actionBar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ADD8E6")));

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListner.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.store_status_bar, menu);
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
		listPosiCounter = 0;

	}

	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_MENU) {
	// startActivity(new Intent(Store_Status.this, Settings.class));
	// return true;
	// } else {
	// return super.onKeyUp(keyCode, event);
	// }
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		drawerListner.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		listPosiCounter = position;

		Fragment fragment = null;
		String title = null;
		String titleColor = "000000";

		switch (position) {

		case 0:

			selectItem(position);
			fragment = new FragItmsNStock();
			title = getResources().getString(R.string.text_items_in_stock);
			SoundEffects.playActivityChangeSound();

			break;

		case 1:

			selectItem(position);
			fragment = new FragSoldItms();
			title = getResources().getString(R.string.text_sold_items);
			SoundEffects.playActivityChangeSound();

			break;

		case 2:
			selectItem(position);
			fragment = new FragTop10Itms();
			title = getResources().getString(R.string.text_top_10_items);
			SoundEffects.playActivityChangeSound();

			break;

		case 3:
			selectItem(position);
			fragment = new FragExpiredItms();
			title = getResources().getString(R.string.text_expired_items);
			SoundEffects.playActivityChangeSound();

			break;

		case 4:
			selectItem(position);
			fragment = new FragItmsToOrder();
			title = getResources().getString(R.string.text_items_to_order);
			SoundEffects.playActivityChangeSound();

			break;
		case 5:
			selectItem(position);
			fragment = new FragProfitStatus();
			title = getResources().getString(R.string.text_profit_status);
			SoundEffects.playActivityChangeSound();

			break;
		case 6:
			SoundEffects.playActivityChangeSound();

			finish();

			break;
		default:
			return;
		}

		if (fragment != null && title != null) {
			changeFragemnt(fragment, title, titleColor);
		}

	}

	private void changeFragemnt(Fragment fragment, String title,
			String titleColor) {

		if (titleColor == null) {
			titleColor = "000000";
		}

		actionBar.setTitle(Html.fromHtml(String.format(
				"<font color='#%s'>%s</font>", titleColor, title)));
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		transaction.replace(R.id.mainContent, fragment, title);
		transaction.addToBackStack(null);

		transaction.commit();
		closeDrawer();
	}

	@Override
	protected void onStart() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		FragItmsNStock fragment = new FragItmsNStock();
		String title = "Items In Stock";
		transaction.replace(R.id.mainContent, fragment, title);
		// transaction.addToBackStack(null);
		transaction.commit();
		closeDrawer();
		super.onStart();
	}

	public void closeDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerLayout.closeDrawers();
	}

	public void selectItem(int position) {
		drawerListView.setItemChecked(position, true);
		setTitle(storeTasks[position]);

	}

	public class StoreStatusAdapter extends BaseAdapter {

		private Context context;

		private String[] storeTasks;
		int[] listIcons = { R.drawable.ic_items_in_stock,
				R.drawable.ic_items_sold, R.drawable.ic_top_10_items,
				R.drawable.ic_expired_items, R.drawable.ic_items_to_order,
				R.drawable.ic_profit_status };

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

			TextView textViewTitle = null;
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

			if (position == Store_Status.listPosiCounter) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.bckColor));
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.sand));
			}
			return convertView;
		}

	}

	private static class ViewHolder {
		public TextView txtTitle;
		public ImageView image;
	}

	@Override
	public void onBackPressed() {

		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			closeDrawer();

		} else {

			if (getFragmentManager().getBackStackEntryCount() > 0) {
				getFragmentManager().popBackStack();
				SoundEffects.playActivityChangeSound();

			} else {
				super.onBackPressed();
				// finish();
			}

		}

	}

}
*/