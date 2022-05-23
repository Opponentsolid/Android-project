package com.example.mainproject;

import static android.content.ContentValues.TAG;
import static com.example.mainproject.util.Constants.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mainproject.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    //Create variables
    String latid, longi, addre, count;
    TextView country, address, longitude, latitude;
    LatLng loc;
    Button button, buttonToSave;
    GoogleMap mMap;
    ActivityMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;

    //Initialises the base code for the Map activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Get layout elements
        button = findViewById(R.id.get_location_button);
        buttonToSave = findViewById(R.id.buttonToSave);
        country = findViewById(R.id.country);
        address = findViewById(R.id.address);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        //Get available location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Set on click listener for button to get current location
        button.setOnClickListener(view -> getLastLocation());
        //Set on click listener for button to go to next page,
        //requires output from getLastLocation();
        buttonToSave.setOnClickListener(view -> {
            if (loc != null) {
                openSaveActivity();
            } else {
                Toast.makeText(MapsActivity.this, "Cannot proceed without values", Toast.LENGTH_SHORT).show();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //Map fragment cannot be null
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    //Code to open the next activity in the stack
    public void openSaveActivity() {
        //Sets intent from MapsActivity to SaveActivity in variable
        Intent intent = new Intent(MapsActivity.this, SaveActivity.class);
        //Sets a new bundle
        Bundle bundle = new Bundle();
        //Insert lat and lon variables into bundle
        bundle.putString("Lat", latid);
        bundle.putString("Lon", longi);
        bundle.putString("Add", addre);
        //sets extras to the main intent
        intent.putExtras(bundle);
        //starts the next activity in the stack with intent included
        startActivity(intent);
    }

    //Code to get the users current location based on GPS
    @SuppressLint("SetTextI18n")
    private void getLastLocation() {
        //Checks if we have permission, if not exit
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Sets onsuccesslistener for location, getting a provider launches the internal code
            //Normalises content
//If location is found, launch internal code
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                //if location variable is not null
                if (location != null) {
                    //Set a new Geocoder to get locale information
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    //Set a new List for addresses
                    List<Address> addresses = null;
                    //Set a new variable to get Longitude and Latitude
                    loc = new LatLng(location.getLatitude(), location.getLongitude());
                    //Try internal code
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //If addresses isn't null, run internal code
                        if (addresses != null) {
                            //set variables
                            latid = String.valueOf(addresses.get(0).getLatitude());
                            longi = String.valueOf(addresses.get(0).getLongitude());
                            addre = addresses.get(0).getAddressLine(0);
                            count = addresses.get(0).getCountryName();
                            //set textviews to match new data
                            latitude.setText(getString(R.string.map_latitude_text) + latid);
                            longitude.setText(getString(R.string.map_longitude_text) + longi);
                            address.setText(getString(R.string.map_address_text) + addre);
                            country.setText(getString(R.string.map_country_text) + count);
                            //set text on pointer
                            mMap.addMarker(new MarkerOptions().position(loc).title("I am here"));
                            //move map marker
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 15);
                            mMap.animateCamera(cameraUpdate);
                        }
                    } catch (IOException e) {
                        //if try catch fails, print to log
                        e.printStackTrace();
                    }


                }
            });
        } else {
            //if permission denied, request permission
            askPermission();
        }
    }

    //method to request permissions from user for map usage
    private void askPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    //listening for permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Requires permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    //listening for request to start map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    //listening for location changes
    @Override
    public void onLocationChanged(@NonNull Location location) {
        MarkerOptions a = new MarkerOptions();
        a.position(loc);
        Marker m = mMap.addMarker(a);
        assert m != null;
        m.setPosition(loc);
    }

    //Lifecycle callbacks
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume MapsActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause MapsActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop MapsActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart MapsActivity");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy MapsActivity");
        super.onDestroy();
    }
}