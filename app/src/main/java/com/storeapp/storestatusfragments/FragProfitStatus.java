package com.storeapp.storestatusfragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storeapp.R;

public class FragProfitStatus extends Fragment {
	private View view;

	public FragProfitStatus() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {

		view = inflater.inflate(R.layout.fragmen_profit_status, container,
				false);
		return view;

	}

}
