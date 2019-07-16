package com.example.login

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_permission.*

class permission : AppCompatActivity() {
    private var GPSgrant: Boolean = false
    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        GPSgrant = chkGpsService()
        if (!GPSgrant)
            chkGpsService()
        getPermission()

        StartButton.setOnClickListener(View.OnClickListener {
            GPSgrant = chkGpsService()
            if (!GPSgrant)
                chkGpsService()
            getPermission()
        })
    }
    fun chkGpsService(): Boolean {
        val gps = android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if (!(gps.matches(".*gps.*".toRegex()) && gps.matches(".*network.*".toRegex()))) {
            val gsDialog = AlertDialog.Builder(this)
            gsDialog.setTitle("위치 서비스 설정")
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?")
            gsDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                startActivity(intent)
            }).create().show()
            return false
        } else {
            return true
        }
    }
    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(applicationContext, main_kt::class.java)
            startActivity(intent)
            finish()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {

                }
            } else {

            }
        }
    }
}
