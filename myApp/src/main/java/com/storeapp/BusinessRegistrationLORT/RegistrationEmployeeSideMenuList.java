/*
package com.storeapp.BusinessRegistrationLORT;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.storeapp.Activities.AppContextProvider;
import com.storeapp.Activities.BaseFragment;
import com.storeapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistrationEmployeeSideMenuList extends BaseFragment {
	private View view;


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    public AppContextProvider ctx;
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registraion_employee_side_menu, container,
                false);
        // setContentView(R.layout.fragment_registraion_sidemenu_main);

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();
//Context ctx = AppContextProvider.getContext();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

//       listAdapter = new ExpandableListAdapter(ctx , listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return view;
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();

        // Adding child data

        listDataHeader.add("Employee");

        // Adding child data
        List<String> BusinessRegistratSteps = new ArrayList<String>();
        BusinessRegistratSteps.add("Employee Name");
        BusinessRegistratSteps.add("Employee Name");
        BusinessRegistratSteps.add("Employee Name");
        BusinessRegistratSteps.add("CVR and Password");



        List<String> EmployeeRegistSteps = new ArrayList<String>();
        EmployeeRegistSteps.add("Person Info");
        EmployeeRegistSteps.add("Contact Details");
        EmployeeRegistSteps.add("Email and Password");

        listDataChild.put(listDataHeader.get(0), BusinessRegistratSteps); // Header, Child data
        listDataChild.put(listDataHeader.get(1), EmployeeRegistSteps);

    }

    @Override
    protected BaseFragment getPrevFragment() {
        return null;
    }

    @Override
    protected BaseFragment getNextFragment() {
        return null;
    }

    @Override
    protected String validate() {
        return null;
    }

    @Override
    protected void save() {

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Activity _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Activity context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(AppContextProvider.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.registraion_sidemenu_main_list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(AppContextProvider.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.registraion_sidemenu_main_list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
*/
