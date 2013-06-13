package com.pfe.v4.entities;



public class Questionnaire {

	public static final String TABLE_NAME = "questionnaire";
	
	private int id;
	
	private String titre;
	
	private Long date_creation;
	
	private int premierQuestion_id;
  
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
	 * @return the titre
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * @param titre the titre to set
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * @return the date_creation
	 */
	public Long getDate_creation() {
		return date_creation;
	}

	/**
	 * @param date_creation the date_creation to set
	 */
	public void setDate_creation(Long date_creation) {
		this.date_creation = date_creation;
	}

	/**
	 * @return the premierQuestion_id
	 */
	public int getPremierQuestion_id() {
		return premierQuestion_id;
	}

	/**
	 * @param premierQuestion_id the premierQuestion_id to set
	 */
	public void setPremierQuestion_id(int premierQuestion_id) {
		this.premierQuestion_id = premierQuestion_id;
	}
	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'questionnaire' ("
				+ "  'id' int(11) NOT NULL ,"
				+ "  'titre' varchar(255) NOT NULL,"
				+ "  'date_creation' int(13) NOT NULL,"
				+ "  'enquete' int(11) NOT NULL,"
				+ "  'premiereQuestion_id' int(11) DEFAULT NULL,"
				+ "  PRIMARY KEY ('id')" + ");";
	}
}
