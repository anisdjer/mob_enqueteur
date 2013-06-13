package com.pfe.v4;



import static com.pfe.v4.gcm.CommonUtilities.SENDER_ID;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.pfe.v4.adapters.GridItemAdapter;
import com.pfe.v4.entities.Enqueteur;

public class ProfileActivity extends Activity {

	Enqueteur enqueteur;
	Context context;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*******************************/
		  
		 
		 /********************************/
		
		
		setContentView(R.layout.activity_profile);

		/*************************************/
		
		 /*************************************/
		
		context = this.getApplicationContext();

		GridView gv = (GridView) findViewById(R.id.gridView);
		gv.setAdapter(new GridItemAdapter(this.getApplicationContext()));

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View tv,
					int position, long id) {
				switch (position) {
				case 0:
					// Tasks
					// Intent ii = new Intent(context, LoginActivity.class);
					// startActivity(ii);
					break;
				case 1:
					// Questionnaires
				{
					Intent i = new Intent(context, EnquetesActivity.class);
					startActivity(i);
				}
					break;
				case 2:
					// Teams
				{
					Intent i = new Intent(context, EquipesActivity.class);
					startActivity(i);
				}
					break;
				case 3:
					// Notif
				{
					Intent i = new Intent(context, NotificationActivity.class);
					startActivity(i);
				}
					break;
				case 4:
					// Stats
				{
					Intent i = new Intent(context, GeoActivity.class);
					startActivity(i);
				}
					break;
				case 5:
					// Exit
					finish();
					break;

				default:
					break;
				}

			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);

		pictureDownload pictureIcon = new pictureDownload();
		pictureIcon.execute("");

		enqueteur = (Enqueteur) this.getIntent().getExtras().get("enqueteur");
		TextView nom = (TextView) findViewById(R.id.profile_name);
		TextView email = (TextView) findViewById(R.id.profile_email);

		nom.setText(enqueteur.getName());
		email.setText(enqueteur.getEmail());
		
		GCMRegistrar.register(this, SENDER_ID);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GCMRegistrar.unregister(this);
	}

	// TODO : Downloading Image
	private static Bitmap download_Image(String url) {

		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			Log.e("Hub", "Error getting the image from server : "
					+ e.getMessage().toString());
		}
		return bm;

	}

	private class pictureDownload extends AsyncTask<String, Void, Bitmap> {

		ImageView mChart = (ImageView) findViewById(R.id.profile_icon);

		@Override
		protected Bitmap doInBackground(String... params) {

			try {
				String URL = "http://"
						+ getResources().getString(R.string.server_address)
						+ getResources().getString(R.string.project_root_name)
						+ "/" + enqueteur.getImage();
				return download_Image(URL);
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (result != null)
				mChart.setImageBitmap(result);
		}
	}

}
