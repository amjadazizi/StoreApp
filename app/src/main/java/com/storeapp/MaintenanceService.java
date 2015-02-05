package com.storeapp;

import com.storeapp.db.CommunicationManager;
import com.storeapp.db.DbManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class MaintenanceService extends IntentService {

	public MaintenanceService() {
		super("MaintenanceService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		if (Prefs.isDeletingOldMessagesEnabled()) {

			int days = Prefs.getDeletingOldMessagesDays();

			CommunicationManager com = DbManager.getDbManager()
					.getCommunicationManager();
			com.deleteMessages(days);
		}

	}
}
