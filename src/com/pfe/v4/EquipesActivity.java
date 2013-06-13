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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.pfe.v4.adapters.EnquetesListAdapter;
import com.pfe.v4.adapters.EquipeListAdapter;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.entities.Equipe;
import com.pfe.v4.entities.Superviseur;
import com.pfe.v4.utils.Utils;

public class EquipesActivity extends Activity {

	private ArrayList<Equipe> equipesList = new ArrayList<Equipe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipes);

		EquipeLoading equipesLoading = new EquipeLoading();
		equipesLoading.execute();

		EquipeListAdapter adapter = new EquipeListAdapter(
				this.getApplicationContext());
		ListView equipesList = (ListView) findViewById(R.id.equipes_list);
		equipesList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.equipes, menu);
		return true;
	}

	private class EquipeLoading extends AsyncTask<Void, Equipe, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			String hostname = "http://"
					+ getResources().getString(R.string.server_address)
					+ getResources().getString(R.string.project_root_name)
					+ getResources().getString(R.string.server_host)
					+ "/rest/enqueteurs/" + Enqueteur.id + "/equipes";

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

			equipesList = new ArrayList<Equipe>();
			try {
				JSONArray equipes = new JSONArray(result);
				for (int i = 0; i < equipes.length(); i++) {
					final Equipe eq = new Equipe();
					eq.setId(equipes.getJSONObject(i).getInt("id"));

					JSONObject JsonSuperviseur = (JSONObject) ((JSONObject)equipes.get(i)).get("superviseur");
					Superviseur superviseur = new Superviseur();

					superviseur.setId(JsonSuperviseur.getInt("id"));
					superviseur.setName(JsonSuperviseur.getString("username"));

					eq.setSuperviseur(superviseur);

					JSONArray jEnqueteurs = (JSONArray) ((JSONObject) equipes
							.get(i)).get("enqueteurs");

					ArrayList<Enqueteur> enqueteurs = new ArrayList<Enqueteur>();

					for (int j = 0; j < jEnqueteurs.length(); j++) {

						Enqueteur enqu = new Enqueteur();

						enqu.setId((jEnqueteurs.getJSONObject(j)).getInt("id"));
						
						enqu.setName((jEnqueteurs.getJSONObject(j))
								.getString("username"));
						 

						enqueteurs.add(enqu);
					}
					
					eq.setEnqueteurs(enqueteurs);

					equipesList.add(eq);
 

				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Equipe... values) {
			super.onProgressUpdate(values);
			
			
 
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			EquipeListAdapter.equipes = equipesList;
 
			EquipeListAdapter adapter = new EquipeListAdapter(
					Utils.applicationContext);
			
			ListView equipesListView = (ListView) findViewById(R.id.equipes_list);
			
			equipesListView.setAdapter(adapter);

			((ListView) findViewById(R.id.equipes_list))
					.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.equipes_status))
					.setVisibility(View.INVISIBLE);
		}

	}
}
