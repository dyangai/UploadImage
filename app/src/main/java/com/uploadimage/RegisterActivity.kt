package com.uploadimage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Patterns
import android.view.View
import com.uploadimage.Model.ModelUser


class RegisterActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var db : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        done.setOnClickListener{
           // val name = reg_user.text.toString().trim()
            val email = reg_email.text.toString().trim()
            val password = reg_password.text.toString().trim()
          //  val outlet = reg_outlet.text.toString().trim()

            if(email.isEmpty()){
                reg_email.error = "Email tidak boleh kosong"
                reg_password.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                reg_email.error = "Input Alamat email yang valid"
                reg_password.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()|| password.length < 6){
                reg_password.error = "Password minimal 6 karakter"
                reg_password.requestFocus()
                return@setOnClickListener
            }

            createUser(email, password)

        }

        Login.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun createUser(email : String , password : String ){
        reg_progressbar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                reg_progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val id = currentUser!!.uid

                            val user = ModelUser(id,currentUser.email!!, reg_user.text.toString().trim(),"",reg_outlet.text.toString().trim())
                            //User(id,currentUser.email!!, reg_user.text.toString().trim(),"",reg_outlet.text.toString().trim())
                            /*val map = HashMap<String,String>()
                            map["name"] = name
                            map["outlet"] = outlet
                            map["id"] = id
                            map["email"] = currentUser.email!!
                            map["image"] = ""*/


                           db = FirebaseDatabase.getInstance().getReference("Users").child(id)
                            db.setValue(user).addOnCompleteListener(OnCompleteListener {
                                if (task.isSuccessful) {
                                   //login()
                                   val USER_NAME = "username"
                                   val USER_ID = "userid"

                                    val bundle = Bundle()
                                   bundle.putString(USER_ID, id)
                                   bundle.putString(USER_NAME, reg_user.text.toString().trim() )

                                   val intent = Intent(this, LoginActivity::class.java)
                                   startActivity(intent)

                                }
                            })
                    } else {
                        Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    }
            }
    }

    private fun login(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    override fun onBackPressed() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            login()
        }
    }

}


//class User (val id:String,val email:String, val name:String, val image:String,val outlet:String){
    //  constructor(): this("","","","","")
//}

