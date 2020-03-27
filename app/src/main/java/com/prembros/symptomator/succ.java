package com.prembros.symptomator;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class succ extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    private Button signOut;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.out,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item1){
            mAuth.signOut();
            mGoogleSignInClient.signOut();
            Toast.makeText(this," Sign Out Success",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(succ.this, signhome.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succ);
        //signOut=findViewById(R.id.out);

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

       String name=user.getDisplayName();
        Toast.makeText(this,"Welcome "+" "+name,Toast.LENGTH_SHORT).show();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void out(View view){
        mAuth.signOut();
        mGoogleSignInClient.signOut();

        Toast.makeText(this," Sign Out Success",Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(succ.this, signhome.class);
        startActivity(intent);
    }

    public void health(View view){
        Intent intent = new Intent(succ.this, MainActivity.class);
        startActivity(intent);
    }

    public void update(View view){
        Intent intent = new Intent(succ.this, ReportsAuthActivity.class);
        startActivity(intent);
    }

    public void park(View view){
        Intent intent = new Intent(succ.this, homeparking.class);
        startActivity(intent);
    }

    public void remind(View view){
        Intent intent = new Intent(succ.this, homealarm.class);
        startActivity(intent);
    }

    public void drive(View view){
        Intent intent = new Intent(succ.this, details.class);
        startActivity(intent);
    }

    public void tolls(View view){
        Intent intent = new Intent(succ.this, checktolls.class);
        startActivity(intent);
    }

    public void sil(View view){
        Intent intent = new Intent(succ.this, homesilent.class);
        startActivity(intent);
    }

    public void phone(View view){
        Intent intent = new Intent(succ.this, phonecall.class);
        startActivity(intent);
    }





}
