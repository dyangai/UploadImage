package com.uploadimage

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.uploadimage.Model.ModelProduk
import com.uploadimage.R.layout.*
import kotlinx.android.synthetic.main.activity_ilaporan.*
import java.text.SimpleDateFormat
import java.util.*

class ILaporan : AppCompatActivity() {

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_ilaporan)

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
                val myFormat = "EEEE, dd.MM.yyyy"
                val sdf = SimpleDateFormat(myFormat)
                date_show.setText(sdf.format(cal.time))

            }
        }

        // Show Datepicker when clicked
        date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@ILaporan,
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

        qty.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                hitung()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
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
