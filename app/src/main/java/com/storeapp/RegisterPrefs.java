package com.storeapp;

public class RegisterPrefs {

	public static final String PREF_FIRST_NAME = "pref_first_name";
	public static final String PREF_LAST_NAME = "pref_last_name";

	public static final String PREF_EMAIL_NAME = "pref_email";

	public static final String PREF_PHONE_NUMBER = "pref_phone_number";

	public static final String PREF_PASSWORD = "pref_password";

	public static void setFirstName(String firstName) {

		Prefs.setStringPref(PREF_FIRST_NAME, firstName);
	}

	public static String getFirstName() {
		return Prefs.getStringPref(PREF_FIRST_NAME, null);
	}

	public static void setLastName(String lastName) {

		Prefs.setStringPref(PREF_LAST_NAME, lastName);
	}

	public static String getLastName() {
		return Prefs.getStringPref(PREF_LAST_NAME, null);
	}

	public static void setEmal(String email) {

		Prefs.setStringPref(PREF_EMAIL_NAME, email);
	}

	public static String getEmail() {
		return Prefs.getStringPref(PREF_EMAIL_NAME, null);
	}

	public static void setPhoneNumber(String phoneNumber) {

		Prefs.setStringPref(PREF_PHONE_NUMBER, phoneNumber);
	}

	public static String getPhoneNumber() {
		return Prefs.getStringPref(PREF_PHONE_NUMBER, null);
	}

	public static void setPassword(String password) {

		Prefs.setStringPref(PREF_PASSWORD, password);
	}

	public static String getPassword() {
		return Prefs.getStringPref(PREF_PASSWORD, null);
	}

}
