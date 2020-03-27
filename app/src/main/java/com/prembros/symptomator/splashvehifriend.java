package com.prembros.symptomator;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.android.gms.common.util.IOUtils;

//import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class splashvehifriend extends AppCompatActivity {

    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashvehifriend);

        gifImageView=findViewById(R.id.gifimageview);

        try{
            InputStream inputStream=getAssets().open("velcaresplash.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();


        }
        catch(IOException ex){

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashvehifriend.this.startActivity(new Intent(splashvehifriend.this,signhome.class));
                splashvehifriend.this.finish();


            }
        },2500);

    }
}
