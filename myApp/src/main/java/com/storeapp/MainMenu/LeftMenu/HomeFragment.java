package com.storeapp.MainMenu.LeftMenu;

/**
 * Created by Amjad on 04-03-2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.special.ResideMenu.ResideMenu;
import com.storeapp.MainMenu.CashRegister.Cash_Register;
import com.storeapp.MainMenu.EmployeeList.Emp_List;
import com.storeapp.MainMenu.StoreStatus.Store_Status;
import com.storeapp.MainMenu.Store_Items;
import com.storeapp.R;


public class HomeFragment extends Fragment {

    private View view;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);


            ImageButton ctCashRegBtn = (ImageButton) view.findViewById(R.id.ctCashRegBtn);
            ctCashRegBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Cash_Register.class);
                    startActivity(intent);

                }
            });

        ImageButton ctStoreStatus = (ImageButton) view.findViewById(R.id.ctStoreStatus);
            ctStoreStatus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), Store_Status.class);
                    startActivity(intent);

                }
            });

        ImageButton ctStoreItemBtn = (ImageButton) view.findViewById(R.id.ctStoreItemBtn);
            ctStoreItemBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    startActivity(new Intent(getActivity(), Store_Items.class));

                }
            });

        ImageButton ctEmplList = (ImageButton) view.findViewById(R.id.ctEmplList);
            ctEmplList.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), Emp_List.class));
                }
            });

        return view;

    }

}