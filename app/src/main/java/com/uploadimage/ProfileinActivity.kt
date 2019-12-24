@file:Suppress("DEPRECATION")

package com.uploadimage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_profilegal.*
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap


class ProfileinActivity : AppCompatActivity() {
    private lateinit var imageUri : Uri
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var mAuth: FirebaseAuth
    lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilegal)

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Edit Profile"
        //setbackbutton
        actionbar.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid
        db = FirebaseDatabase.getInstance().getReference("Users").child(uid!!)

        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.message            }

            override fun onDataChange(data: DataSnapshot) {
                val name = data.child("name").value!!.toString().trim()
                val outlet = data.child("outlet").value!!.toString().trim()
                val email = data.child("email").value!!.toString().trim()
                val image = data.child("image").value!!.toString()
                if(image.isNotEmpty()) {
                    Picasso.get().load(image).into(image_view)
                }
                else{
                    image_view.setImageDrawable(resources.getDrawable(R.drawable.profile))
                }
                t_name.setText(name)
                t_email.setText(email)
                t_outlet.setText(outlet)

            }

        })

        image_view.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        button_save.setOnClickListener{
            val name = t_name.text.toString().trim()
            val outlet = t_outlet.text.toString().trim()

            updateUser(name,outlet)
        }
    }

    private fun updateUser(name: String, outlet: String){
        progressbar.visibility = View.VISIBLE

        val uMap = HashMap<String,Any>()
        uMap["name"] = name
        uMap["outlet"] = outlet

        db.updateChildren(uMap).addOnCompleteListener{task ->
            if(task.isSuccessful){
                startActivity(Intent(this, DashActivity::class.java))
                finish()
            }
        }
    }

     var selectedPhotoUri: Uri? = null
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

         }

         if(selectedPhotoUri == null) return
         val filename = UUID.randomUUID().toString()
         val ref = FirebaseStorage.getInstance().getReference("/userimage/$filename")
         ref.putFile(selectedPhotoUri!!).addOnCompleteListener(){
             ref.downloadUrl.addOnSuccessListener {
                 val ini = (it.toString())

                 val update_image = HashMap<String,Any>()
                 update_image["image"] = ini

                 db.updateChildren(update_image).addOnCompleteListener(OnCompleteListener {task ->
                     if(task.isSuccessful){
                         Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
                     }
                 })
             }
         }

     }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
 }



