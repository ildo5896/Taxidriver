package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class main extends AppCompatActivity {
    ImageView PROFILEimage,DRIVERimage;
    TextView NameText,PHONENUMBERtext,POINTtext;
    Button CALLbutton,DAYbutton,MONTHbutton;
    String ID,PW;
    String TAXINUMBER,PHONENUMBER,PAY;
    DatabaseReference mDatabase;
    void init(){
        SharedPreferences DATA = getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = DATA.edit();

//        DRIVERimage = findViewById(R.id.DRIVERimage);
        NameText = findViewById(R.id.NameText);
        PHONENUMBERtext = findViewById(R.id.NUMBERText);
        POINTtext = findViewById(R.id.POINTtext);
        CALLbutton = findViewById(R.id.CALLbutton);
        DAYbutton = findViewById(R.id.DAYbutton);
        MONTHbutton = findViewById(R.id.MONTHbutton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        ID = intent.getExtras().getString("ID");

        Query INFOquery = mDatabase.child("taxi-driver").orderByChild("id").equalTo(ID);
        INFOquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Data_Taxi_Driver data_taxi_driver = snapshot.getValue(Data_Taxi_Driver.class);
                    NameText.setText(data_taxi_driver.getNUMBER());
                    PHONENUMBERtext.setText(data_taxi_driver.getPHONENUMBER());
                    POINTtext.setText(data_taxi_driver.getPOINT());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    void click(){
        CALLbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                if(CALLbutton.getText().toString().equals("OFF")){
                    CALLbutton.setText("ON");
                    map.put("call",true);
                    mDatabase.child("taxi-driver").child(NameText.getText().toString()).updateChildren(map);
                }else{
                    CALLbutton.setText("OFF");
                    map.put("call",false);
                    mDatabase.child("taxi-driver").child(NameText.getText().toString()).updateChildren(map);
                }//TODO : 횟수 제한 두기(DB 과부하 방지차원)
            }
        });
        DAYbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MONTHbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        click();

    }
}
