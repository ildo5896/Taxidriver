package com.example.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register2.*

class Register_2 : AppCompatActivity() {
    val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var Dialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        click()
    }

    fun click() {
        SEARCHbutton.setOnClickListener {
            if (!NUMBERedit.text.toString().equals("")) {
                val numberQuery = mDatabase.child("taxi-driver").orderByChild("number").equalTo(NUMBERedit.text.toString())
                numberQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.children.iterator().hasNext()) {
                            for (snapshot: DataSnapshot in dataSnapshot.children) {
                                val taxiDriver = snapshot.getValue(Data_Taxi_Driver::class.java)
                                if (taxiDriver!!.auth) {
                                    if (!taxiDriver!!.id.equals("")) {
                                        DIALOG("회원 확인", "이미 아이디가 존재합니다.\n아이디를 잊어버리셨나요?")
                                        Dialog!!.show()
                                        return
                                    }
                                    var MSG = "차량번호 : ${taxiDriver.number}\n승인되었습니다."
                                    DIALOG("승인", MSG)
                                    Dialog!!.show()
                                } else {
                                    var MSG = "차량번호 : ${taxiDriver.number}\n승인을 기다리고 있습니다."
                                    DIALOG("확인", MSG)
                                    Dialog!!.show()
                                }
                            }
                        } else {
                            var MSG = "가입하신 차량번호가 조회되지 않았습니다.\n차량번호를 다시 확인해주세요."
                            DIALOG("확인", MSG)
                            Dialog!!.show()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            } else Toast.makeText(applicationContext, "조회할 차량번호를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
        Register2Button.setOnClickListener {
            SUBMIT()
        }
    }

    fun DIALOG(title: String, MSG: String) {
        Dialog = AlertDialog.Builder(this)
        if (title.equals("승인")) {
            Dialog!!.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        textview1.visibility = View.INVISIBLE
                        NUMBERedit.visibility = View.INVISIBLE
                        SEARCHbutton.visibility = View.INVISIBLE

                        NameText.visibility = View.VISIBLE
                        NameEdit.visibility = View.VISIBLE
                        IDtext.visibility = View.VISIBLE
                        PWtext.visibility = View.VISIBLE
                        IDedit.visibility = View.VISIBLE
                        PWedit.visibility = View.VISIBLE
                        NUMBERText.visibility = View.VISIBLE
                        PHONENUMBERedit.visibility = View.VISIBLE
                        PHONENUMBERbutton.visibility = View.VISIBLE
                        AUTHedit.visibility = View.VISIBLE
                        Register2Button.visibility = View.VISIBLE
                    }
        } else if (title.equals("확인")) {
            Dialog!!.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("확인") { dialogInterface, i -> }
        } else if (title.equals("완료")) {
            Dialog!!.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("이동") { dialogInterface, i ->
                        var loginIntent = Intent(applicationContext, Login2::class.java)
                        startActivity(loginIntent)
                        finish()
                    }
        } else if (title.equals("회원 확인")) {
            Dialog!!.setTitle(title)
                    .setMessage(MSG)
                    .setPositiveButton("예") { dialogInterface, i ->
                        var lostIntent = Intent(applicationContext, findID::class.java)
                        startActivity(lostIntent)
                        finish()
                    }
                    .setNegativeButton("아니요") { dialogInterface, i ->
                        var loginIntent = Intent(applicationContext, Login2::class.java)
                        startActivity(loginIntent)
                        finish()
                    }
        }
    }

    fun SUBMIT() {
        if (TextUtils.isEmpty(NameEdit.text.toString())) {
            NameEdit.setError("Required")
            Toast.makeText(applicationContext,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(IDedit.text.toString())) {
            IDedit.setError("Required")
            Toast.makeText(applicationContext,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(PWedit.text.toString())) {
            PWedit.setError("Required")
            Toast.makeText(applicationContext,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(PHONENUMBERedit.text.toString())) {
            PHONENUMBERedit.setError("Required")
            Toast.makeText(applicationContext,"전화번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(AUTHedit.text.toString())){
            AUTHedit.setError("Required")
            Toast.makeText(applicationContext,"인증번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if(!AUTHedit.text.toString().equals("123456")){
            Toast.makeText(applicationContext,"인증번호를 맞게 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }

        createUSER(IDedit.text.toString()+"@taxi.com", PWedit.text.toString())
    }

    fun createUSER(email: String, password: String) {
        if (password.length < 6) {
            Toast.makeText(applicationContext, "6자리 이상의 비밀번호를 사용해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful()) {
                        Toast.makeText(applicationContext, "가입 실패!\n중복된 ID입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        var map = HashMap<String, Any?>()
                        map.put(NUMBERedit.text.toString(), Data_Taxi_Driver(IDedit.text.toString()
                                , PWedit.text.toString()
                                , NameEdit.text.toString()
                                , NUMBERedit.text.toString()
                                , 0
                                , PHONENUMBERedit.text.toString()
                                , true
                                , false))
                        mDatabase.child("taxi-driver").updateChildren(map)
                        Toast.makeText(applicationContext, "가입 성공!", Toast.LENGTH_SHORT).show()
                        DIALOG("완료", "회원가입이 완료되었습니다.\n로그인 페이지로 이동하시겠습니까?")
                        Dialog!!.show()
                    }
                }
    }
}