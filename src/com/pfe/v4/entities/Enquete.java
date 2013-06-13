package com.pfe.v4.entities;

import java.util.ArrayList;

public class Enquete {

	public static final String TABLE_NAME = "enquete";
	
	private int id;

	private String titre;

	private ArrayList<Questionnaire> questionnaires;

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
	 * @return the titre
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * @param titre
	 *            the titre to set
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * @return the questionnaires
	 */
	public ArrayList<Questionnaire> getQuestionnaires() {
		// questionnaires = (ArrayList<Questionnaire>)
		// Utils.helper.GetQuestionnaireData(this);

		if (questionnaires == null)
			return new ArrayList<Questionnaire>();

		return questionnaires;
	}

	/**
	 * @param questionnaires
	 *            the questionnaires to set
	 */
	public void setQuestionnaires(ArrayList<Questionnaire> questionnaires) {

		this.questionnaires = questionnaires;

	}

	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'enquete' ("
				+ "  'id' int(11) NOT NULL,"
				+ "  'titre' varchar(255) NOT NULL,"
				// + "  'date_creation' date NOT NULL,"
				// + "  'campagne' int(11) NOT NULL,"
				+ "  'enqueteur' int(11) NOT NULL,"
				+ "  PRIMARY KEY ('id')" + ");";
	}
 

}
