package com.storeapp;

import java.security.acl.LastOwnerException;

import com.storeapp.util.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public abstract class BaseFragment extends Fragment {

	protected abstract Fragment getNextFragment();

	protected void gotoNextFragment() {

		Fragment fragment = getNextFragment();
		if (fragment != null) {// if not last then go to next fragment
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();

			transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down,
					R.anim.slide_up, R.anim.slide_down);
			SoundEffects.playActivityChangeSound();
			transaction.add(R.id.fragConLay, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

}
