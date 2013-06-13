package com.pfe.v4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pfe.v4.utils.Utils;

public class GeoActivity extends FragmentActivity {
 
	private GoogleMap mMap;
//	private LatLng geoPoint = new LatLng(53.558, 9.927);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo);

		if (mMap == null) {
 
			mMap  = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)) .getMap();

			if (mMap != null) {
				
				Calendar c = Calendar.getInstance(); 

				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy",Locale.FRANCE);
				String formattedDate = df.format(c.getTime());
				
				
				mMap.addMarker(new MarkerOptions().position(
						Utils.geoPoint).title(formattedDate));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Utils.geoPoint, 13.0f));
			}
		}
	}
	
	@Override
	public void onBackPressed() { 
		super.onBackPressed();
		this.finish();
	}
}
