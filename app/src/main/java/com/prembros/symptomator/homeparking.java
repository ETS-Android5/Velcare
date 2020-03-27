package com.prembros.symptomator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class homeparking extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    ArrayList<String> latitudes = new ArrayList<>();
    ArrayList<String> longitudes = new ArrayList<>();




    FusedLocationProviderClient client;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeparking);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.prembros.symptomator", Context.MODE_PRIVATE);



        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();


        try {

            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {
            if (places.size() == latitudes.size() && places.size() == longitudes.size()) {
                for (int i=0; i < latitudes.size(); i++) {
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else {
            //  places.add("Add a new place...");
            //locations.add(new LatLng(0,0));
        }


        ListView listView = findViewById(R.id.listView);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), parkingmaps.class);
                intent.putExtra("placeNumber",i);

                startActivity(intent);
            }
        });




        client = LocationServices.getFusedLocationProviderClient(this);
        Set<String> s = new LinkedHashSet<String>();
        //button = findViewById(R.id.button);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(homeparking.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        String address = "";

                        try {

                            List<Address> listAdddresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);

                            if (listAdddresses != null && listAdddresses.size() > 0) {
                                if (listAdddresses.get(0).getThoroughfare() != null) {
                                    if (listAdddresses.get(0).getSubThoroughfare() != null) {
                                        address += listAdddresses.get(0).getSubThoroughfare() + " ";
                                    }
                                    address += listAdddresses.get(0).getThoroughfare();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (address.equals("")) {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                            address += sdf.format(new Date());
                        }


                        homeparking.places.add(address);
                        homeparking.locations.add(userLocation);

                        homeparking.arrayAdapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getSharedPreferences("com.prembros.symptomator", Context.MODE_PRIVATE);

                        try {

                            ArrayList<String> latitudes = new ArrayList<>();
                            ArrayList<String> longitudes = new ArrayList<>();

                            for (LatLng coord : homeparking.locations) {
                                latitudes.add(Double.toString(coord.latitude));
                                longitudes.add(Double.toString(coord.longitude));
                            }

                            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(homeparking.places)).apply();
                            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();
                            sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(homeparking.this, "Location Saved!", Toast.LENGTH_SHORT).show();
                    }

                });

            }

        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                places.remove(i);
                latitudes.remove(i);
                longitudes.remove(i);

                sharedPreferences.edit().remove("places").apply();
                sharedPreferences.edit().remove("lats").apply();
                sharedPreferences.edit().remove("lons").apply();

                try {
                    sharedPreferences.edit().putString("places", ObjectSerializer.serialize(homeparking.places)).apply();
                    sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();
                    sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                homeparking.arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

        */


    }

    public void save(View view){

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnSuccessListener(homeparking.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {


                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                String address = "";

                try {

                    List<Address> listAdddresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);

                    if (listAdddresses != null && listAdddresses.size() > 0) {
                        if (listAdddresses.get(0).getThoroughfare() != null) {
                            if (listAdddresses.get(0).getSubThoroughfare() != null) {
                                address += listAdddresses.get(0).getSubThoroughfare() + " ";
                            }
                            address += listAdddresses.get(0).getThoroughfare();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (address.equals("")) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                    address += sdf.format(new Date());
                }


                homeparking.places.add(address);
                homeparking.locations.add(userLocation);

                homeparking.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getSharedPreferences("com.prembros.symptomator", Context.MODE_PRIVATE);

                try {

                    ArrayList<String> latitudes = new ArrayList<>();
                    ArrayList<String> longitudes = new ArrayList<>();

                    for (LatLng coord : homeparking.locations) {
                        latitudes.add(Double.toString(coord.latitude));
                        longitudes.add(Double.toString(coord.longitude));
                    }

                    sharedPreferences.edit().putString("places", ObjectSerializer.serialize(homeparking.places)).apply();
                    sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();
                    sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply();


                } catch (Exception e) {
                    e.printStackTrace();
                }


                Toast.makeText(homeparking.this, "Location Saved!", Toast.LENGTH_SHORT).show();
            }

        });

    }







}


