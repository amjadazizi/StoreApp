package com.storeapp.MainMenu.EmployeeList;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.storeapp.BaseActivity;
import com.storeapp.R;
import com.storeapp.parse.User;
import com.storeapp.util.Prefs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Emp_List extends BaseActivity {

    private ListView empoyeeList;
    Picasso picasso;
    ImageButton btnbackArrowEmpList;
    private List<User> users;
    ProgressBar empllistProgress;
    private static final String PARSE_CACHE_BUSINESS_USERS_LABLE = "parse_cache_business_users_lable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_emp_list);
        getActionBar().hide();

        int largeMemoryInMegaBytes = ((ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;


        Picasso.Builder pb = new Picasso.Builder(this);
        picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        empllistProgress = (ProgressBar) findViewById(R.id.empllistProgress);
        empllistProgress.setVisibility(View.VISIBLE);

        empoyeeList = (ListView) findViewById(R.id.employeeList);

        btnbackArrowEmpList = (ImageButton) findViewById(R.id.btnbackArrowEmpList);
        btnbackArrowEmpList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void downloadBusinessUsers(String businessCvr) {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.orderByAscending(User.COL_FIRST_NAME);
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


    private void fetchBusinessUsers() {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("businessCvr", Prefs.getBusinessCvr());
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> usersList, ParseException e) {
                if (e != null) {
                    return;
                }

                Collections.sort(usersList,new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                      String  user1Firstname = user1.getFirstName().toUpperCase();
                        String  user2Firstname = user2.getFirstName().toUpperCase();

                        return user1Firstname.compareTo(user2Firstname);
                    }
                });
                users = usersList;
                if(users == null || users.size()==0 ){

                    downloadBusinessUsers(PARSE_CACHE_BUSINESS_USERS_LABLE);
                }
                else{
                    refreshUsers();
                }

                empllistProgress.setVisibility(View.GONE);

            }
        });

    }

    private void refreshUsers() {

        EmployeeAdapter adapter = new EmployeeAdapter(this, users);

        empoyeeList.setAdapter(adapter);

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
            ImageButton btnCall ,btnSms, btnEmail;
            TextView firstName = null;
            TextView lastName = null;
            TextView Email = null;
            TextView phoneNumber = null;
            ImageView profile_image;

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
                profile_image = (ImageView) view.findViewById(R.id.profile_image);

                ViewHolder holder = new ViewHolder();
                holder.fname = firstName;
                holder.lname = lastName;
                holder.empEmail = Email;
                holder.empPhoneNumber = phoneNumber;
                holder.imgEmpPic = profile_image;


                view.setTag(holder);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                firstName = holder.fname;
                lastName = holder.lname;
                Email = holder.empEmail;
                phoneNumber = holder.empPhoneNumber;
                profile_image = holder.imgEmpPic;
            }

            final User user = users.get(position);

            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            Email.setText(user.getEmail());
            phoneNumber.setText(user.getPhoneNumber());


            profile_image.setImageResource(R.drawable.ic_employee_white);

            ParseFile pf = user.getProfileImage();
            if(pf!=null){
                picasso.load(pf.getUrl()).resize(140, 140).placeholder(R.drawable.ic_employee_white).into(profile_image);
            }

            btnCall  = (ImageButton)  view.findViewById(R.id.btnCall);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + user.getPhoneNumber()));
                    startActivity(callIntent);

                }
            });
            btnSms  = (ImageButton)  view.findViewById(R.id.btnSms);
            btnSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse("smsto:" + user.getPhoneNumber()));
                    startActivity(Intent.createChooser(smsIntent, ""));

                }
            });

            btnEmail  = (ImageButton)  view.findViewById(R.id.btnEmail);
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
                            .fromParts("mailto", user.getEmail(), null));

                    startActivity(Intent.createChooser(emailIntent,null));
                }
            });
            return view;

        }
    }

    public static class ViewHolder {

        public ImageView imgEmpPic;
        public TextView fname,lname, empEmail, empPhoneNumber;
    }
}

