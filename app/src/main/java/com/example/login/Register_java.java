package com.example.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Register_java extends AppCompatActivity {
    Button GAINbutton,CORPORATIONbutton,GALLERYbutton,CAMERAbutton,REGISTERbutton;
    EditText NUMBERedit;
    ImageView IMAGEview;
    DatabaseReference mDatabase;
    StorageReference mStorage;
    AlertDialog.Builder builder;
    Bitmap img;
    String KIND="";
    void init(){
        GAINbutton = findViewById(R.id.GAINbutton);
        CORPORATIONbutton = findViewById(R.id.CORPORATIONbutton);
        GALLERYbutton = findViewById(R.id.GALLERYbutton);
        CAMERAbutton = findViewById(R.id.CAMERAbutton);
        REGISTERbutton = findViewById(R.id.Register2Button);
        NUMBERedit = findViewById(R.id.NUMBERedit);
        IMAGEview = findViewById(R.id.IMAGEview);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }
    void click(){
        GAINbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GAINbutton.setTextColor(Color.parseColor("#FF6600"));
                CORPORATIONbutton.setTextColor(Color.parseColor("#000000"));
                KIND = GAINbutton.getText().toString();
            }
        });
        CORPORATIONbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GAINbutton.setTextColor(Color.parseColor("#000000"));
                CORPORATIONbutton.setTextColor(Color.parseColor("#FF6600"));
                KIND = CORPORATIONbutton.getText().toString();
            }
        });
        GALLERYbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IMAGEintent = new Intent();
                IMAGEintent.setType("image/*");
                IMAGEintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(IMAGEintent, 1);
            }
        });
        CAMERAbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CAMERAintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CAMERAintent,2);
            }
        });
        REGISTERbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KIND.equals("") || NUMBERedit.getText().toString().equals("") || img == null) {
                    Toast.makeText(getApplicationContext(),"정보를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    DIALOG();
                    builder.show();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        init();
        click();
    }

    void DIALOG(){
        HashMap<String,Object> map = new HashMap<>();
        map.put(NUMBERedit.getText().toString(),new Data_Taxi_Driver("","","",NUMBERedit.getText().toString(),0,"",false,false));
        mDatabase.child("taxi-driver").updateChildren(map);
        Upload_image();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("신청 완료")
                .setMessage("택시 기사 신청이 완료되었습니다.\n24시간이내에 가입 승인이 됩니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    void Upload_image(){
        StorageReference storageReference = mStorage.child("taxi_driver_image/" + NUMBERedit.getText().toString() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) { }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) { //갤러리 선택 시
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    IMAGEview.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == 2){ //카메라 선택 시
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    IMAGEview.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
