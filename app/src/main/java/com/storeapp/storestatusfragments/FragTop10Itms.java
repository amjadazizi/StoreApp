package com.storeapp.storestatusfragments;

import com.storeapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragTop10Itms extends Fragment {
	private View view;

	public FragTop10Itms() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragament_top_10_items, container,
				false);
		return view;
	}
}
