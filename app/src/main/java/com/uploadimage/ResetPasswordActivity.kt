package com.uploadimage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        close.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        bt_reset.setOnClickListener {
            val email = text_email.text.toString().trim()

            if (email.isEmpty()) {
                text_email.error = "Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                text_email.error = "Valid Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE

            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Check your email", Toast.LENGTH_SHORT).show()
                    } else {
                        (task.exception?.message!!)
                    }
                }
        }

    }
}
