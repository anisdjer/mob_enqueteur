package com.pfe.v4;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pfe.v4.entities.Repondant;
import com.pfe.v4.utils.Utils;

public class RespondentActivity extends Activity implements
		OnItemSelectedListener {

	EditText nameView, lastNameView, ageView;
	Spinner genderView;

	Repondant respondent;
	
	RespondentActivity context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_respondent);

		 context= this;

		respondent = new Repondant();

		nameView = (EditText) findViewById(R.id.respondent_name);
		lastNameView = (EditText) findViewById(R.id.respondent_lastname);
		ageView = (EditText) findViewById(R.id.respondent_age);
		genderView = (Spinner) findViewById(R.id.respondent_gender);

		genderView.setOnItemSelectedListener(context);

		Button valider = (Button) findViewById(R.id.respondent_validate);
		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (valid_data()) {
					Utils.respondent = respondent;
					Utils.db.saveRespondent(respondent);
					context.finish();
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.respondent, menu);
		return true;
	}

	private boolean valid_data() {
		// TextUtils.isEmpty(mPassword)
		boolean valid = false;

		nameView.setError(null);
		lastNameView.setError(null);
		ageView.setError(null);

		// name = nameView.getText().toString();
		// lastName = lastNameView.getText().toString();
		// age = Integer.valueOf(ageView.getText().toString());
		if ((!TextUtils.isEmpty(nameView.getText().toString())
				&& (!TextUtils.isEmpty(lastNameView.getText().toString())) && (!TextUtils
					.isEmpty(ageView.getText().toString())))) {
			valid = true;

			respondent.setName(nameView.getText().toString());
			respondent.setLast_name(lastNameView.getText().toString());
			respondent.setAge(Integer.valueOf(ageView.getText().toString()));

		} else {
			if (TextUtils.isEmpty(nameView.getText().toString()))
				nameView.setError(getResources().getString(
						R.string.respondent_name_emty_error));
			if (TextUtils.isEmpty(lastNameView.getText().toString()))
				lastNameView.setError(getResources().getString(
						R.string.respondent_lastname_emty_error));
			if (TextUtils.isEmpty(ageView.getText().toString()))
				ageView.setError(getResources().getString(
						R.string.respondent_age_emty_error));
		}
		return valid;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String gender = ((TextView) arg1).getText().toString();
		if (gender.equals(getResources().getString(R.string.gender_male)))
			respondent.setGender("M");
		else
			respondent.setGender("F");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		respondent.setGender("M");

	}
}
