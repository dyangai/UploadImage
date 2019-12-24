package com.uploadimage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.uploadimage.Model.ModelProduk
import com.uploadimage.Model.ModelUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener{
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()

            if(email.isEmpty()){
                et_email.error = "Email tidak boleh kosong"
                et_email.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                et_email.error = "Input Alamat email yang valid"
                et_email.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()|| password.length < 6){
                et_password.error = "Password minimal 6 karakter"
                et_password.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)

        }


        text_view_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))

        }
        register.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }
    }

    private fun loginUser(email: String, password: String) {
        progressbar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE

                val uid = mAuth.currentUser!!.uid
                val roleRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                if (task.isSuccessful) {

                    roleRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.child("email").value!!.toString() == "admin@gmail.com") {
                                dashAdmin()

                            }else{
                                dashClient()
                            }

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                    //dashClient()
                } else {
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun dashClient(){
        val intent = Intent(this, DashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun dashAdmin(){
        val intent = Intent(this, DashAdmin::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

  /* override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            dashClient()
        }
    }*/

}



