package com.prembros.symptomator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;


import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class textdetect extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FusedLocationProviderClient client;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    String price;
    String text;
    String num;
EditText editText;
EditText editText1;
    ImageView imageView;
    TextView textView;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textdetect);





        if(checkPermission(Manifest.permission.SEND_SMS)){
            //send.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        imageView = findViewById(R.id.imageView);
        editText=findViewById(R.id.editText2);
        editText1=findViewById(R.id.editText);


    }

    public void add_image(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        detect();
    }

    public void detect() {
        FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionTextDetector firebaseVisionTextRecognizer= FirebaseVision.getInstance().getVisionTextDetector();

        firebaseVisionTextRecognizer.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                process_text(firebaseVisionText);
            }
        });
    }

    private void process_text(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> textBlocks=firebaseVisionText.getBlocks();
        if(textBlocks.size()==0){
            Toast.makeText(this,"no text",Toast.LENGTH_SHORT).show();
        }
        else{
            for (FirebaseVisionText.Block textBlock:firebaseVisionText.getBlocks()){
                text=textBlock.getText();
                editText.setText(text);
            }
        }

    }

    public void upload(View view){


        databaseReference1= FirebaseDatabase.getInstance().getReference("data").child(editText.getText().toString()).child("phone");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 num=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        client = LocationServices.getFusedLocationProviderClient(this);

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
        client.getLastLocation().addOnSuccessListener(textdetect.this, new OnSuccessListener<Location>() {
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
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                    address += sdf.format(new Date());
                }





                String number=editText.getText().toString();
                 price=editText1.getText().toString();

                databaseReference= FirebaseDatabase.getInstance().getReference("tolls");
                databaseReference.child(number).child(address).setValue(price);




                Toast.makeText(textdetect.this, "Saved!", Toast.LENGTH_SHORT).show();
            }

        });

        String phoneNumber =num;
        String smsMessage = "Toll to pay "+ price ;
        if(phoneNumber == null || phoneNumber.length() == 0 ||
                smsMessage == null || smsMessage.length() == 0){
            return;
        }

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
