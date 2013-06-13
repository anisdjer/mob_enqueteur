package com.pfe.v4.entities;

/**
 * @author Anis BOUHACHEM
 * @since 30 Avril 2013
 *
 */

public class FicheReponse {
	
	public static final String TABLE_NAME = "fichereponse";
	
	private int id;
	
	private int questionnaire;
	
	private int repondant;
	
	private int enqueteur;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the questionnaire
	 */
	public int getQuestionnaire() {
		return questionnaire;
	}

	/**
	 * @param questionnaire the questionnaire to set
	 */
	public void setQuestionnaire(int questionnaire) {
		this.questionnaire = questionnaire;
	}

	/**
	 * @return the repondant
	 */
	public int getRepondant() {
		return repondant;
	}

	/**
	 * @param repondant the repondant to set
	 */
	public void setRepondant(int repondant) {
		this.repondant = repondant;
	}

	/**
	 * @return the enqueteur
	 */
	public int getEnqueteur() {
		return enqueteur;
	}

	/**
	 * @param enqueteur the enqueteur to set
	 */
	public void setEnqueteur(int enqueteur) {
		this.enqueteur = enqueteur;
	}

	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'fichereponse' (" +
				"  'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
				"  'repondant' int(11) NOT NULL," +
				"  'questionnaire' int(11) NOT NULL," +
				"  'enqueteur' int(11) NOT NULL" +
//				"  PRIMARY KEY ('id') " +
				");";
	}
	
	
}
