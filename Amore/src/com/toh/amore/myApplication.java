package com.toh.amore;

import android.app.Application;

public class myApplication extends Application {

	private double lat;
	private double lon;

	public double getLat() {
		return lat;
	}

	public void setLat(double lati) {
		lat = lati;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double longi) {
		lon = longi;
	}
}
