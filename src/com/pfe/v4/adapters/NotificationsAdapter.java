/**
 * 
 */
package com.pfe.v4.adapters;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pfe.v4.R;
import com.pfe.v4.entities.Notification;
import com.pfe.v4.utils.Utils;

/**
 * @author Anis BOUHACHEM
 * @since 17 mai 2013
 */
public class NotificationsAdapter extends BaseAdapter {

	ArrayList<Notification> values;

	public NotificationsAdapter(ArrayList<Notification> values) {

		this.values = values;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView != null)
			return convertView;

		LayoutInflater inflater = LayoutInflater.from(Utils.applicationContext);

		convertView = inflater.inflate(R.layout.notif_item, null);

		TextView notifTexte = (TextView) convertView
				.findViewById(R.id.notif_texte);
		TextView notifDate = (TextView) convertView
				.findViewById(R.id.notif_date);

		notifTexte.setText(values.get(position).getTexte());
		notifTexte.setTextColor(Color.RED);

		Timestamp stamp = new Timestamp(values.get(position).getDate());
		Date date = new Date(stamp.getTime());
		
		
		notifDate.setText(date.toString());
		notifDate.setTextColor(Color.RED);
		return convertView;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Notification getItem(int arg0) {
		return values.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

}
