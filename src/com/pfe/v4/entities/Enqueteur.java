package com.pfe.v4.entities;

import java.io.Serializable;

public class Enqueteur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int id;

	private int id_enq;
	private String name;
	
	private String email;
	
	private String image;

	/**
	 * @return the id
	 */
	public int getId() {
		return id_enq;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id_enq = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
}
