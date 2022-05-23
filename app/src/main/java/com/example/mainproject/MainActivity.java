package com.example.mainproject;

import static android.content.ContentValues.TAG;
import static com.example.mainproject.util.Constants.LOCATION_PERMISSION_REQUEST_CODE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mapsButton = findViewById(R.id.mapButton);
        mapsButton.setOnTouchListener(this);
    }

    public void openMapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //Check for location VIA GPS permissions
    public boolean checkLocationPermission() {
        //Checks if user has given permission
        //if not, go into if statement
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Checks if device should show a message to user
            //if it should, go into statement
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission_popup_text)
                        .setMessage(R.string.text_location_permission_popup_text)
                        .setPositiveButton(R.string.selection_confirm_button_text, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        })
                        .create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            return false;

        } else {

            return true;
        }
    }

    //Listening for onTouch event
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, MotionEvent event) {
        if (checkLocationPermission()) {
            //Checks if map button is pressed
            if (event.getAction() == MotionEvent.ACTION_UP) {
                openMapActivity();
            } else {
                return super.onTouchEvent(event);
            }
            return true;
        }
        return false;
    }

    //Lifecycle callbacks
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume MainActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause MainActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop MainActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart MainActivity");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy MainActivity");
        super.onDestroy();
    }

}