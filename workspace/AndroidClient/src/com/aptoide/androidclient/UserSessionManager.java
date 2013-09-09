package com.aptoide.androidclient;

import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager implements Serializable {

	private SharedPreferences pref;

	private Editor editor;

	public static final String PREF_NAME = "aptoideclientprefs";
	
	public static final int PRIVATE_MODE = 0;


	// Shared preferences keys
	public static final String IS_LOGGED = "isLogged";

	public static final String KEY_EMAIL = "email";

	public static final String KEY_USERTOKEN = "usertoken";

	public static final String IS_DEVICE_ASSOCIATED_WITH_ACCOUNT = "isDeviceRegistered";

	public static final String KEY_DEVICE_ID = "device_id";
	
	public static final String KEY_QUEUE_ID = "queue_id";

	
//	public static SharedPreferences getUserSessionSharedPreferences(Context context){
//		return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//	}	
	
	public UserSessionManager(Context context) {
		
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession(String email, String usertoken) {
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_USERTOKEN, usertoken);
		editor.putBoolean(IS_LOGGED, true);
//		editor.putString(KEY_DEVICE_ID, null);
//		editor.putString(KEY_QUEUE_ID, null);
//		editor.putBoolean(IS_DEVICE_ASSOCIATED_WITH_ACCOUNT, false);

		editor.commit();
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGGED, false);
	}

	public void logoutUser() {
		editor.clear();
		editor.commit();
	}

	public void registerDeviceInAccount(String device_id, String queue_id) {
		if (isLoggedIn()) {
			editor.putString(KEY_DEVICE_ID, device_id);
			editor.putString(KEY_QUEUE_ID, queue_id);
			editor.putBoolean(IS_DEVICE_ASSOCIATED_WITH_ACCOUNT, true);

			editor.commit();
		}
	}

	public boolean isDeviceRegistered() {
		return pref.getBoolean(IS_DEVICE_ASSOCIATED_WITH_ACCOUNT, false);
	}

	public void disassociateDevice() {
		editor.remove(KEY_DEVICE_ID);
		editor.remove(IS_DEVICE_ASSOCIATED_WITH_ACCOUNT);

		editor.commit();
	}

	public static boolean isDeviceAlreadyAssociated(Context context) {
		return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE).getBoolean(IS_DEVICE_ASSOCIATED_WITH_ACCOUNT, false);
	}
}
