package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public static Login LOGINactivity;
    private FirebaseAuth mAuth;
    EditText EMAILedit,PASSWORDedit;
    Button LOGINbutton,REGISTERbutton;
    String ID,PW;
    TextView SEARCHtext;
    void init(){
        SharedPreferences DATA = getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = DATA.edit();

        mAuth = FirebaseAuth.getInstance();
        EMAILedit = findViewById(R.id.IDedit);
        PASSWORDedit = findViewById(R.id.PASSWORDedit);
        SEARCHtext = findViewById(R.id.SEARCHtext);
        LOGINbutton = findViewById(R.id.LOGINbutton);
        REGISTERbutton = findViewById(R.id.REGISTERbutton);
        ID = DATA.getString("ID","");
        PW = DATA.getString("PW","");
    }
    void click(){
        LOGINbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID.equals("") || PW.equals(""))
                    Toast.makeText(getApplicationContext(),"ID와 PW를 입력해주세요.",Toast.LENGTH_SHORT).show();
                ID = EMAILedit.getText().toString();
                PW = PASSWORDedit.getText().toString();
                firebaseAUTH(ID,PW);
            }
        });
        REGISTERbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent REGISTERintent = new Intent(getApplicationContext(),Register.class);
                startActivity(REGISTERintent);
            }
        });
        SEARCHtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SEARCHintent = new Intent(getApplicationContext(),register2.class);
                startActivity(SEARCHintent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        click();
        if(!ID.equals("") && !PW.equals("")){
            firebaseAUTH(ID,PW);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    void firebaseAUTH(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인 성공
                            Intent LOGINintent = new Intent(getApplicationContext(),main.class);
                            startActivity(LOGINintent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
