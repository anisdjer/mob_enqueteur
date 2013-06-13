package com.pfe.v4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.pfe.v4.adapters.EnquetesListAdapter;
import com.pfe.v4.entities.Enquete;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.entities.Questionnaire;
import com.pfe.v4.utils.Utils;

public class EnquetesActivity extends Activity {

	private ArrayList<Enquete> enquetesList = new ArrayList<Enquete>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enquetes);
 

		EnqueteLoading enquetesLoading = new EnqueteLoading();
		enquetesLoading.execute();

		EnquetesListAdapter adapter = new EnquetesListAdapter(
				this.getApplicationContext());
		ListView enquetesList = (ListView) findViewById(R.id.enquetes_list);
		enquetesList.setAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Utils.questionStack.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.enquetes, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_update:
			// Toast.makeText(Utils.applicationContext,
			// "Mettre à jour les enquetes", Toast.LENGTH_SHORT).show();
			// TODO : tester la connexion
			new updateAsyncTask().execute();
			break;
		case R.id.new_respondent:
			// Toast.makeText(Utils.applicationContext, "Nouveau repondant",
			// Toast.LENGTH_SHORT).show();
			Intent i = new Intent(Utils.applicationContext,
					RespondentActivity.class);
			startActivity(i);

			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private class EnqueteLoading extends AsyncTask<Void, Enquete, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			enquetesList = (ArrayList<Enquete>) Utils.db.getEnquetesList();
			Log.e("Enquete Loading size enquetes list",
					"" + enquetesList.size());
			if (enquetesList.size() != 0) {
				return null;
			}
			String hostname = "http://"
					+ getResources().getString(R.string.server_address)
					+ getResources().getString(R.string.project_root_name)
					+ getResources().getString(R.string.server_host)
					+ "/rest/enqueteurs/" + Enqueteur.id + "/enquetes";

			InputStream is = null;

			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httppost = new HttpGet(hostname);

			HttpResponse response;

			try {

				response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			String result = "[]";
			try {
				BufferedReader reader1 = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader1.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				Log.e("log_tag_convert",
						"Error converting result " + e.toString());
			}

			Log.d("Enquetes Loading AsyncTask", result);

			enquetesList = new ArrayList<Enquete>();
			try {
				JSONArray enquetes = new JSONArray(result);
				for (int i = 0; i < enquetes.length(); i++) {
					final Enquete enq = new Enquete();
					enq.setId(enquetes.getJSONObject(i).getInt("id"));
					enq.setTitre(enquetes.getJSONObject(i).getString("titre"));
					JSONArray jQuestionnaires = (JSONArray) ((JSONObject) enquetes
							.get(i)).get("questionnaire");
					ArrayList<Questionnaire> questionnaires = new ArrayList<Questionnaire>();
					for (int j = 0; j < jQuestionnaires.length(); j++) {

						Questionnaire questionnaire = new Questionnaire();
						questionnaire.setId((jQuestionnaires.getJSONObject(j))
								.getInt("id"));
						questionnaire.setTitre((jQuestionnaires
								.getJSONObject(j)).getString("titre"));

						questionnaire
								.setDate_creation(jQuestionnaires
										.getJSONObject(j).getLong(
												"date_creation") * 1000);

						questionnaire.setPremierQuestion_id((jQuestionnaires
								.getJSONObject(j)).getInt("premiereQuestion"));

						// Question question = new Question();
						// JSONObject jPremierQuestion =
						// jQuestionnaires.getJSONObject(j).

						questionnaires.add(questionnaire);
					}

					enq.setQuestionnaires(questionnaires);

					publishProgress(enq);

				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Enquete... values) {
			super.onProgressUpdate(values);
			Log.d("progress", values[0].getTitre());
			enquetesList.add(values[0]);

			Utils.db.addEnquete(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			EnquetesListAdapter.enquetes = enquetesList;
			
			
			// TODO : change
			// Utils.helper.deleteAll();
			// for (Enquete e : enquetesList)
			// Utils.helper.addEnquete(e);

			EnquetesListAdapter adapter = new EnquetesListAdapter(
					Utils.applicationContext);
			ListView enquetesListView = (ListView) findViewById(R.id.enquetes_list);
			if (enquetesList.size() == 0)
				Toast.makeText(Utils.applicationContext,
						"Enquetes Liste est vide.", Toast.LENGTH_LONG).show();
			enquetesListView.setAdapter(adapter);

			((ListView) findViewById(R.id.enquetes_list))
					.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.enquetes_status))
					.setVisibility(View.INVISIBLE);

		}
	}

	private class updateAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			((LinearLayout) findViewById(R.id.enquetes_status))
					.setVisibility(View.VISIBLE);
			((ListView) findViewById(R.id.enquetes_list))
					.setVisibility(View.INVISIBLE);
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			Utils.db.truncate();
			new EnqueteLoading().execute();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
//			//
//			((ListView) findViewById(R.id.enquetes_list))
//					.setVisibility(View.VISIBLE);
//			((LinearLayout) findViewById(R.id.enquetes_status))
//					.setVisibility(View.INVISIBLE);
		}

	}

}
