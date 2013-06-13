/**
 * 
 */
package com.pfe.v4.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pfe.v4.entities.Enquete;
import com.pfe.v4.entities.questions.Question;
import com.pfe.v4.entities.questions.Question.Choix;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.entities.FicheReponse;
import com.pfe.v4.entities.Notification;
import com.pfe.v4.entities.Questionnaire;
import com.pfe.v4.entities.Repondant;
import com.pfe.v4.entities.Reponse;
import com.pfe.v4.utils.Utils;

/**
 * @author Anis BOUHACHEM
 * 
 */

public class DatabaseAdapter {

	DataBaseHelper DBHelper;
	SQLiteDatabase db;

	public DatabaseAdapter() {
		DBHelper = new DataBaseHelper();
	}

	public DatabaseAdapter open() {

		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {

		try {
			db.close();
		} catch (Exception e) {
		}
	}

	public void truncate() {
		open();
		db.execSQL("DELETE FROM '" + Reponse.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + Repondant.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + FicheReponse.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + Choix.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + Question.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + Questionnaire.TABLE_NAME + "'");
		db.execSQL("DELETE FROM '" + Enquete.TABLE_NAME + "'");
		close();
	}

	public void addEnquete(Enquete enq) {

		open();
		ContentValues values1 = new ContentValues();

		values1.put("id", Integer.toString(enq.getId()));
		values1.put("titre", enq.getTitre());
		values1.put("enqueteur", Enqueteur.id);

		for (Questionnaire q : enq.getQuestionnaires()) {

			ContentValues values2 = new ContentValues();

			values2.put("id", Integer.toString(q.getId()));
			values2.put("titre", q.getTitre());
			values2.put("date_creation", q.getDate_creation());
			values2.put("premiereQuestion_id", q.getPremierQuestion_id());

			values2.put("enquete", Integer.toString(enq.getId()));

			db.insert("questionnaire", null, values2);
		}

		db.insert("enquete", null, values1);
		close();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Enquete> getEnquetesList() {

		ArrayList<Enquete> enquetesList = new ArrayList<Enquete>();
		
		try {
			open();
			Cursor enqueteCursor = db.rawQuery("SELECT id, titre FROM "
					+ Enquete.TABLE_NAME + " where enqueteur=" + Enqueteur.id,
					null);

			if (enqueteCursor.getCount() == 0) {

				enqueteCursor.close();
				return new ArrayList<Enquete>();
			}

			enqueteCursor.moveToFirst();

			while (!enqueteCursor.isAfterLast()) {
				Enquete enq = new Enquete();
				enq.setId(enqueteCursor.getInt(0));
				enq.setTitre(enqueteCursor.getString(1));

				ArrayList<Questionnaire> questionnairesList = new ArrayList<Questionnaire>();
				Cursor questionnaireCursor = db.rawQuery(
						"SELECT id, titre, premiereQuestion_id, date_creation FROM "
								+ Questionnaire.TABLE_NAME + " where enquete="
								+ enq.getId(), null);

				if (questionnaireCursor.getCount() != 0) {

					questionnaireCursor.moveToFirst();
					while (!questionnaireCursor.isAfterLast()) {
						Questionnaire quest = new Questionnaire();
						quest.setId(questionnaireCursor.getInt(0));
						quest.setTitre(questionnaireCursor.getString(1));
						quest.setPremierQuestion_id(questionnaireCursor
								.getInt(2));
						quest.setDate_creation(questionnaireCursor.getLong(3));
						questionnairesList.add(quest);
						questionnaireCursor.moveToNext();
					}
				}
				questionnaireCursor.close();
				enq.setQuestionnaires(questionnairesList);
				enquetesList.add(enq);
				enqueteCursor.moveToNext();
			}
			enqueteCursor.close();
		} finally {
			close();
		}
		return enquetesList;
	}

	/**
	 * 
	 * @param ques
	 * @return
	 */
	public void addQuestion(Question ques) {
		open();
		ContentValues questionValues = new ContentValues();

		questionValues.put("id", ques.getId());
		questionValues.put("texte", ques.getTexte());
		questionValues.put("obligatoire", (ques.isObligatoire()) ? 1 : 0);
		questionValues.put("q_type", ques.getType());
		questionValues.put("questionSuivante_id", ques.getQuestionSuivante());

		for (Question.Choix choi : ques.getChoix()) {
			ContentValues choiValues = new ContentValues();
			choiValues.put("id", choi.getId());
			choiValues.put("valeur", choi.getValeur());
			choiValues.put("ch_type", choi.getType());
			choiValues.put("question", ques.getId());
			choiValues.put("attachedQuestion_id", choi.getQuestionAttache());

			db.insert(Choix.TABLE_NAME, null, choiValues);
		}

		db.insert(Question.TABLE_NAME, null, questionValues);
		close();
	}

	/**
	 * 
	 * @param id_question
	 * @return
	 */
	public Question getQuestion(int id_question) {

		open();
		Cursor questionCur = db.rawQuery(
				"SELECT id, texte, obligatoire, q_type, questionSuivante_id FROM "
						+ Question.TABLE_NAME + " where id=" + id_question,
				null);

		Question question = null;

		if (questionCur.moveToFirst()) {
			question = new Question();
			question.setId(questionCur.getInt(0));
			question.setTexte(questionCur.getString(1));
			question.setObligatoire((questionCur.getInt(2) == 0) ? false : true);
			question.setType(questionCur.getString(3));
			question.setQuestionSuivante(questionCur.getInt(4));

			Cursor choixCur = db.rawQuery(
					"SELECT id, valeur, ch_type, attachedQuestion_id FROM "
							+ Choix.TABLE_NAME + " where choix.question="
							+ id_question, null);

			ArrayList<Question.Choix> choixList = new ArrayList<Question.Choix>();

			if (choixCur.moveToFirst()) {
				while (!choixCur.isAfterLast()) {
					Choix choi = new Question.Choix();

					choi.setId(choixCur.getInt(choixCur.getColumnIndex("id")));
					choi.setValeur(choixCur.getString(choixCur
							.getColumnIndex("valeur")));
					choi.setType(choixCur.getString(choixCur
							.getColumnIndex("ch_type")));
					choi.setQuestionAttache(choixCur.getInt(choixCur
							.getColumnIndex("attachedQuestion_id")));

					choixList.add(choi);
					choixCur.moveToNext();
				}
			}
			choixCur.close();
			question.setChoix(choixList);
		}
		questionCur.close();
		close();
		return question;
	}

	public void saveRespondent(Repondant respondent) {
		Log.d("Respondent", respondent.getName());
		Log.d("Respondent", respondent.getLast_name());
		Log.d("Respondent", "" + respondent.getAge());
		Log.d("Respondent", respondent.getGender());

		open();

		ContentValues respondentValues = new ContentValues();

		respondentValues.putNull("id");
		respondentValues.put("name", respondent.getName());
		respondentValues.put("lastname", respondent.getLast_name());
		respondentValues.put("age", respondent.getAge());
		respondentValues.put("sexe", respondent.getGender());
		respondentValues.put("answertime", respondent.getTimestamp());

		db.insert(Repondant.TABLE_NAME, null, respondentValues);

		// get the Id of repondant
		Cursor repCur = db.rawQuery(
				"SELECT id FROM " + Repondant.TABLE_NAME + " where answertime="
						+ respondentValues.getAsLong("answertime"), null);

		repCur.moveToFirst();
		if (!repCur.isAfterLast())
			Utils.respondent.setId(repCur.getInt(repCur.getColumnIndex("id")));

		Log.e("Save Repondent : Repondant Id", "" + Utils.respondent.getId());

		close();

	}

	/**
	 * 
	 * @return List of questionnaire Ids answered by actual respondent
	 */
	public ArrayList<Integer> getAnsweredQuestionnaire() {

		ArrayList<Integer> result = new ArrayList<Integer>();
		open();

		Cursor ficheCur = db.rawQuery("SELECT questionnaire FROM "
				+ FicheReponse.TABLE_NAME + " where repondant="
				+ Utils.respondent.getId(), null);

		if (ficheCur.moveToFirst()) {
			while (!ficheCur.isAfterLast()) {
				result.add(ficheCur.getInt(ficheCur
						.getColumnIndex("questionnaire")));
				ficheCur.moveToNext();
			}
		}

		ficheCur.close();
		close();
		return result;
	}

	public void saveNotif(Notification notif) {
		open();

		Log.d("Save Notif", "Presave");
		ContentValues respondentValues = new ContentValues();

		respondentValues.putNull("id");
		respondentValues.put("texte", notif.getTexte());
		respondentValues.put("date_creation", notif.getDate());
		respondentValues.put("consulted", (notif.getConsulted()) ? 1 : 0);
		respondentValues.put("enqueteur", Enqueteur.id);

		db.insert("notification", null, respondentValues);

		close();
	}

	public ArrayList<Notification> getNotifList() {
		ArrayList<Notification> notifs = new ArrayList<Notification>();
		open();

		Cursor notifCur = db.rawQuery(
				"SELECT * FROM notification where enqueteur=" + Enqueteur.id,
				null);

		if (notifCur.moveToFirst()) {
			while (!notifCur.isAfterLast()) {
				Notification not = new Notification(null, null);
				not.setId(notifCur.getInt(0));
				not.setTexte(notifCur.getString(1));
				not.setDate(notifCur.getLong(2));
				not.setConsulted((notifCur.getInt(3) == 0) ? false : true);
				notifs.add(not);
				notifCur.moveToNext();
			}
		}

		notifCur.close();
		return notifs;

	}

	public class DataBaseHelper extends SQLiteOpenHelper {

		private final static String DATABASE_NAME = "smartenqueteures.db";

		public DataBaseHelper() {
			super(Utils.applicationContext, DATABASE_NAME, null, 1);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			Log.d(DATABASE_NAME, "PreCreate");

			db.execSQL(Notification.sqlCreate());

			db.execSQL(Enquete.sqlCreate());

			db.execSQL(Questionnaire.sqlCreate());

			db.execSQL(Question.sqlCreate());

			db.execSQL(Question.Choix.sqlCreate());

			db.execSQL(Repondant.sqlCreate());

			db.execSQL(Reponse.sqlCreate());

			db.execSQL(FicheReponse.sqlCreate());

			db.execSQL("CREATE TABLE IF NOT EXISTS 'choix_reponse' ("
					+ "  'choix' int(11) NOT NULL,"
					+ "  'reponse' int(11) NOT NULL,"
					+ "  PRIMARY KEY ('choix','reponse')" + ");");

			Log.d(DATABASE_NAME, "PostCreate");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS '" + Repondant.TABLE_NAME + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + FicheReponse.TABLE_NAME + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + Reponse.TABLE_NAME + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + Choix.TABLE_NAME + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + Question.TABLE_NAME + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + Questionnaire.TABLE_NAME
					+ "'");
			db.execSQL("DROP TABLE IF EXISTS '" + Enquete.TABLE_NAME + "'");
			onCreate(db);
		}

	}

}