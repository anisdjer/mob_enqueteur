package com.pfe.v4.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.pfe.v4.database.DatabaseAdapter;
import com.pfe.v4.entities.Repondant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	public static Utils instance = null;
	
	private Utils(){ }
	
	public Utils(Context context){
		
		if(instance != null) return;
		
		instance = new Utils();
		Utils.applicationContext= context;
		db = new DatabaseAdapter();
		
	}
	
	public static LatLng geoPoint;
	
	public static final String QUESTION_TYPE_CHOICE = "choix_unique";
	
	public static final String QUESTION_TYPE_MULTI_CHOICES = "multi_choices";
	
	public static final String QUESTION_TYPE_TEXT = "texte";
	

	public static Context applicationContext; 
	public static DatabaseAdapter db;
	
	public static List<Integer> questionStack = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static Repondant respondent = null;
	
	
	/**
	 * Testing whether device is connected or not.
	 * @return <b>true</b>: if connected, <b>false</b> else
	 */
	public boolean isOnline() {
		
		ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	/**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
  
          }
          return false;
    }

}
