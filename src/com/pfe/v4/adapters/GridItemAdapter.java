package com.pfe.v4.adapters;

import com.pfe.v4.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class GridItemAdapter extends BaseAdapter implements ListAdapter {

	private Context context;
	
	int [] images = {
			R.drawable.tasks_icon,R.drawable.question_icon,R.drawable.team_icon,
			R.drawable.notif_icon,R.drawable.stat_icon,R.drawable.logout_icon
	};
	String [] titles = {
		"Tasks","Questionnaires","Teams",
		"Notif","Stats","Logout"
	};

	public GridItemAdapter(Context applicationContext) {
		context = applicationContext;
	}

	@Override
	public int getCount() { 
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}
 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		TextView tv ;
		
		
		
		if(convertView != null){
			tv = (TextView) convertView;
		}
		else{
			
			tv = new TextView(context);
			tv.setText(titles[position]);
			tv.setGravity(Gravity.CENTER);
			tv.setCompoundDrawablesWithIntrinsicBounds(0, images[position], 0, 0);

			
		}
		 
		return tv;
	}

}
