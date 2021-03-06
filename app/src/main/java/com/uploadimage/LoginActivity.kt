package com.uploadimage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.uploadimage.admin.DashAdmin
import com.uploadimage.client.DashActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    private var session = userSession()

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

               // val uid = mAuth.currentUser!!.email.toString()

               // val roleRef = FirebaseDatabase.getInstance().getReference("Users")

                if (task.isSuccessful) {
                    if(email.equals("admin@gmail.com")){
                        dashAdmin()
                        session.setStatus(this@LoginActivity, "admin")
                    }else{
                        dashClient()
                        session.setStatus(this@LoginActivity, "client")
                    }

                    /*if(uid.equals("admin@gmail.com")) {
                        dashAdmin()
                    }else{
                        dashClient()
                    }
*/

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

  override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            dashClient()
        }
    }

}





