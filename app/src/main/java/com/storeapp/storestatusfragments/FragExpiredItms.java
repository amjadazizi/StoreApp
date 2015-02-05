package com.storeapp.storestatusfragments;

import com.storeapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragExpiredItms extends Fragment {

	private View view;

	public FragExpiredItms() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_expired_items, container,
				false);
		return view;
	}

}
