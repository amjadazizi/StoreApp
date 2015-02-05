package com.storeapp;

import android.media.MediaPlayer;

public class SoundEffects {

	public static void playActivityChangeSound() {
		boolean soundEnabled = Prefs.isSoundsEnabledPref();
		if (soundEnabled) {
			MediaPlayer scannerSound = MediaPlayer.create(
					AppContextProvider.getContext(), R.raw.pagesound);
			scannerSound.start();
		}

	}

	public static void playCashRegisterSound() {
		boolean soundEnabled = Prefs.isSoundsEnabledPref();

		if (soundEnabled) {
			MediaPlayer scannerSound = MediaPlayer.create(
					AppContextProvider.getContext(), R.raw.cash_register_sound);
			scannerSound.start();

		}

	}
}
