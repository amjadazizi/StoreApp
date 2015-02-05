package com.storeapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragmentActivity extends FragmentActivity {

	protected boolean doSoundeffect = true;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		Prefs.changeUserLanguageToCurrent();
		doSoundeffect = (bundle == null);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (doSoundeffect) {
			SoundEffects.playActivityChangeSound();
		}

	}

}
