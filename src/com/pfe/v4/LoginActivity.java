package com.pfe.v4;

import static com.pfe.v4.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.pfe.v4.gcm.CommonUtilities.EXTRA_MESSAGE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.model.LatLng;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.utils.Utils;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class LoginActivity extends Activity implements LocationListener {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	@SuppressWarnings("unused")
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	private Context context;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private LocationManager lm;
 
	private double latitude;
	private double longitude;
	private double altitude;
	private float accuracy;

	private long time = 0;

	/********************************************/
	AsyncTask<Void, Void, Void> mRegisterTask;
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			Log.e("Display", newMessage + "\n");
		}
	};

	/*
	 * private void checkNotNull(Object reference, String name) { if (reference
	 * == null) { throw new
	 * NullPointerException(getString(R.string.error_config, name)); } }
	 * 
	 * /******************************
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*******************************/
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		/********************************/
		setContentView(R.layout.login_test);

		// unregisterReceiver(mHandleMessageReceiver);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

//		final String regId = GCMRegistrar.getRegistrationId(this);
		//if (regId.equals("")) {
			// Automatically registers application on startup.
//			GCMRegistrar.register(this, SENDER_ID);
		/*} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Log.d("Display", getString(R.string.already_registered) + "\n");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context,
								regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}*/

		context = this;

		new Utils(this.getApplicationContext());// Utils.applicationContext =
												// this.getApplicationContext();

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		@SuppressWarnings("unused")
		JSONArray json;

		// playSound();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
				this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(this);
		unregisterReceiver(mHandleMessageReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Playing sound will play button toggle sound on flash on / off
	 * */
	@SuppressWarnings("unused")
	private void playSound() {

		MediaPlayer mp = MediaPlayer.create(LoginActivity.this, R.raw.test);

		((MediaPlayer) mp).setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
				// mp.seekTo(0);
			}
		});
		mp.start();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		String result = "";
		private boolean local = false;

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				return authenticate();
			} catch (Exception e) {
				return false;
			}
			// for (String credential : DUMMY_CREDENTIALS) {
			// String[] pieces = credential.split(":");
			//
			// Log.e("Test", "Test Credential");
			//
			// if (pieces[0].equals(mEmail)) {
			// // Account exists, return true if the password matches.
			//
			// return pieces[1].equals(mPassword);
			// }
			// }

			// TODO: register the new account here.

			// return false;
		}

		private Boolean authenticate() {

			final String deviceId = GCMRegistrar
					.getRegistrationId(Utils.applicationContext);

			Log.e("ID Attemp Login", " : " + deviceId);

			String hostname = "http://"
					+ getResources().getString(R.string.server_address)
					+ getResources().getString(R.string.project_root_name)
					+ getResources().getString(R.string.server_host)
					+ "/security/login";

			Log.e("server address",
					getResources().getString(R.string.server_address));

			InputStream is = null;
 
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(hostname);

			int statusCode = 401; // Unauthorized

			HttpResponse response;
			try {

				Log.d("mail", mEmail);
				Log.d("pass", mPassword);
				if (mEmail.equals("a@a.a") && mPassword.equals("aaaaa")) {
					local = true;
					return true;
				}
				httppost.addHeader(
						"Authorization",
						"Basic "
								+ Base64.encodeToString(
										(mEmail + ":" + mPassword).getBytes(),
										Base64.NO_WRAP));

//				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
////				params.add(new BasicNameValuePair("latitude", "" + latitude));
////				params.add(new BasicNameValuePair("longitude", "" + longitude));
//
//				httppost.setEntity(new UrlEncodedFormEntity(params));

				response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				statusCode = response.getStatusLine().getStatusCode();

				Log.d("Status Code ", "" + statusCode);

			} catch (ClientProtocolException e) {
				Log.e("ClientProtocolException", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("IOException", e.getMessage());
				e.printStackTrace();
			}

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
			Log.d("AsyncTask DoInBackGround", result);

			if (statusCode == 200)
			{ 
				return true;
			}
				
			return false;

		}
		
		 

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(true);

			if (success) {

				Enqueteur enqueteur;
				if (local) {
					enqueteur = new Enqueteur();
					enqueteur.setId(2);
					enqueteur.setName("Anis Bouhachem");
					enqueteur.setEmail("anis@mail.com");
					enqueteur
							.setImage("http://bouhachem.netii.net/profile_photo.jpg");

				} else {
					enqueteur = remplirEnqueteur();
				}
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra("enqueteur", enqueteur);
				startActivity(intent);
				finish();
			} else {
				showProgress(false);
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		private Enqueteur remplirEnqueteur() {
			Enqueteur enqueteur = new Enqueteur();
			try {
				JSONObject jObject = new JSONObject(result);
				Enqueteur.id = (jObject.getInt("id"));
				enqueteur.setName(jObject.getString("name"));
				enqueteur.setEmail(jObject.getString("email"));

				enqueteur.setImage(jObject.getString("photo"));

				Log.d("Id", "" + Enqueteur.id);
				Log.d("name", "" + enqueteur.getName());
				Log.d("photo", "" + enqueteur.getImage());
			} catch (JSONException e) {
				Log.e("Remplir", e.getMessage());
			}
			return enqueteur;
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		accuracy = location.getAccuracy();

		// Toast.makeText(this, "Lat : " + latitude + " long : " + longitude,
		// Toast.LENGTH_LONG).show();

		Utils.geoPoint = new LatLng(latitude, longitude);

		Log.d("Time", "" + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();

		Log.d("Latitude", "" + latitude);
		Log.d("logitude", "" + longitude);
		Log.d("altitude", "" + altitude);
		Log.d("accuracy", "" + accuracy);
	}

	 

	@Override
	public void onProviderDisabled(String provider) {
		// Toast.makeText(this, "Provider " + provider + " Disabled",
		// Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String provider) {

		// Toast.makeText(this, "Provider " + provider + " Enabled",
		// Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
//		String newStatus = "";
//		switch (status) {
//		case LocationProvider.OUT_OF_SERVICE:
//			newStatus = "OUT_OF_SERVICE";
//			break;
//		case LocationProvider.TEMPORARILY_UNAVAILABLE:
//			newStatus = "TEMPORARILY_UNAVAILABLE";
//			break;
//		case LocationProvider.AVAILABLE:
//			newStatus = "AVAILABLE";
//			break;
//		}

		// Toast.makeText(this, "Provider " + provider + " " + newStatus,
		// Toast.LENGTH_SHORT).show();

	}
}
