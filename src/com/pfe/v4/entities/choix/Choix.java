package com.pfe.v4.entities.choix;

public class Choix {

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
