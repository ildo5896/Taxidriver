package com.example.login

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_drive.*

class Drive : AppCompatActivity(), OnMapReadyCallback {
    var Call_Map: GoogleMap? = null

    var Gps: String? = null
    var Start: String? = null
    var Arrive: String? = null
    var Start_latlng_String: String? = null
    var Arrive_latlng_String: String? = null
    var call_phone: String? = null
    var call_person: String? = null
    var INDEX: String? = null

    var Gps_Latlng: LatLng? = null
    var Start_Latlng: LatLng? = null
    var Arrive_Latlng: LatLng? = null

    var dialog: AlertDialog.Builder? = null
    var dialog2: AlertDialog.Builder? = null

    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    var Complete: Boolean = false

    var once : Int = 0
    override fun onMapReady(p0: GoogleMap?) {
        Call_Map = p0

        val call_position = CameraPosition.Builder().target(Start_Latlng).zoom(15f).build()
        Call_Map!!.moveCamera(CameraUpdateFactory.newCameraPosition(call_position))
        Call_Map!!.addMarker(MarkerOptions().position(Start_Latlng!!)
                .title("콜 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)


        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        Gps = positionDATA.getString("현재위치", "")
        Start = positionDATA.getString("출발지", "")
        Arrive = positionDATA.getString("도착지", "")
        Start_latlng_String = positionDATA.getString("출발좌표", "")
        Arrive_latlng_String = positionDATA.getString("도착좌표", "")
        call_phone = positionDATA.getString("콜_전화번호", "")
        call_person = positionDATA.getString("콜_인원수", "")

        callText.text = Start
        Start_Latlng = LatLng(Start_latlng_String!!.split(",")[0].toDouble(),
                Start_latlng_String!!.split(",")[1].toDouble())
        Arrive_Latlng = LatLng(Arrive_latlng_String!!.split(",")[0].toDouble(),
                Arrive_latlng_String!!.split(",")[1].toDouble())
        Gps_Latlng = LatLng(Gps!!.split(",")[0].toDouble(),
                Gps!!.split(",")[1].toDouble())

        personText.text = call_person + " 명"

        click()
        map()
        database()
    }

    fun database() {
        Log.e("INDEX", INDEX)
        mDatabase.child("taxi-call").child(INDEX!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                var data_call = p0.getValue(Data_call::class.java)
                if (data_call!!.complete_driver!! && data_call!!.complete_client!! && once == 0) {
                    var PayIntent = Intent(this@Drive, Order_Pay::class.java)
                    startActivity(PayIntent)
                    once++
                    finish()
                }
            }
        })

    }

    fun click() {
        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        INDEX = positionDATA.getString("콜_인덱스", "")

        call_Button.setOnClickListener {
            titleText.text = "콜 위치"
            titleText.setBackgroundColor(Color.parseColor("#C3633F"))

            val call_position = CameraPosition.Builder().target(Start_Latlng).zoom(15f).build()
            Call_Map!!.moveCamera(CameraUpdateFactory.newCameraPosition(call_position))
            Call_Map!!.clear()
            Call_Map!!.addMarker(MarkerOptions().position(Start_Latlng!!)
                    .title("콜 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

            callText.text = Start
            callText.setBackgroundColor(Color.parseColor("#C3633F"))

            navigationButton.text = "콜 위치까지\n카카오 네비로 안내시작"
            navigationButton.setBackgroundColor(Color.parseColor("#C3633F"))
        }

        arrive_Button.setOnClickListener {
            titleText.text = "도착 위치"
            titleText.setBackgroundColor(Color.parseColor("#2A4D5D"))

            val arrive_position = CameraPosition.Builder().target(Arrive_Latlng).zoom(15f).build()
            Call_Map!!.moveCamera(CameraUpdateFactory.newCameraPosition(arrive_position))
            Call_Map!!.clear()
            Call_Map!!.addMarker(MarkerOptions().position(Arrive_Latlng!!)
                    .title("도착 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

            callText.text = Arrive
            callText.setBackgroundColor(Color.parseColor("#2A4D5D"))

            navigationButton.text = "도착 위치까지\n카카오 네비로 안내시작"
            navigationButton.setBackgroundColor(Color.parseColor("#2A4D5D"))
        }

        navigationButton.setOnClickListener {
            if (navigationButton.text.equals("콜 위치까지\n카카오 네비로 안내시작")) {
                var url = "daummaps://route?sp="
                url += Gps
                url += "&ep=" + Start_latlng_String + "&by=CAR"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                var url = "daummaps://route?sp="
                url += Start_latlng_String
                url += "&ep=" + Arrive_latlng_String + "&by=CAR"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        phoneText.setOnClickListener {
            dialog = AlertDialog.Builder(this)
            dialog!!.setTitle("전화하기")
            dialog!!.setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->
                val PhoneCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call_phone))
                startActivity(PhoneCall)
            }).setNegativeButton("아니요", DialogInterface.OnClickListener { dialogInterface, i ->
                onDestroy()
            })
            dialog!!.show()
        }
        OrderButton.setOnClickListener {
            dialog = AlertDialog.Builder(this)
            dialog!!.setTitle("운행 종료").setMessage("운행을 종료하고,\n결제를 요청합니다.")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                        var map = HashMap<String, Any?>()
                        map.put("complete_driver",true)
                        mDatabase.child("taxi-call").child(INDEX!!).updateChildren(map)
                        dialog2 = AlertDialog.Builder(this)
                        dialog2!!.setTitle("운행 확인").setMessage("운행 종료를 기다리고 있습니다.\n승객이 운행종료를 눌러야 진행됩니다.\n\n(승객에게 운행종료를 요청하세요)")
                                .setCancelable(false).setNegativeButton("종료", DialogInterface.OnClickListener { dialogInterface, i ->
                                }).show()

                    }).setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i ->
                        onDestroy()
                    }).show()
        }
    }

    fun map() {
        val mapFragment_call = fragmentManager.findFragmentById(R.id.call_map) as MapFragment
        mapFragment_call.getMapAsync(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                Toast.makeText(applicationContext, "운행 중엔 기능이 제한됩니다.", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
