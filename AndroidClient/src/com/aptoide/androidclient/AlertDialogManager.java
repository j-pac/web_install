package com.aptoide.androidclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
	
	public void showAlertDialog(Context context, String title, String message, boolean status) {
		AlertDialog alert_dialog = new AlertDialog.Builder(context).create();
		
		alert_dialog.setTitle(title);
		alert_dialog.setMessage(message);
		
//		alert_dialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
		
		alert_dialog.show();
	}

}
