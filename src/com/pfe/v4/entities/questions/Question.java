/**
 * 
 */
package com.pfe.v4.entities.questions;

import java.util.ArrayList;

import android.view.View;

/**
 * @author Anonymous
 * 
 */
public class Question {

	public static final String TABLE_NAME = "question";
	
	private int id;

	private String texte;

	private String type;

	private boolean obligatoire;

	private int questionSuivante;

	private ArrayList<Choix> choix = new ArrayList<Question.Choix>();

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the texte
	 */
	public String getTexte() {
		return texte;
	}

	/**
	 * @param texte
	 *            the texte to set
	 */
	public void setTexte(String texte) {
		this.texte = texte;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the obligatoire
	 */
	public boolean isObligatoire() {
		return obligatoire;
	}

	/**
	 * @param obligatoire
	 *            the obligatoire to set
	 */
	public void setObligatoire(boolean obligatoire) {
		this.obligatoire = obligatoire;
	}

	/**
	 * @return the questionSuivante
	 */
	public int getQuestionSuivante() {
		return questionSuivante;
	}

	/**
	 * @param questionSuivante
	 *            the questionSuivante to set
	 */
	public void setQuestionSuivante(int questionSuivante) {
		this.questionSuivante = questionSuivante;
	}

	/**
	 * @return the choix
	 */
	public ArrayList<Choix> getChoix() {

		return choix;
	}

	/**
	 * @param choix
	 *            le choix 
	 */
	public void setChoix(ArrayList<Choix> choix) {
		this.choix = choix;
	}

	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'question' ("
				+ " 'id' int(11) NOT NULL, "
				+ " 'texte' varchar(255) NOT NULL,"
				+ " 'obligatoire' int(1) NOT NULL, "
				+
				// " 'questionnaire' int(11) NOT NULL," +
				" 'q_type' varchar(255) NOT NULL, "
				+ " 'questionSuivante_id' int(11) DEFAULT NULL, "
				+ " PRIMARY KEY ('id')" + ");";
	}
	public static class Choix {

		public static final String TABLE_NAME = "choix";
		int id;
		String valeur;
		String type;
		Integer questionAttache = 0;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getValeur() {
			return valeur;
		}

		public void setValeur(String valeur) {
			this.valeur = valeur;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getQuestionAttache() {
			return questionAttache;
		}

		public void setQuestionAttache(Integer questionAttache) {
			this.questionAttache = questionAttache;
		}

		public static String sqlCreate(){
			return "CREATE TABLE IF NOT EXISTS 'choix' ("
					+ " 'id' int(11) NOT NULL, "
					+ " 'valeur' varchar(255) NOT NULL,"
					+ " 'ch_type' varchar(255) NOT NULL, "
					+ " 'question' int(11) NOT NULL,"
					+ " 'attachedQuestion_id' int(11) DEFAULT NULL, "
					+ " PRIMARY KEY ('id')" + ");";
		}
	}
	
	public View getView() {
		// TODO : abstract
		return null;
	}
}
