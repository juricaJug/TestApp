package com.example.testapp;

public class NearByPlaceRec {

	String name; 
	String address;
	double lat; 
	double lng;
	
	protected NearByPlaceRec (String name,String address,double lat,double lng){
		super();
		this.name=name;
		this.address=address;
		this.lat=lat;
		this.lng=lng;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public double getLat(){
		return lat;
	}
	
	public double getLng(){
		return lng;
	}
	
}
