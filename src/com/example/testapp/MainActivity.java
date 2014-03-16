package com.example.testapp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;



import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends ActionBarActivity implements PlaceDialogFragment.PlacesDialogListener{
	
	private static GoogleMap mMap;
	private static MapView mapView;
	public static List<NearByPlaceRec> result;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	static Location location;
	String mUrl;
	private String API_KEY="AIzaSyDLj-KntqSEs3aTJ137cfd6tWuSc-fC2Ns";
	private SQLiteDatabase db;
	public static String markerTitle;
	public static String markerAddress;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	Uri uriSavedImage;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		/**DataBase instance
		*/
		DataBaseOpenHelper dbOH= new DataBaseOpenHelper(getApplicationContext());
		db=dbOH.getWritableDatabase();
		
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			finish();}
		
		/**Getting last known location
		*/
		location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		
		String locationString=Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude());
		
		/**Starting Async task to download near by places
		*/
		AsyncTaksRunner runner = new AsyncTaksRunner();
		runner.execute(locationString);
		
		
		
		mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location currLocation) {
				// TODO Auto-generated method stub
				location=currLocation;
				
			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		/** If a user click on marker info window app shows dialog thet offer name and address chaning and photo taking
		*/
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			@Override
			public void onInfoWindowClick(Marker marker) {
				markerTitle=marker.getTitle();
				markerAddress=marker.getSnippet();
				showDialog();
				
		        
		    }
			
			
		});
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			MapsInitializer.initialize(getActivity().getApplication() );
			mapView = (MapView) rootView.findViewById(R.id.mapview);
			mapView.onCreate(savedInstanceState);
			/**Crating Google map
			 * 
			 */
			
			if (mMap == null) {
		            mMap = ((MapView) rootView.findViewById(R.id.mapview)).getMap();
			 }
			mMap.getUiSettings().setMyLocationButtonEnabled(false);
			mMap.setMyLocationEnabled(true);
			mMap.animateCamera(CameraUpdateFactory.zoomIn());
			mMap.moveCamera(CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(location.getLatitude(),location.getLongitude())));
						
			return rootView;
		}
	}

	
	
	
	@Override
	protected void onResume(){
		super.onResume();
	
	
		
		
	}

	/** Asyinc task for downloading near by places
	  */
	public class AsyncTaksRunner extends
	AsyncTask<String, Void, List<NearByPlaceRec>> {

@Override
protected List<NearByPlaceRec> doInBackground(String... params) {
	// TODO Auto-generated method stub
	mUrl="https://maps.googleapis.com/maps/api/place/search/json?location="+params[0]+
			"&radius=500&sensor=false&key="+API_KEY;
	HttpGet request = new HttpGet(mUrl);
	AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	JSONResponsHandeler resHandeler=new JSONResponsHandeler();
	try{
		
		return client.execute(request, resHandeler);
		
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
		client.close();
	}
	return null;
}

/** Processing of downloaded results and saving results to database
 * 
 */

protected void onPostExecute(List<NearByPlaceRec> result) {
	if (null != mMap) {
		
		for(NearByPlaceRec rec : result){
			mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
			.position(new com.google.android.gms.maps.model.LatLng(rec.getLat(), rec.getLng()))
			.title(rec.getName())					
			.snippet(rec.getAddress())
			.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			
			ContentValues cv=new ContentValues();			
			cv.put("placeName", rec.getName());
			cv.put("placeAdd", rec.getAddress());
			cv.put("placeLat", rec.getLat());
			cv.put("placeLng", rec.getLng());
			
			db.insert("places", null, cv);
			
		}
	}
	
}

}
	/**
	 *  Show dialog method
	 */
	void showDialog() {
		DialogFragment dialog = new PlaceDialogFragment();
	    dialog.show(getFragmentManager(), "Place");
	}

	/**
	 *  Code that runs if user changes place name or address 
	 *  Saving new name and address to database
	 */
@Override
public void onDialogPositiveClick(DialogFragment dialog, String placeName,
		String placeAdd) {
	// TODO Auto-generated method stub
	if(markerTitle!=placeName || markerAddress!=placeAdd){
		String[] whereArgs=new String[]{markerTitle,markerAddress};
		ContentValues cv=new ContentValues();
		cv.put("placeName", placeName);
		cv.put("placeAdd", placeAdd);
		if(0<db.update("places", cv, "placeName=? AND placeAdd=?", whereArgs)){
			Toast toast=Toast.makeText(getApplicationContext(), "Data Base is updated", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}

/**
 * Code that runs if user chose to take a photo
 */

@Override
public void onDialogNeutralClick(DialogFragment dialog, String placeName) {
	// TODO Auto-generated method stub
	Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
    imagesFolder.mkdirs(); // <----
    File image = new File(imagesFolder, "image_001.jpg");
    uriSavedImage = Uri.fromFile(image);
    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
    startActivityForResult(imageIntent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
}

/**
 *  Uploading photo to Amazon S3 service
 */

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
    	 if (resultCode == RESULT_OK) {
    		
    		File fileToUpload= new File(uriSavedImage.toString());
    		 AmazonS3Client s3Client =   new AmazonS3Client( new BasicAWSCredentials("AKIAIHMFHPPCL24DKE3A","sDUGZnm18ZG8Ci6tdxBIJdkp/QWOuZvrBjMO0vE") );
    		s3Client.createBucket("my_picture_bucket");
    	 	PutObjectRequest por= new PutObjectRequest("my_picture_bucket",markerTitle,fileToUpload);
    	 	s3Client.putObject(por);
    	 	Toast toast=Toast.makeText(getApplicationContext(), "Photo is successfully uploaded", Toast.LENGTH_SHORT);
			toast.show();
    	 }
    	}
    }
@Override
public void onDestroy() {
	db.close();
    db = null;
}

}





	

