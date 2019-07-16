package com.example.login

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Register : AppCompatActivity() {
    val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mStorage: StorageReference = FirebaseStorage.getInstance().reference
    var Img = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888)
    var KIND: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        click()
    }

    override fun onStart() {
        super.onStart()
    }

    fun click() {
        GAINbutton.setOnClickListener {
            GAINbutton.setTextColor(Color.parseColor("#FF6600"))
            CORPORATIONbutton.setTextColor(Color.parseColor("#000000"))
            KIND = GAINbutton.text.toString()
        }
        CORPORATIONbutton.setOnClickListener {
            GAINbutton.setTextColor(Color.parseColor("#000000"))
            CORPORATIONbutton.setTextColor(Color.parseColor("#FF6600"))
            KIND = CORPORATIONbutton.text.toString()
        }
        GALLERYbutton.setOnClickListener {
            var GalleryIntent = Intent()
            GalleryIntent.setType("image/*")
            GalleryIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(GalleryIntent,1)
        }
        CAMERAbutton.setOnClickListener {
            var CameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var Camera_permission : Array<String> = arrayOf(android.Manifest.permission.CAMERA)
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,Camera_permission,2)
            }else{
                startActivityForResult(CameraIntent, 2);
            }
        }
        Register2Button.setOnClickListener {
            if (KIND.equals("") || NUMBERedit.text.toString().equals("") || Img == null) {
                Toast.makeText(applicationContext, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val Dialog = AlertDialog.Builder(this)
                var map = HashMap<String, Any?>()
                map.put(NUMBERedit.text.toString(), Data_Taxi_Driver("", "",""
                        , NUMBERedit.text.toString(), 0, "", false, false))
                mDatabase.child("taxi-driver").updateChildren(map)
                Upload_image()
                Dialog.setTitle("신청 완료")
                        .setMessage("택시 기사 신청이 완료되었습니다.\n24시간이내에 가입 승인이 완료됩니다.")
                        .setPositiveButton("확인") { dialogInterface, i ->
                            var LoginIntent = Intent(applicationContext, Login2::class.java)
                            startActivity(LoginIntent)
                            finish()
                        }
                Dialog.show()
            }
        }

    }


    fun Upload_image() {
        val storageReference = mStorage.child("taxi_driver_image/" + NUMBERedit.text.toString() + ".jpg")
        val baos = ByteArrayOutputStream()
        Img.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageReference.putBytes(data)
        uploadTask.addOnFailureListener { }.addOnSuccessListener { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                try{
                    val Input : InputStream = contentResolver.openInputStream(data!!.data)
                    Img = BitmapFactory.decodeStream(Input)
                    Input.close()
                    IMAGEview.setImageBitmap(Img)
                }catch (e : Exception){}
            }
        }else if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
               try{
                   val bundle : Bundle = data!!.extras
                   val bitmap = bundle.get("data") as Bitmap
                   IMAGEview.setImageBitmap(bitmap)
                }catch (e : Exception){}
            }
        }
    }
}