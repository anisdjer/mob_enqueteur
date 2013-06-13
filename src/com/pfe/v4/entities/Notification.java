/**
 * 
 */
package com.pfe.v4.entities;


/**
 * @author Anis BOUHACHEM
 * @since 16 mai 2013
 */
public class Notification {
	
	private int id;
	
	private String texte;
	
	private Long date;
	
	private Boolean consulted;
	
	

	/**
	 * @param texte
	 * @param date
	 */
	public Notification(String texte, Long date) {
		 
		this.texte = texte;
		this.date = date;
		this.consulted = false;
	}



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
	 * @return the texte
	 */
	public String getTexte() {
		return texte;
	}



	/**
	 * @param texte the texte to set
	 */
	public void setTexte(String texte) {
		this.texte = texte;
	}



	/**
	 * @return the date
	 */
	public Long getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(Long date) {
		this.date = date;
	}



	/**
	 * @return the consulted
	 */
	public Boolean getConsulted() {
		return consulted;
	}



	/**
	 * @param consulted the consulted to set
	 */
	public void setConsulted(Boolean consulted) {
		this.consulted = consulted;
	}



	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'notification' ("
				+ "  'id' INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "  'texte' varchar(255) NOT NULL,"
				+ "  'date_creation' int(13) NOT NULL,"
				+ "  'consulted' int(1) NOT NULL,"
				+ "  'enqueteur' int(11) NOT NULL"
				//+ "  PRIMARY KEY ('id')" 
				+ ");";
	}
}
