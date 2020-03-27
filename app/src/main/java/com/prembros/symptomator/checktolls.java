package com.prembros.symptomator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prembros.symptomator.paytoll;

import java.util.ArrayList;

public class checktolls extends AppCompatActivity {


    static ArrayList<String> places = new ArrayList<String>();

    static ArrayAdapter arrayAdapter;

    DatabaseReference databaseReference;


    static int deletposition;
    static String x;

    static String number;
    EditText editText;

    static String value;

    ListView listView;


    FusedLocationProviderClient client;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checktolls);

        places.clear();



        //button = findViewById(R.id.button);
        editText=findViewById(R.id.editText);






        listView = findViewById(R.id.listView);
        // places.add("Add a new place...");
        //locations.add(new LatLng(0,0));

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,places){
        };

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                x= places.get(i);
                deletposition=i;




                databaseReference= FirebaseDatabase.getInstance().getReference("tolls").child(number).child(x);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        value=dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(getApplicationContext(), paytoll.class);
                startActivity(intent);
            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
/*
                x=places.get(position);
                databaseReference=FirebaseDatabase.getInstance().getReference("tolls").child(number).child(x);
                databaseReference.removeValue();


                places.remove(position);
                arrayAdapter.notifyDataSetChanged();



*/




                return false;
            }
        });




    }

    public void search(View view){

        number=editText.getText().toString();

        places.clear();
        arrayAdapter.notifyDataSetChanged();


        if(number.equals("")){
            Toast.makeText(this,"Enter Number",Toast.LENGTH_SHORT).show();
        }

        else {


            databaseReference = FirebaseDatabase.getInstance().getReference("tolls").child(number);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String x = dataSnapshot.getKey();
                    places.add(x);
                    arrayAdapter.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }






}



