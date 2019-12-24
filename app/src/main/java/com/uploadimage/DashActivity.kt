@file:Suppress("DEPRECATION")

package com.uploadimage

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_register.*

class DashActivity : AppCompatActivity() {

    lateinit var db: DatabaseReference
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)
        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Dashboard"
        supportActionBar!!.elevation = 0f

        mAuth = FirebaseAuth.getInstance()


        val uid = mAuth.currentUser?.uid

        //header

            db = FirebaseDatabase.getInstance().getReference("Users").child(uid!!)
            db.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("DashActivity", "error")
                }

                override fun onDataChange(data: DataSnapshot) {
                    val name = data.child("name").value!!.toString().trim()
                    val outlet = data.child("outlet").value!!.toString().trim()
                    val image = data.child("image").value!!.toString()
                    if (image.isNotEmpty()) {
                        Picasso.get().load(image).into(profile)
                    } else {
                        profile.setImageDrawable(resources.getDrawable(R.drawable.profile))
                    }


                    p_nama.setText(name)
                    p_outlet.setText(outlet)

                }

            })

            c_input.setOnClickListener(clickListener)
            c_lpjual.setOnClickListener(clickListener)
            c_edit.setOnClickListener(clickListener)
            c_riwayat.setOnClickListener(clickListener)

            profile.setOnClickListener {
                val intent = Intent(this, ProfileinActivity::class.java)
                startActivity(intent)
            }

    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.c_input -> input()
            R.id.c_lpjual -> lpjual()
            R.id.c_edit -> edit()
            R.id.c_riwayat -> riwayat()
        }
    }

    private fun input() {
        val intent = Intent(this, ILaporan::class.java)
        startActivity(intent)
    }

    private fun lpjual() {
        val intent = Intent(this, LPJual_Activity::class.java)
        startActivity(intent)
    }

    private fun edit() {
        val intent = Intent(this, Admin_Input::class.java)
        startActivity(intent)
    }

    private fun riwayat() {
        val intent = Intent(this, DataProduk::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_logout) {

            AlertDialog.Builder(this).apply {
                setTitle("Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    logout()
                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.create().show()

        }
        else{
            val intent = Intent(this, DashAdmin::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun logout(){
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}


