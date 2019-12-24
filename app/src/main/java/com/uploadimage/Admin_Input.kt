package com.uploadimage

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.uploadimage.Model.ModelProduk
import kotlinx.android.synthetic.main.admin_input.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Admin_Input : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_input)

        ref = FirebaseDatabase.getInstance().getReference("Data Produk")

        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Input Data Produk"
        actionbar?.elevation = 0f

        //setbackbutton
        actionbar?.setDisplayHomeAsUpEnabled(true)

        promage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        bt_oke.setOnClickListener{
            val nama = proname.text.toString().trim()
            val merk = merk.text.toString().trim()
            val stok = stok.text.toString().trim()
            val harga = harga.text.toString().trim()

            savedata(nama,merk,stok,harga)
        }
    }

  /*  var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

        }

        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/productimage/$filename")
        ref.putFile(selectedPhotoUri!!).addOnCompleteListener(){
            ref.downloadUrl.addOnSuccessListener {
                val ini = (it.toString())


            }
        }

    }*/


    private fun savedata(nama: String,merk: String,stok: String,harga: String){
        //val currentUser = FirebaseAuth.getInstance().currentUser
        //val author = currentUser!!.uid
        val key= ref.push().key.toString()
        val map = ModelProduk(key,nama,merk,stok,"",harga)

        ref.child(key).setValue(map).addOnCompleteListener(OnCompleteListener {task ->
            if (task.isSuccessful) {
                    startActivity(Intent(this, DashAdmin::class.java))
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this@Admin_Input, DashAdmin::class.java))
        finish()
    }


}
