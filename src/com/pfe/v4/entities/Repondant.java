/**
 * 
 */
package com.pfe.v4.entities;


/**
 * @author Anis BOUHACHEM
 * @since 29 Avril 2013
 * 
 */
public class Repondant {

	public static final String TABLE_NAME = "repondant";
	
	private int id;

	private int server_id;

	private String name = "";

	private String last_name= "";

	private int age = 0;

	private String gender = "M";
	
	private long timestamp;
	
	public Repondant(){
		timestamp = System.currentTimeMillis();
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

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
	 * @return the server_id
	 */
	public int getServer_id() {
		return server_id;
	}

	/**
	 * @param server_id
	 *            the server_id to set
	 */
	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name
	 *            the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public static String sqlCreate(){
		return "CREATE TABLE IF NOT EXISTS 'repondant' (" +
				" 'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
				" 'name' varchar(255) NOT NULL," +
				" 'lastname' varchar(255) NOT NULL," +
				" 'age' int(11) NOT NULL, " +
//				"'region' varchar(255) NOT NULL," +
				" 'sexe' varchar(5) NOT NULL ," +
				" 'answertime' int(13) NOT NULL" +
//				" PRIMARY KEY('id')" +
				");";
	}

}
