package com.example.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class PlaceDialogFragment extends DialogFragment {
	EditText placeName;
	EditText placeAdd;
	public interface PlacesDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String placeName, String placeAdd);
        public void onDialogNeutralClick(DialogFragment dialog, String placeName);
    }
    
    // Use this instance of the interface to deliver action events
    PlacesDialogListener mListener;
	
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PlacesDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 View view=inflater.inflate(R.layout.dialog, null);
		 
		 placeName=(EditText) view.findViewById(R.id.placeName);
		 placeName.setText(MainActivity.markerTitle);
		 placeAdd=(EditText) view.findViewById(R.id.placeAdd);
		 placeAdd.setText(MainActivity.markerAddress);
		 builder.setView(view)
		 .setPositiveButton("Save", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
				 mListener.onDialogPositiveClick(PlaceDialogFragment.this,placeName.getText().toString(), placeAdd.getText().toString());
				
			}
			 
		 })
		 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   PlaceDialogFragment.this.getDialog().cancel();
               }
           })
          .setNeutralButton("Take photo", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				mListener.onDialogNeutralClick(PlaceDialogFragment.this, placeName.getText().toString());
			}
			 
		 });
		 
		
		return builder.create();
		
	}

}
