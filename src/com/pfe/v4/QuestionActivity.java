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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pfe.v4.entities.questions.Question;
import com.pfe.v4.entities.questions.Question.Choix;
import com.pfe.v4.utils.Utils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class QuestionActivity extends Activity {

	private Question question;
	private int id_question;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		context = this;
		id_question = Utils.questionStack.get(0);// getIntent().getExtras().getInt("id_question");
		Utils.questionStack.remove((Integer) id_question);
		while (id_question == 0 && Utils.questionStack.size() != 0) {
			id_question = Utils.questionStack.get(0);// getIntent().getExtras().getInt("id_question");
			Utils.questionStack.remove((Integer) id_question);
		}
		new QuestionLoading().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	private class QuestionLoading extends AsyncTask<Void, Void, Void> {

		View question_view;

		@Override
		protected Void doInBackground(Void... params) {

			if (id_question == 0)
				return null;

			question = Utils.db.getQuestion(id_question);

			if (question == null) {
				String hostname = "http://"
						+ getResources().getString(R.string.server_address)
						+ getResources().getString(R.string.project_root_name)
						+ getResources().getString(R.string.server_host)
						+ "/rest/questions/" + id_question;

				Log.e("server", hostname);

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

				String result = "{}";
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

				Log.d("Question Loading AsyncTask", result);

				try {
					JSONObject jQuestion = new JSONObject(result);
					question = new Question();
					question.setId(jQuestion.getInt("id"));
					question.setTexte(jQuestion.getString("texte"));
					question.setType(jQuestion.getString("type"));
					question.setObligatoire(jQuestion.getBoolean("obligatoire"));
					question.setQuestionSuivante(jQuestion
							.getInt("questionSuivante"));

					JSONArray jChoix = (JSONArray) jQuestion.get("choix");

					ArrayList<Question.Choix> choix = new ArrayList<Question.Choix>();
					for (int i = 0; i < jChoix.length(); i++) {
						Choix choi = new Question.Choix();
						choi.setId(((JSONObject) jChoix.getJSONObject(i))
								.getInt("id"));
						choi.setValeur(((JSONObject) jChoix.getJSONObject(i))
								.getString("valeur"));
						choi.setType(((JSONObject) jChoix.getJSONObject(i))
								.getString("ch_type"));
						choi.setQuestionAttache(((JSONObject) jChoix
								.getJSONObject(i)).getInt("attachedQuestion"));

						choix.add(choi);
						Log.e("Val choix", ((JSONObject) jChoix
								.getJSONObject(i)).getString("valeur"));
					}

					question.setChoix(choix);

					// TODO : add to database
					Utils.db.addQuestion(question);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			Utils.questionStack.add(0, question.getQuestionSuivante());  

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			question_view = findViewById(R.id.question_view);
			// question_view.setLayoutParams(new RelativeLayout.LayoutParams(
			// RelativeLayout.CENTER_IN_PARENT,
			// RelativeLayout.CENTER_IN_PARENT));
			ViewGroup parent = (ViewGroup) question_view.getParent();
			int index = parent.indexOfChild(question_view);
			parent.removeView(question_view);

			Button next = (Button) findViewById(R.id.next_question);
			if (id_question == 0) {
				next.setText("Questionnaires");
				next.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						((Activity) context).finish();

					}
				});

				// question_view = findViewById(R.id.question_view);
				// question_view.setLayoutParams(new
				// RelativeLayout.LayoutParams(
				// RelativeLayout.CENTER_IN_PARENT,
				// RelativeLayout.CENTER_IN_PARENT));
				// ViewGroup parent = (ViewGroup) question_view.getParent();
				// int index = parent.indexOfChild(question_view);
				// parent.removeView(question_view);

				question_view = new TextView(Utils.applicationContext);
				((TextView) question_view).setText("Fin de questionnaire.");
				parent.addView(question_view, index);

			} else {

				// TODO : Controler le type de question
				question_view = getLayoutInflater().inflate(
						R.layout.question_type_text, parent, false);
				((TextView) question_view.findViewById(R.id.question_text))
						.setText((id_question == 0) ? "Pas de question"
								: question.getTexte()
										+ ((question.isObligatoire()) ? " *"
												: ""));

				if (question.getType().toString()
						.equals(Utils.QUESTION_TYPE_CHOICE)) {
					question_view = getLayoutInflater().inflate(
							R.layout.question_type_choice, parent, false);

					((TextView) question_view.findViewById(R.id.question_text))
							.setText((id_question == 0) ? "Pas de question"
									: question.getTexte()
											+ ((question.isObligatoire()) ? " *"
													: ""));

					RadioGroup choices = (RadioGroup) question_view
							.findViewById(R.id.choices);
					if (question.getChoix().size() > 0) {

						for (int j = 0; j < question.getChoix().size(); j++) {
							RadioButton choix = new RadioButton(
									Utils.applicationContext);
							choix.setTextColor(Color.BLUE);
							choix.setText(question.getChoix().get(j)
									.getValeur());
							choix.setTag(question.getChoix().get(j)
									.getQuestionAttache());
							choix.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									if (isChecked) {
										Utils.questionStack.add(0,
												(Integer) buttonView.getTag());
									} else
										Utils.questionStack
												.remove((Integer) buttonView
														.getTag());
								}

							});

							choices.addView(choix, j);
						}

						if (question.isObligatoire())
							choices.getChildAt(0).performClick();
					}
				} else if (question.getType().toString()
						.equals(Utils.QUESTION_TYPE_MULTI_CHOICES)) {

					question_view = getLayoutInflater()
							.inflate(R.layout.question_type_multi_choices,
									parent, false);

					((TextView) question_view.findViewById(R.id.question_text))
							.setText((id_question == 0) ? "Pas de question"
									: question.getTexte()
											+ ((question.isObligatoire()) ? " *"
													: ""));

					LinearLayout listChoices = (LinearLayout) question_view
							.findViewById(R.id.multi_choices);
					for (int i = 0; i < question.getChoix().size(); i++) {
						CheckBox choi = new CheckBox(Utils.applicationContext);
						choi.setTextColor(Color.BLUE);
						choi.setText(question.getChoix().get(i).getValeur());
						listChoices.addView(choi, i);
					}

				}
				parent.addView(question_view, index);

				// View question_view = findViewById(R.id.question_view);

				next.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (question.isObligatoire()
								&& question.getType().toString()
										.equals(Utils.QUESTION_TYPE_TEXT)) {
							if (TextUtils.isEmpty(((TextView) question_view
									.findViewById(R.id.question_input))
									.getText().toString())) {

								TextView erreur = (TextView) findViewById(R.id.question_error);
								erreur.setText(getResources()
										.getString(
												R.string.question_obligatoire_error_text));

								Toast.makeText(
										Utils.applicationContext,
										getResources()
												.getString(
														R.string.question_obligatoire_error_text),
										Toast.LENGTH_LONG).show();
								((Vibrator) getSystemService(Context.VIBRATOR_SERVICE))
										.vibrate(200);

								return;
							}

						}

						// TODO : Database handler => ajouter dans la base
						// les reponses.
						saveResponses();
						Intent i = new Intent(Utils.applicationContext,
								QuestionActivity.class);
						i.putExtra("id_question", id_question);

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// i.putExtra("id",
						// questionnaires.get(position).getId());
						Utils.applicationContext.startActivity(i);

						((Activity) context).finish();
					}

				});
			}

			((View) findViewById(R.id.contentQuestions))
					.setVisibility(View.VISIBLE);
			((View) findViewById(R.id.questions_status))
					.setVisibility(View.GONE);

		}

		private void saveResponses() {
			if (question.getType().toString().equals(Utils.QUESTION_TYPE_TEXT)) {

				Toast.makeText(
						Utils.applicationContext,
						((TextView) findViewById(R.id.question_input))
								.getText(), Toast.LENGTH_LONG).show();

			} else if (question.getType().toString()
					.equals(Utils.QUESTION_TYPE_CHOICE)) {

				// RadioGroup rg = (RadioGroup) findViewById(R.id.choices);
				//
				// Toast.makeText(Utils.applicationContext,
				// ((TextView)findViewById(R.id.question_input)).getText() ,
				// Toast.LENGTH_LONG).show();
			}
		}
	}

}
