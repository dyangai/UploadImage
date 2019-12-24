package com.uploadimage

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_input_lp_.*
import java.text.SimpleDateFormat
import java.util.*
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import com.uploadimage.Model.ModelProduk
import java.security.AccessController.getContext



class InputLP_Activity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var db: DatabaseReference
    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_lp_)
        date_show.inputType = InputType.TYPE_NULL

        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Input Laporan"
        actionbar?.elevation = 0f

        //setbackbutton
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "EEEE, dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat)
                //val date = sdf.format(cal.time)
                date_show.setText(sdf.format(cal.time))

            }
        }

        // Show Datepicker when clicked
         date.setOnClickListener(object : View.OnClickListener {
             override fun onClick(view: View) {
                 DatePickerDialog(this@InputLP_Activity,
                     dateSetListener,
                     // set DatePickerDialog to point to today's date when it loads up
                     cal.get(Calendar.YEAR),
                     cal.get(Calendar.MONTH),
                     cal.get(Calendar.DAY_OF_MONTH)).show()
             }
         })

         produkname.setOnClickListener{
             startActivity(Intent(this, DataProduk::class.java))

         }



         val model = intent.getParcelableExtra<ModelProduk>(DataProduk.NAMA_PRODUK)
         produkname.setText(model?.nama_produk)
         merk.setText(model?.merk)
         harga.setText(model?.harga)

         //ontypelistener

         qty.addTextChangedListener(object:TextWatcher{
             override fun afterTextChanged(p0: Editable?) {
                 hitung()
             }

             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

         })


         bt_save.setOnClickListener{
               //  val date = date_show.text.toString().trim()
                // val namapro = proname.text.toString().trim()
                // val merk = merk.text.toString().trim()

               //  val totall = total.text.toString().trim()

            // var totally = qty*harga
             //total.text = totally.toString()

             saveToDB()
             setResult(Activity.RESULT_OK)
             finish()
         }



     }

     private fun saveToDB(){

     }

     private fun hitung(){
         if(qty != null && harga != null && produkname != null ) {
             val a = qty.text.toString().trim()
             val b = harga.text.toString().trim()
             val c= a.toIntOrNull()
             val d = b.toIntOrNull()
             val totally = d?.let { c?.times(it) }
             total.text = "$totally"

         }else{
             Toast.makeText(applicationContext, "Silahkan isi", Toast.LENGTH_SHORT).show()
         }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

