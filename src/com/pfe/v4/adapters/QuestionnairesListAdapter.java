/**
 * 
 */
package com.pfe.v4.adapters;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pfe.v4.QuestionActivity;
import com.pfe.v4.R;
import com.pfe.v4.RespondentActivity;
import com.pfe.v4.entities.Questionnaire;
import com.pfe.v4.utils.Utils;

/**
 * @author Anonymous
 * 
 */
public class QuestionnairesListAdapter extends BaseAdapter {

	private ArrayList<Questionnaire> questionnaires = new ArrayList<Questionnaire>();
	private Context context;

	public QuestionnairesListAdapter(Context applicationContext,
			ArrayList<Questionnaire> questionnaires) {
		this.context = applicationContext;
		if (questionnaires != null)
			this.questionnaires = questionnaires;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return questionnaires.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return questionnaires.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView != null)
			return convertView;

		// TODO : si le repondant a repondu a un questionnaire => le questionnaire est disabled
		
		LayoutInflater inflater = LayoutInflater.from(context);

		View questionnaire = inflater.inflate(R.layout.questionnaire_item_list,
				null);

		TextView questionnaireText = (TextView) questionnaire
				.findViewById(R.id.questionnaire_text);
		questionnaireText.setText(questionnaires.get(position).getTitre());

		TextView questionnaireDate = (TextView) questionnaire
				.findViewById(R.id.questionnaire_date);

		Timestamp stamp = new Timestamp(questionnaires.get(position)
				.getDate_creation());
		Date date = new Date(stamp.getTime());
		questionnaireDate.setText(date.toString());

		questionnaire.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Utils.respondent == null){
					Intent i = new Intent(Utils.applicationContext, RespondentActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Utils.applicationContext.startActivity(i);
					return;
				}
				
				// Intent i = new Intent(context, QuestionnaireActivity.class);
				
				Intent i = new Intent(context, QuestionActivity.class);
				// i.putExtra("id_question",
				// questionnaires.get(position).getPremierQuestion_id());
				Utils.questionStack.add(0, questionnaires.get(position)
						.getPremierQuestion_id());
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// i.putExtra("id", questionnaires.get(position).getId());
				context.startActivity(i);
			}
		});

		
		return questionnaire;
	}

	

}
