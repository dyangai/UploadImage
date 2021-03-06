package com.uploadimage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.uploadimage.admin.DashAdmin
import com.uploadimage.client.DashActivity

class SplashActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private val splashTime = 3000L
    private lateinit var myHandler : Handler

    private var session = userSession()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth = FirebaseAuth.getInstance()
        myHandler = Handler()
        myHandler.postDelayed({
            mAuth = FirebaseAuth.getInstance()
            //val user = mAuth.currentUser


        if(session.getStatus(this).equals("admin")) {
            val intent = Intent(this, DashAdmin::class.java)
            startActivity(intent)
            finish()
        }

        else if(session.getStatus(this).equals("client"))
        {
            toDash()
        }
        else{
            toLogin()
        }
           /* if (user != null){
                toDash()
            }
            else {
                toLogin()
            }*/
        },splashTime)

    }


    private fun toDash(){
        val intent = Intent(this, DashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
