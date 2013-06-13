/**
 * 
 */
package com.pfe.v4.entities;

/**
 * @author Anis BOUHACHEM
 * @since 30 Avril 2013
 *
 */
public class Reponse {
	
	public static final String TABLE_NAME = "reponse";

	private int id;
	
	private String valeur;
	
	private int question;
	
	private int ficheReponse;

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
	 * @return the valeur
	 */
	public String getValeur() {
		return valeur;
	}

	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	/**
	 * @return the question
	 */
	public int getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(int question) {
		this.question = question;
	}

	/**
	 * @return the ficheReponse
	 */
	public int getFicheReponse() {
		return ficheReponse;
	}

	/**
	 * @param ficheReponse the ficheReponse to set
	 */
	public void setFicheReponse(int ficheReponse) {
		this.ficheReponse = ficheReponse;
	}
	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'reponse' (" +
				"  'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
				"  'valeur' varchar(255) NOT NULL," +
				"  'question' int(11) NOT NULL," +
				"  'fiche' int(11) NOT NULL" +
				");";
	}
}
