package com.pfe.v4.entities;

import java.util.ArrayList;


public class Equipe {
	int id;
	Superviseur superviseur;
	
	ArrayList<Enqueteur> enqueteurs;
	
	
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
	 * @return the superviseur
	 */
	public Superviseur getSuperviseur() {
		return superviseur;
	}


	/**
	 * @param superviseur the superviseur to set
	 */
	public void setSuperviseur(Superviseur superviseur) {
		this.superviseur = superviseur;
	}


	/**
	 * @return the enqueteurs
	 */
	public ArrayList<Enqueteur> getEnqueteurs() {
		return enqueteurs;
	}


	/**
	 * @param enqueteurs the enqueteurs to set
	 */
	public void setEnqueteurs(ArrayList<Enqueteur> enqueteurs) {
		this.enqueteurs = enqueteurs;
	}


	 

 
}
