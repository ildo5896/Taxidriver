package com.example.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login2 : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        click()
    }

    override fun onStart() {
        super.onStart()
    }

    fun click(){
        LOGINbutton.setOnClickListener{
            if(IDedit.text.toString().equals("") || PASSWORDedit.text.toString().equals("")){
                Toast.makeText(applicationContext,"ID와 PW를 입력해주세요.",Toast.LENGTH_SHORT).show()
            }else{
                firebasemAuth(IDedit.text.toString()+"@taxi.com",PASSWORDedit.text.toString())
            }
        }
        Login_RegisterButton.setOnClickListener{
            val registerIntent = Intent(this,Register::class.java)
            startActivity(registerIntent)
        }
        SEARCHtext.setOnClickListener{
            val searchIntent = Intent(this,Register_2::class.java)
            startActivity(searchIntent)
        }
        LOSTtext.setOnClickListener{

        }

    }

    fun firebasemAuth(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var LoginIntent = Intent(this,main_kt::class.java)
                        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
                        val editor = positionDATA.edit()
                        editor.putString("ID",IDedit.text.toString())
                        editor.apply()
                        startActivity(LoginIntent)
                        finish()
                    }
                    else
                        Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                }
    }
}
