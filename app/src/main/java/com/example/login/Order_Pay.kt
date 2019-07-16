package com.example.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order__pay.*

class Order_Pay : AppCompatActivity() {
    val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var person: Int? = null
    var INDEX: String? = null
    var map = HashMap<String, Any?>()
    var dialog : AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order__pay)

        dialog = AlertDialog.Builder(this)
        Database()
        click()

        ResultText.text = ServiceText.text.toString()

        OrderEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!OrderEdit.text.toString().equals("")) {
                    if (OrderEdit.text.toString().toInt() % person!! != 0) {
                        if ((OrderEdit.text.toString().toInt() / person!!) % 10 == 3) { // 1000/3 을 한 나머지가 6일경우, 100원 차감
                            ResultText.text = (OrderEdit.text.toString().toInt() - 100).toString() +
                                    ServiceText.text.toString().split(" ")[0] + " 원"
                        } else if ((OrderEdit.text.toString().toInt() / person!!) % 10 == 3) { // 2000/3 을 한 나머지가 3일경우, 200원 차감
                            ResultText.text = (OrderEdit.text.toString().toInt() - 200).toString() +
                                    ServiceText.text.toString().split(" ")[0] + " 원"
                        }
                    } else {
                        ResultText.text = (OrderEdit.text.toString().toInt() + ServiceText.text.toString().split(" ")[0].toInt()).toString() + " 원"
                    }
                } else {
                    ResultText.text = ServiceText.text.toString().split(" ")[0] + " 원"
                }
            }
        })
    }

    fun Database() {
        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        INDEX = positionDATA.getString("콜_인덱스", "")
        var NUMBER = positionDATA.getString("택시번호","")

        mDatabase.child("taxi-call").child(INDEX!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var data_call = p0.getValue(Data_call::class.java)
                if (data_call!!.person!!.equals(2)) {
                    person = 2
                    ServiceText.text = (1000 * person!!).toString() + " 원"
                    map.put("service_each", 1000)
                } else if (data_call!!.person!!.equals(3)) {
                    person = 3
                    ServiceText.text = (700 * person!!).toString() + " 원"
                    map.put("service_each", 700)

                } else if (data_call!!.person!!.equals(4)) {
                    person = 4
                    ServiceText.text = (500 * person!!).toString() + " 원"
                    map.put("service_each", 500)
                }

                if(data_call!!.pay_complete!!.equals(0)){
                    Toast.makeText(applicationContext,"결제가 완료되었습니다.",Toast.LENGTH_SHORT).show()
                    val MainIntent = Intent(applicationContext,main_kt::class.java)
                    map.clear()
                    map.put("point",data_call!!.pay!!.toInt() + (data_call!!.service_each!!.toInt() * data_call!!.person!!.toInt()))
                    mDatabase.child("taxi-driver").child(NUMBER).updateChildren(map)
                    startActivity(MainIntent)
                    finish()
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

        mDatabase.child("taxi-call").child(INDEX!!).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data_call = p0.getValue(Data_call::class.java)
                if(data_call!!.pay_complete!!.equals(0)){
                    dialog!!.setTitle("결제가 정상적으로 완료되었습니다!").setPositiveButton("확인",DialogInterface.OnClickListener { dialogInterface, i ->
                    })
                    TitleText.text = "결제 완료"
                    OrderEdit.isEnabled = false
                    OrderButton.text = "첫 화면으로"
                }
            }
        })

    }

    fun click() {
        OrderButton.setOnClickListener {
            if (OrderButton.text.toString().equals("첫 화면으로")) {
                val intent = Intent(applicationContext,main_kt::class.java)
                startActivity(intent)
                finish()
            } else {
                if (OrderEdit.text.toString().equals("")) {
                    Toast.makeText(applicationContext, "미터기 요금을 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    map.put("pay", OrderEdit.text.toString().toInt())
                    map.put("pay_complete", OrderEdit.text.toString().toInt())
                    mDatabase.child("taxi-call").child(INDEX!!).updateChildren(map)
                    Toast.makeText(applicationContext, "요금을 요청했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                Toast.makeText(applicationContext, "결제 진행중입니다.", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
