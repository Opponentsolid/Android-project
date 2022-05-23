package com.example.mainproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SaveActivity extends AppCompatActivity {
    //define variables
    private String latData, lonData, addData;
    private TextView add, lon, lat;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //get bundle from extras, sent from previous page
        Bundle bundle = getIntent().getExtras();

        //load and init strings available in the bundle to variables
        latData = bundle.getString("Lat");
        lonData = bundle.getString("Lon");
        addData  = bundle.getString("Add");

        //init sharedpreferences variable
        sharedpreferences = getSharedPreferences("Location data", Context.MODE_PRIVATE);

        //init button variable
        Button save = findViewById(R.id.saveButton);

        //Define textview variables
        add = findViewById(R.id.saveAdd);
        lon = findViewById(R.id.saveLon);
        lat = findViewById(R.id.saveLat);

        //Loads details of location to the page
        displayLocation();

        //on button press, perform actions
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Onclick save location to sharedpreferences, then output to logcat
                saveLocation();
                loadLocation();
            }
        });
    }

    //Outputs data passed to activity via intent from mapsactivity
    public void displayLocation () {
        add.setText(String.format("%s%s", getString(R.string.save_address_text), addData));
        lon.setText(String.format("%s%s", getString(R.string.save_longitude_text), lonData));
        lat.setText(String.format("%s%s", getString(R.string.save_latitude_text), latData));
    }

    //Saves current location and address to shared preferences
    public void saveLocation () {
        File dir = Environment.getDataDirectory();
        Log.e("Directory:", String.valueOf(dir));
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Latitude", latData);
        editor.putString("Longitude", lonData);
        editor.putString("Address", addData);
        editor.apply();
        Toast.makeText(SaveActivity.this,"Location added to preferences",Toast.LENGTH_LONG).show();
    }

    //Loads the data saved to shared preferences as log to show data has inserted correctly
    //to sharedpreferences
    public void loadLocation () {
        SharedPreferences sharedPref = getSharedPreferences("Location data", Context.MODE_PRIVATE);
        String loadLat = sharedPref.getString("Latitude", "");
        String loadLon = sharedPref.getString("Longitude", "");
        String loadAdd = sharedPref.getString("Address", "");
        Log.e("Latitude", loadLat);
        Log.e("Longitude", loadLon);
        Log.e("Address", loadAdd);
    }

    //Lifecycle callbacks
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume SaveActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause SaveActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop SaveActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart SaveActivity");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy SaveActivity");
        super.onDestroy();
    }
}