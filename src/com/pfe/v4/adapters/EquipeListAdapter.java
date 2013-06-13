/**
 * 
 */
package com.pfe.v4.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfe.v4.R;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.entities.Equipe;

/**
 * @author Anis BOUHACHEM
 * @since 12 juin 2013
 */
public class EquipeListAdapter extends BaseAdapter {

	public static ArrayList<Equipe> equipes;
	private Context context;

	public EquipeListAdapter(Context applicationContext) {
		this.context = applicationContext;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return equipes.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return equipes.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		if (convertView != null)
			return convertView;
		
		LayoutInflater inflater = LayoutInflater.from(context);

		View header = inflater
				.inflate(R.layout.equipe_item, null);
		
		LinearLayout usernames = (LinearLayout) header.findViewById(R.id.usernames);
		
		TextView superviseur = new TextView(context);
		
		superviseur.setText(equipes.get(position).getSuperviseur().getName());
		
		superviseur.setBackgroundColor(Color.BLACK);
		superviseur.setTextColor(Color.WHITE);
		usernames.addView(superviseur);
		
		for(int i = 0 ; i<equipes.get(position).getEnqueteurs().size(); i++)
		{
			TextView enqueteur = new TextView(context);
			enqueteur.setText(equipes.get(position).getEnqueteurs().get(i).getName());
			enqueteur.setBackgroundColor(Color.WHITE);
			enqueteur.setTextColor(Color.BLACK);
			
			usernames.addView(enqueteur);
		}
		
		
		
		
		return usernames;
	}

}
