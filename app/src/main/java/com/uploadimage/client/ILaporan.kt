package com.uploadimage.client

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.uploadimage.admin.DataProduk
import com.uploadimage.Model.ModelLaporan
import com.uploadimage.Model.ModelProduk
import com.uploadimage.R.layout.*
import kotlinx.android.synthetic.main.activity_ilaporan.*
import java.text.SimpleDateFormat
import java.util.*

class ILaporan : AppCompatActivity() {

   var cal = Calendar.getInstance()
   lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_ilaporan)

        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Input Laporan"
        actionbar?.elevation = 0f

        //setbackbutton
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val author = currentUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("Laporan").child(author).push()

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

            bt_save.setOnClickListener{
                val date = date_show.text.toString().trim()
                val namap = produkname.text.toString().trim()
                val merk = merk.text.toString().trim()
                val harga = harga.text.toString().trim()
                val quantity = qty.text.toString().trim()
                val total = total.text.toString().trim()


                if(date.equals("Tanggal")){
                    Toast.makeText(applicationContext, "Pilih tanggal yang benar", Toast.LENGTH_SHORT).show()
                    date_show.requestFocus()
                    return@setOnClickListener
                }

                if(namap.equals("")){
                    Toast.makeText(applicationContext, "Pilih produk terlebih dahulu", Toast.LENGTH_SHORT).show()
                    produkname.requestFocus()
                    return@setOnClickListener
                }


                if(quantity.isEmpty() || (quantity == "0")){
                    qty.error="Quantity tidak boleh kosong"
                    qty.requestFocus()
                    return@setOnClickListener
                }

                save(date,namap,merk,harga,quantity,total)

                //stok berkurang
            }

            val model = intent.getParcelableExtra<ModelProduk>(DataProduk.NAMA_PRODUK)
            if(model != null) {
                produkname.setText(model.nama_produk)
                merk.setText(model.merk)
                harga.setText(model.harga)
            }
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

    private fun save(date: String, namap: String,merk: String,harga: String,quantity: String,total: String){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val author = currentUser!!.uid
        val nm = currentUser.email.toString()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.message            }

            override fun onDataChange(data: DataSnapshot) {
                //val username = data.child("name").value!!.toString().trim()
                val key= ref.key.toString()
                val map = ModelLaporan(key,author,nm,date,namap,merk,harga,quantity,total,"")
                ref.setValue(map).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@ILaporan, DashActivity::class.java))
                    }
                }

            }

        })

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
