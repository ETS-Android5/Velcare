package com.prembros.symptomator;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class details extends AppCompatActivity {

    EditText no, model, owner,phone;
    Button search;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        no = findViewById(R.id.editText1);
        model = findViewById(R.id.editText2);
        owner = findViewById(R.id.editText3);
        phone = findViewById(R.id.editText4);










    }

    public void search(View view){

        databaseReference= FirebaseDatabase.getInstance().getReference("data").child(no.getText().toString());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );



                model.setText(map.get("model"));
                owner.setText(map.get("owner name"));
                phone.setText(map.get("insurance"));





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
