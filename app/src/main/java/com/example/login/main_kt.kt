package com.example.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*


class main_kt : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    var ID: String = ""
    var PW: String = ""
    var Taxi_PhoneNumber: String = ""
    var Call: Boolean = false

    var call_Index: String? = null
    var call_phonenumber: String? = null
    var call_person: String? = null
    val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mStorageRef: StorageReference = FirebaseStorage.getInstance().reference
    var ImageRef: StorageReference = mStorageRef
    var map: GoogleMap? = null
    var fusedLocation: FusedLocationProviderClient? = null
    var mLocationPermissionGranted: Boolean = false
    var googleApiClient: GoogleApiClient? = null
    internal var location: Task<Location>? = null
    internal var gpsLatLng: LatLng? = null
    var DISTANCE = 0.009
    var mapDialog: Dialog? = null
    var testDialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testDialog = AlertDialog.Builder(this)

        init()
        AD()
        click()
        CALL()
    }

    fun init() {
        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        val editor = positionDATA.edit()
        ID = positionDATA.getString("ID", "")
        if (ID.equals("")) {
            Toast.makeText(this, "로그인 문제가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            val LoginIntent = Intent(this, main_kt::class.java)
            startActivity(LoginIntent)
            finish()
        }

        val InfoQuery: Query = mDatabase.child("taxi-driver").orderByChild("id").equalTo(ID)
        InfoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot: DataSnapshot in p0.children) {
                    val data_taxi_driver = snapshot.getValue(Data_Taxi_Driver::class.java)
                    NameText.text = data_taxi_driver!!.name
                    NUMBERText.text = data_taxi_driver!!.number
//                    PHONENUMBERtext.text = data_taxi_driver!!.phonenumber
                    Taxi_PhoneNumber = data_taxi_driver!!.phonenumber
                    POINTtext.text = data_taxi_driver!!.point.toString()

                    editor.putString("이름", NameText.text.toString())
                    editor.putString("택시번호", NUMBERText.text.toString())
                    editor.putString("포인트", POINTtext.text.toString())
                    editor.apply()
//                    ImageRef = mStorageRef.child("taxi_driver_image/123456789.jpg")
//                    Log.e("Taxi", "taxi_driver_image/" + NUMBERText.text.toString() + ".jpg")
//                    Glide.with(applicationContext).load(ImageRef).into(DRIVERimage)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        check()
    }

    fun check() {
        mDatabase.child("taxi-call").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot: DataSnapshot in p0.children) {
                    val data_call = snapshot.getValue(Data_call::class.java)

                }

            }
        })
    }

    fun click() {
        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        val editor = positionDATA.edit()

        CALLbutton.setOnClickListener {
            if (CALLbutton.text.toString().equals("실시간 콜 받기 OFF")) {
                callON()
            } else {
                callOFF()
            }
        }
        DAYbutton.setOnClickListener {

        }

        MONTHbutton.setOnClickListener {

        }
        mapButton.setOnClickListener {
            DIALOG(LatLng(calllatlng.text.split(",")[0].toDouble(), calllatlng.text.split(",")[1].toDouble()),
                    LatLng(arrivelatlng.text.split(",")[0].toDouble(), arrivelatlng.text.split(",")[1].toDouble()))
        }
        noButton.setOnClickListener {

        }

        yesButton.setOnClickListener {
            if (Battery(this) <= 10) {
                Toast.makeText(applicationContext, "배터리가 10%이하에선 제한됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                testDialog = AlertDialog.Builder(this)
                testDialog!!.setTitle("콜 수락 확인")
                testDialog!!.setMessage("콜을 수락하시겠습니까?\n(콜 수락 후, 취소가 되지 않습니다.)")
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                            val DriveIntent = Intent(this, Drive::class.java)
                            editor.putString("현재위치", gpsLatLng!!.latitude.toString() + "," + gpsLatLng!!.longitude.toString())
                            editor.putString("출발지", startText.text.toString())
                            editor.putString("출발좌표", calllatlng.text.toString())
                            editor.putString("도착지", arriveText.text.toString())
                            editor.putString("도착좌표", arrivelatlng.text.toString())
                            editor.putString("콜_인덱스", call_Index)
                            editor.putString("콜_전화번호", call_phonenumber)
                            editor.putString("콜_인원수", call_person)
                            editor.apply()

                            var map = HashMap<String, Any?>()
                            map.put("driver", NameText.text.toString())
                            map.put("taxinumber", NUMBERText.text.toString())
                            map.put("taxiphonenumber", Taxi_PhoneNumber)
                            map.put("complete_driver", false)
                            map.put("complete_client", false)
                            map.put("pay_complete", 1)
                            mDatabase.child("taxi-call").child(call_Index!!).updateChildren(map)

                            startActivity(DriveIntent)
                            finish()
                        })
                        .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                            onDestroy()
                        })
                testDialog!!.show()
            }
        }
    }

    fun AD() {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        val adRequest = AdRequest.Builder().build()
        navigationAdView.loadAd(adRequest)
    }

    fun callON() {
        var map = HashMap<String, Any?>()
        CALLbutton.setBackgroundColor(Color.parseColor("#D02B1E"))

        NameText.visibility = View.INVISIBLE
        NUMBERText.visibility = View.INVISIBLE
        기사님.visibility = View.INVISIBLE
        textView13.visibility = View.INVISIBLE
        DAYbutton.visibility = View.INVISIBLE
        MONTHbutton.visibility = View.INVISIBLE
        POINTtext.visibility = View.INVISIBLE
        textView12.visibility = View.INVISIBLE

        CallText.visibility = View.VISIBLE

        Call = true

        CALLbutton.text = "실시간 콜 받기 ON"
        map.put("call", true)
        mDatabase.child("taxi-driver").child(NUMBERText.text.toString()).updateChildren(map)
    }

    fun callOFF() {
        var map = HashMap<String, Any?>()
        CALLbutton.text = "실시간 콜 받기 OFF"
        NameText.visibility = View.VISIBLE
        NUMBERText.visibility = View.VISIBLE
        기사님.visibility = View.VISIBLE
        textView13.visibility = View.VISIBLE
        DAYbutton.visibility = View.VISIBLE
        MONTHbutton.visibility = View.VISIBLE
        POINTtext.visibility = View.VISIBLE
        textView12.visibility = View.VISIBLE

        CallText.visibility = View.INVISIBLE
        noButton.visibility = View.INVISIBLE
        yesButton.visibility = View.INVISIBLE
        arriveText.visibility = View.INVISIBLE
        priceText.visibility = View.INVISIBLE
        distanceText.visibility = View.INVISIBLE
        startText.visibility = View.INVISIBLE
        mapButton.visibility = View.INVISIBLE

        CALLbutton.setBackgroundColor(Color.parseColor("#F6E14B"))
        map.put("call", false)
        mDatabase.child("taxi-driver").child(NUMBERText.text.toString()).updateChildren(map)
    }

    fun CALL() {
        var count = 0
        mDatabase.child("taxi-call").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot: DataSnapshot in p0.children) {

                    var data_call = snapshot.getValue(Data_call::class.java)
                    if (data_call!!.taxinumber!!.equals(NUMBERText.text.toString())) {
                        testDialog!!.setTitle("확인").setMessage("진행 중인 콜이 있습니다.\n(진행중인 콜로 이동합니다.)").show()
                        try {
                            Thread.sleep(2000)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val intent = Intent(applicationContext, Drive::class.java)
                        startActivity(intent)
                        finish()
                    }
                    val Call_Start_latlng = LatLng(data_call!!.start_Latitude!!.toDouble(), data_call!!.start_Longitude!!.toDouble())
                    val Call_Arrive_latlng = LatLng(data_call!!.arrive_Latitude!!.toDouble(), data_call!!.arrive_Longitude!!.toDouble())
//                    Log.e("data", gpsLatLng!!.latitude.toString())
//                    Log.e("data", Call_Start_latlng.latitude.toString())

                    if (Math.abs(gpsLatLng!!.latitude - Call_Start_latlng.latitude) <= DISTANCE
                            && Math.abs(gpsLatLng!!.longitude - Call_Start_latlng.longitude) <= DISTANCE
                            && data_call!!.driver.equals("")
                            && Call) {
                        CallText.text = "콜이 들어왔습니다!"
                        call_Index = data_call.index
                        noButton.visibility = View.VISIBLE
                        yesButton.visibility = View.VISIBLE
                        arriveText.visibility = View.VISIBLE
                        arriveText.text = data_call.arrive
                        priceText.visibility = View.VISIBLE
                        var person = data_call.person
                        var pay = data_call.pay
                        priceText.text = "요금 : " + (person!! * pay!!).toString() + " P"
                        distanceText.visibility = View.VISIBLE
                        distanceText.text = "거리 : " + data_call.distance
                        startText.visibility = View.VISIBLE
                        startText.text = data_call.start
                        mapButton.visibility = View.VISIBLE

                        call_phonenumber = data_call.phonenumber
                        call_person = data_call.person.toString()
                        calllatlng.text = Call_Start_latlng.latitude.toString() + "," + Call_Start_latlng.longitude.toString()
                        arrivelatlng.text = Call_Arrive_latlng.latitude.toString() + "," + Call_Arrive_latlng.longitude.toString()
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    fun DIALOG(Call_Start_latlng: LatLng, Call_Arrive_latlng: LatLng) {
        val positionDATA = getSharedPreferences("positionDATA", MODE_PRIVATE)
        val editor = positionDATA.edit()

        mapDialog = Dialog(this)
        mapDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mapDialog!!.setContentView(R.layout.activity_map_dialog)
        mapDialog!!.show()

        var mMapView: MapView = mapDialog!!.findViewById(R.id.map_dialog)

        var center_latitude: Double? = null
        var center_longitude: Double? = null
        if (Call_Start_latlng.latitude > Call_Arrive_latlng.latitude) {
            center_latitude = Call_Start_latlng.latitude - ((Call_Start_latlng.latitude - Call_Arrive_latlng.latitude) / 2)
        } else {
            center_latitude = Call_Start_latlng.latitude + ((Call_Arrive_latlng.latitude - Call_Start_latlng.latitude) / 2)
        }
        if (Call_Start_latlng.longitude > Call_Arrive_latlng.longitude) {
            center_longitude = Call_Start_latlng.longitude - ((Call_Start_latlng.longitude - Call_Arrive_latlng.longitude) / 2)
        } else {
            center_longitude = Call_Start_latlng.longitude + ((Call_Arrive_latlng.longitude - Call_Start_latlng.longitude) / 2)
        }
        val location = LatLng(center_latitude, center_longitude)
        /*Log.e("latlng",center_latitude.toString())
        Log.e("latlng",center_longitude.toString())
        Log.e("latlng",Call_Start_latlng.latitude.toString())
        Log.e("latlng",Call_Start_latlng.longitude.toString())
        Log.e("latlng",Call_Arrive_latlng.latitude.toString())
        Log.e("latlng",Call_Arrive_latlng.longitude.toString())*/

        val position = CameraPosition.Builder().target(location).zoom(15f).build()

        MapsInitializer.initialize(this)
        mMapView.onCreate(mapDialog!!.onSaveInstanceState())
        mMapView.onResume()// needed to get the map to display immediately
        mMapView.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap?) {
                p0!!.addMarker(MarkerOptions().position(Call_Start_latlng)
                        .title("콜 위치")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                p0!!.addMarker(MarkerOptions().position(Call_Arrive_latlng)
                        .title("도착 위치")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))//change marker color to red
                p0!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
                p0!!.uiSettings.isZoomControlsEnabled = true
            }
        })
        var yesButton_dialog: Button = mapDialog!!.findViewById(R.id.yesButton_dialog)
        yesButton_dialog.setOnClickListener {
            if (Battery(this) <= 5) {
                Toast.makeText(applicationContext, "배터리가 10%이하에선 제한됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                testDialog = AlertDialog.Builder(this)
                testDialog!!.setTitle("콜 수락 확인")
                testDialog!!.setMessage("콜을 수락하시겠습니까?\n(콜 수락 후, 취소가 되지 않습니다.)")
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                            val DriveIntent = Intent(this, Drive::class.java)
                            editor.putString("현재위치", gpsLatLng!!.latitude.toString() + "," + gpsLatLng!!.longitude.toString())
                            editor.putString("출발지", startText.text.toString())
                            editor.putString("출발좌표", calllatlng.text.toString())
                            editor.putString("도착지", arriveText.text.toString())
                            editor.putString("도착좌표", arrivelatlng.text.toString())
                            editor.putString("콜_인덱스", call_Index)
                            editor.putString("콜_전화번호", call_phonenumber)
                            editor.putString("콜_인원수", call_person)
                            editor.apply()

                            var map = HashMap<String, Any?>()
                            map.put("driver", NameText.text.toString())
                            map.put("taxinumber", NUMBERText.text.toString())
                            map.put("taxiphonenumber", Taxi_PhoneNumber)
                            map.put("complete_driver", false)
                            map.put("complete_client", false)
                            map.put("pay_complete", 0)
                            mDatabase.child("taxi-call").child(call_Index!!).updateChildren(map)

                            startActivity(DriveIntent)
                            finish()
                        })
                        .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                            onDestroy()
                        })
                testDialog!!.show()
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = fusedLocation!!.getLastLocation()
            val locationTask = fusedLocation!!.getLastLocation()
            locationTask.addOnCompleteListener(this, OnCompleteListener<Location> {
                val MyLocation = LatLng(location!!.getResult()!!.latitude, location!!.getResult()!!.longitude)
                gpsLatLng = MyLocation
            })

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            gpsLatLng = LatLng(37.566643, 126.978279)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        googleApiClient!!.connect()
    }

    fun Battery(context: Context): Int {
        var intentBattery = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        var level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        var scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        var batteryPct = level / scale.toFloat()

        return (batteryPct * 100).toInt()
    }//배터리 잔량 확인


    @SuppressLint("MissingPermission")
    internal fun My_Location() {
        if (location != null) {
            val locationTask = fusedLocation!!.getLastLocation()
            locationTask.addOnCompleteListener(this, OnCompleteListener<Location> {
                val MyLocation = LatLng(location!!.getResult()!!.getLatitude(), location!!.getResult()!!.getLongitude())
                val position = CameraPosition.Builder().target(MyLocation).zoom(15f).build()
                map!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
                map!!.setMyLocationEnabled(mLocationPermissionGranted)
                gpsLatLng = MyLocation
                //DrawCirlce(1000)
            })

        } //else {
//            setmDefaultLocation()
//        }
    }

//        internal fun setmDefaultLocation() {
//            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.566643, 126.978279), 15f))
//        }
//
//        internal fun DrawCirlce(D: Int) {
//            if (circle != null) {
//                map!!.clear()
//                circle = CircleOptions().center(gpsLatLng) //원점
//                        .radius(D.toDouble())      //반지름 단위 : m
//                        .strokeWidth(3f)  //선너비 0f : 선없음;
//                map!!.addCircle(circle)
//            } else {
//                circle = CircleOptions().center(gpsLatLng) //원점
//                        .radius(D.toDouble())      //반지름 단위 : m
//                        .strokeWidth(3f)  //선너비 0f : 선없음;
//                map!!.addCircle(circle)
//            }
//
//        }
}