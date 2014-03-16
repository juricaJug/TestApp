package com.example.testapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;


/**
 * 
 * @author Administrator
 *Class used to parse JSON result from Google Places server response
 */
public class JSONResponsHandeler implements
		ResponseHandler<List<NearByPlaceRec>> {
	@Override
	public List<NearByPlaceRec> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub

		
		List<NearByPlaceRec> result = new ArrayList<NearByPlaceRec>();
		String JSONResponse = new BasicResponseHandler()
		.handleResponse(response);
		
		try{
			
			JSONObject responseObject = (JSONObject) new JSONTokener(
					JSONResponse).nextValue();
			JSONArray places = responseObject
					.getJSONArray("results");
			
			for(int i=0;i<places.length();i++){
				JSONObject place = (JSONObject) places.get(i);
				
				String name=place.get("name").toString();
				String add=place.get("vicinity").toString();
				JSONObject geometry = place.getJSONObject("geometry");
				JSONObject location = geometry.getJSONObject("location");
				double lat=Double.parseDouble(location.get("lat").toString());
				double lng=Double.parseDouble(location.get("lng").toString());						
				result.add(new NearByPlaceRec(name,add,lat,lng));
				
			}
			
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}

}
