package com.prembros.symptomator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

public class phonecall extends AppCompatActivity {


    ListView listView;
    static ArrayList<String> name = new ArrayList<String>();
    static ArrayList<String> num = new ArrayList<String>();

    private static final int REQUEST_CAlL=1;



    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecall);

        name.clear();

        name.add("Audi");
        name.add("BMW");
        name.add("mercedes");
        name.add("Honda");
        name.add("Jaguar");
        name.add("Land Rover");
        name.add("Ford");
        name.add("Skoda");
        name.add("Toyota");
        name.add("Tata");
        name.add("Maruti Suzuki");
        name.add("Mahindra");









        num.add("011 4573 5512");
        num.add("1800 102 2269");
        num.add("1800 102 9222");
        num.add("124229091119");
        num.add("1800 121 6808");
        num.add("1800 258 6644");
        num.add("1800 419 2500");
        num.add("1800 123 090 909");
        num.add("1800 425 0001");
        num.add("022 6665 8282");
        num.add("1800 180 0180");
        num.add("1800 425 1624");










        listView=findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        listView.setAdapter(arrayAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              String dial="tel:"+num.get(position);
              startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

           }
       });



    }


}
