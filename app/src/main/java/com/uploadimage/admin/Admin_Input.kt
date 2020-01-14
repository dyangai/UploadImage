package com.uploadimage.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.uploadimage.client.DashActivity
import com.uploadimage.Model.ModelProduk
import com.uploadimage.R
import kotlinx.android.synthetic.main.admin_input.*
import java.util.*

@Suppress("DEPRECATION")
class Admin_Input : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    lateinit var mAuth : FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_input)

        //val key= ref.key.toString()
        //ref = FirebaseDatabase.getInstance().getReference("Data Produk").child(key)

        /*val mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid
        ref = FirebaseDatabase.getInstance().getReference("Data Produk").child(uid!!)
        ref.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            p0.message            }

        override fun onDataChange(data: DataSnapshot) {
            val image = data.child("image").value!!.toString()
            if(image.isNotEmpty()) {
                Picasso.get().load(image).into(promage)
            }
            else{
                promage.setImageDrawable(resources.getDrawable(R.drawable.profile))
            }


        }
        })*/

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

       val model = intent.getParcelableExtra<ModelProduk>(DataProduk.NAMA_PRODUK)
        if(model != null) {
            proname.setText(model.nama_produk)
            merk.setText(model.merk)
            stok.setText(model.stok)
            harga.setText(model.harga)
            if (model.image.isNotEmpty()) {
                Picasso.get().load(model.image).into(promage)
            } else {
                promage.setImageDrawable(resources.getDrawable(R.drawable.profile))
            }

            actionbar?.title = "Update Data Produk"
            bt_oke.text = "Update"
        }

        bt_oke.setOnClickListener{
            val nama = proname.text.toString().trim()
            val merk = merk.text.toString().trim()
            val stok = stok.text.toString().trim()
            val harga = harga.text.toString().trim()


            if(selectedPhotoUri == null) return@setOnClickListener
            val filename = UUID.randomUUID().toString()
            val storage = FirebaseStorage.getInstance().getReference("/productimage/$filename")
            storage.putFile(selectedPhotoUri!!).addOnCompleteListener(){
                storage.downloadUrl.addOnSuccessListener {
                    val ini = (it.toString())

                    if(bt_oke.text.equals("Update")) {
                        updateData(nama, merk, stok, harga)
                    }
                    else{
                        // uploadImage()
                        save(nama, merk, stok, harga, ini)

                    }

                }
            }
        }

    }

   /* private fun uploadImage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/productimage/$filename")
        storage.putFile(selectedPhotoUri!!).addOnCompleteListener(){
            storage.downloadUrl.addOnSuccessListener {
                val ini = (it.toString())

                val update_image = HashMap<String,Any>()
                update_image["image"] = ini

                ref.updateChildren(update_image).addOnCompleteListener(OnCompleteListener {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }*/

    var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            val selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            promage.setImageBitmap(bitmap)
        }
    }

    private fun hitung(){

    }

    private fun save(nama: String,merk: String,stok: String,harga: String, ini: String){
        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid
        ref = FirebaseDatabase.getInstance().getReference("Data Produk").push()
        val key= ref.key.toString()

            val sint = stok.toIntOrNull()
            val hint = harga.toIntOrNull()
            val total = hint?.let { sint?.times(it) }//sint?.times(hint!!)

        val produk = ModelProduk(key,uid!!,nama,merk,stok,ini,harga, total.toString())
        ref.setValue(produk).addOnCompleteListener{task ->
            if(task.isSuccessful) {
                val intent = Intent(this, DashActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
    }

  private fun updateData(nama: String,merk: String,stok: String,harga: String){
        progressbar.visibility = View.VISIBLE
       // val key = ref.key.toString()

        val uMap = HashMap<String,Any>()
        uMap["nama_produk"] = nama
        uMap["merk"] = merk
        uMap["stok"] = stok
        uMap["harga"] = harga

        ref.updateChildren(uMap).addOnCompleteListener{task ->
            if(task.isSuccessful){
                startActivity(Intent(this, DashActivity::class.java))
                finish()
            }
        }

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
