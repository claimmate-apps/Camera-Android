package com.commonsware.cwac.camera.demo.adpt;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.camer.app.model.Area;
import com.example.claimmate.R;
 
/***** Adapter class extends with ArrayAdapter ******/
public class CustomAdapter extends ArrayAdapter<Area>{
     
    private Activity activity;
    ArrayList<Area> data;

    Area tempValues=null;
    LayoutInflater inflater;
     
    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(
                          Activity activitySpinner, 
                          int textViewResourceId,   
                          ArrayList<Area> objects
                       
                         ) 
     {
        super(activitySpinner, textViewResourceId, objects);
         
        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
   
    
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
      }
 
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
 
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);
         
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (Area) data.get(position);
         
        TextView label        = (TextView)row.findViewById(R.id.text1);
      
         
       
            // Set values for spinner each row 
            label.setText(tempValues.getArea_name());
         
             
      
        return row;
      }
 }