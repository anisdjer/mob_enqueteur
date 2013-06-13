/**
 * 
 */
package com.pfe.v4.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pfe.v4.R;
import com.pfe.v4.entities.Enquete;

/**
 * @author Anonymous
 * 
 */
public class EnquetesListAdapter extends BaseAdapter {

	public static ArrayList<Enquete> enquetes = new ArrayList<Enquete>();;
	private Context context;

	public EnquetesListAdapter(Context applicationContext) {

		this.context = applicationContext;
		// if(enquetes!= null)
		// EnquetesListAdapter.enquetes = (ArrayList<Enquete>) enquetes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {

		return enquetes.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return enquetes.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("InlinedApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView != null)
			return convertView;

		LayoutInflater inflater = LayoutInflater.from(context);

		View header = inflater
				.inflate(R.layout.enquetes_header_item_list, null);
		final TextView headerText = (TextView) header
				.findViewById(R.id.header_item_list);
		headerText.setText(enquetes.get(position).getTitre());

		ListView questionnairesList = (ListView) header
				.findViewById(R.id.questionnaires);

		int height = 60 * enquetes.get(position).getQuestionnaires().size();

		questionnairesList.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height));

		QuestionnairesListAdapter adapter = new QuestionnairesListAdapter(
				context, enquetes.get(position).getQuestionnaires());

		questionnairesList.setAdapter(adapter);

		// TODO : ImageView Click listener
		((ImageView) header.findViewById(R.id.header_item_download))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(
								context,
								"Download " + enquetes.get(position).getTitre(),
								Toast.LENGTH_SHORT).show();
					}
				});
		((ImageView) header.findViewById(R.id.header_item_info))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(
								context,
								"Info " + enquetes.get(position).getTitre(),
								Toast.LENGTH_SHORT).show();
					}
				});
		headerText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View header) {
				Toast.makeText(context, "" + enquetes.get(position).getId(),
						Toast.LENGTH_SHORT).show();
			}
		});
		return header;
	}

}
