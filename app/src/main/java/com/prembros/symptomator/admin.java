package com.prembros.symptomator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;


public class admin extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth= FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    public void out(View view){
        mAuth.signOut();
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(admin.this, signhome.class);
        startActivity(intent);
    }

    public void text(View view){

        Intent intent = new Intent(admin.this, textdetect.class);
        startActivity(intent);
    }

    public void detils(View view){

        Intent intent = new Intent(admin.this, details.class);
        startActivity(intent);
    }

}
