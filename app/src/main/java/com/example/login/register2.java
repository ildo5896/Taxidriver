package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class register2 extends AppCompatActivity {
    TextView IDtext,PWtext;
    EditText NUMBERedit,IDedit,PWedit;
    Button SEARCHbutton,REGISTERbutton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    void init(){
        IDtext = findViewById(R.id.IDtext);
        PWtext = findViewById(R.id.PWtext);
        NUMBERedit = findViewById(R.id.NUMBERedit);
        IDedit = findViewById(R.id.IDedit);
        PWedit = findViewById(R.id.PWedit);
        SEARCHbutton = findViewById(R.id.SEARCHbutton);
        REGISTERbutton = findViewById(R.id.REGISTERbutton);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    void click(){
        SEARCHbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NUMBERedit.getText().toString().equals("")){
                    Query NUMBERquery = mDatabase.child("taxi-driver").orderByChild("number").equalTo(NUMBERedit.getText().toString());
                    NUMBERquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildren().iterator().hasNext()){
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Data_Taxi_Driver dataTaxiDriver = snapshot.getValue(Data_Taxi_Driver.class);
                                    if(dataTaxiDriver.getAUTH()){
                                        String MSG = "차량번호 : " + dataTaxiDriver.getNUMBER() + "\n승인되었습니다.";
                                        DIALOG("승인",MSG);
                                        builder.show();
                                    }else{
                                        String MSG = "차량번호 : " + dataTaxiDriver.getNUMBER() + "\n승인을 기다리고 있습니다.";
                                        DIALOG("확인",MSG);
                                        builder.show();
                                    }
                                }
                            }else{
                                String MSG = "가입하신 차량번호가 조회되지 않습니다.\n차량번호를 다시 확인해주세요.";
                                DIALOG("확인",MSG);
                                builder.show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "조회할 차량번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        REGISTERbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBMIT();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        init();
        click();
    }
    void DIALOG(String title,String MSG){
        builder = new AlertDialog.Builder(this);
        if(title.equals("승인")){
            builder.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IDtext.setVisibility(View.VISIBLE);
                            PWtext.setVisibility(View.VISIBLE);
                            IDedit.setVisibility(View.VISIBLE);
                            PWedit.setVisibility(View.VISIBLE);
                            REGISTERbutton.setVisibility(View.VISIBLE);
                        }
                    });
        }else if(title.equals("확인")){
            builder.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
        }else if(title.equals("완료")){
            builder.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent LOGINintent = new Intent(getApplicationContext(),Login.class);
                            startActivity(LOGINintent);
                            finish();
                        }
                    });
        }
    }
    void SUBMIT(){
        if(TextUtils.isEmpty(IDedit.getText().toString())){
            IDedit.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(PWedit.getText().toString())){
            PWedit.setError("Required");
            return;
        }

        createUSER(IDedit.getText().toString(),PWedit.getText().toString());
    }
    private void createUSER(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"가입 실패!",Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(NUMBERedit.getText().toString(),new Data_Taxi_Driver(IDedit.getText().toString()
                                    ,PWedit.getText().toString()
                                    ,NUMBERedit.getText().toString()
                                    ,"0"
                                    ,true));
                            mDatabase.child("taxi-driver").updateChildren(map);
                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();  //이메일 회원가입
                            DIALOG("완료","회원가입이 완료되었습니다.\n로그인 페이지로 이동하시겠습니까?");
                            builder.show();
                        }
                    }
                });
    }
}
