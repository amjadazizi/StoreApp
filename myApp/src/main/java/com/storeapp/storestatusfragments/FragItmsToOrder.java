package com.storeapp.storestatusfragments;

import com.storeapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragItmsToOrder extends Fragment {

	private View view;

	public FragItmsToOrder() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragement_item_to_order, container,
				false);
		return view;
	}
}
