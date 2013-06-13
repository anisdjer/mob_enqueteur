package com.pfe.v4;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pfe.v4.adapters.NotificationsAdapter;
import com.pfe.v4.entities.Notification;
import com.pfe.v4.utils.Utils;

public class NotificationActivity extends Activity {

	private ArrayList<Notification> notifsList = new ArrayList<Notification>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

	}

	@Override
	protected void onStart() {
		super.onStart();

		notifsList = (ArrayList<Notification>) Utils.db.getNotifList();

		Log.d("Notifs", "" + notifsList.size());

		NotificationsAdapter adapter = new NotificationsAdapter(notifsList);

		ListView listNotif = (ListView) findViewById(R.id.notifications_list);

		listNotif.setAdapter(adapter);

		((ListView) findViewById(R.id.notifications_list))
				.setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.notifications_status))
				.setVisibility(View.INVISIBLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}

}
