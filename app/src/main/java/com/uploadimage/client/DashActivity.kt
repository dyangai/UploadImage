@file:Suppress("DEPRECATION")

package com.uploadimage.client

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.uploadimage.*
import com.uploadimage.admin.Admin_Input
import com.uploadimage.admin.DashAdmin
import com.uploadimage.admin.DataProduk
import com.uploadimage.Model.ModelLaporan
import com.uploadimage.Model.ModelProduk
import com.uploadimage.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemLongClickListener
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.laporan_row.view.*
import kotlinx.android.synthetic.main.produk_row.view.*
import java.util.HashMap

class DashActivity : AppCompatActivity() {

    lateinit var db: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var q: DatabaseReference

    companion object{
        val LAPORAN = "LAPORAN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Dashboard Client"
        supportActionBar!!.elevation = 0f
        //rc_dataproduk.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //header
        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid
        db = FirebaseDatabase.getInstance().getReference("Users").child(uid!!)
            db.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(data: DatabaseError) {
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


      /*  filter.setOnClickListener {
9
                val navDrawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!navDrawer.isDrawerOpen(Gravity.RIGHT)) {
                    navDrawer.openDrawer(Gravity.RIGHT)
                }
                else {
                    navDrawer.closeDrawer(Gravity.LEFT)
                }
        }*/



        c_input.setOnClickListener(clickListener)
       // c_dataproduk.setOnClickListener(clickListener)
        //c_dataproduk.setOnClickListener(clickListener)

        profile.setOnClickListener {
            val intent = Intent(this, ProfileinActivity::class.java)
            startActivity(intent)
        }

        fetchLaporan()
       // hitungTarget()
       // fetchDataproduk()
    }

    private fun fetchLaporan(){
        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("Laporan").child(uid!!)
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    p0.children.forEach {
                        val lap = it.getValue(ModelLaporan::class.java)
                        if (lap != null) {
                            adapter.add(mLaporan(lap))
                        }
                    }


                    adapter.setOnItemLongClickListener { item, view ->

                        AlertDialog.Builder(this@DashActivity).apply {
                            val options = arrayOf("Update", "Delete")
                            setItems(options, DialogInterface.OnClickListener() { _, which ->
                                if (which == 0) {
                                    val lpItem = item as mLaporan
                                    val intent = Intent(view.context, ILaporan::class.java)
                                    intent.putExtra(LAPORAN, lpItem.ml)
                                    startActivityForResult(intent, 0)
                                    finish()
                                }
                                if (which == 1) {
                                    val lpItem = item as mLaporan
                                    val query = FirebaseDatabase.getInstance().getReference()
                                        .child("Laporan").child(uid)
                                    query.child(lpItem.ml.id_laporan).removeValue()
                                    Toast.makeText(
                                        this@DashActivity,
                                        "done",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    rc_lp.adapter!!.notifyDataSetChanged()
                                }

                            }).create().show()
                        }
                        return@setOnItemLongClickListener true
                    }
                    rc_lp.adapter = adapter
                    //adapter.notifyDataSetChanged()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

   /* private fun fetchDataproduk(){
        db = FirebaseDatabase.getInstance().getReference("Data Produk")
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {


                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    val model = it.getValue(ModelProduk::class.java)
                    if(model != null ) {
                       // adapter.add(DataItem(model))
                    }
                }
                // if((uid.equals(admin))) {

                //rc_dataproduk.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }*/

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.c_input -> input()
            //R.id.c_dataproduk -> dp()
            //R.id.c_edit -> edit()
            //R.id.c_riwayat -> riwayat()
        }
    }

    private fun input() {
        val intent = Intent(this, ILaporan::class.java)
        startActivity(intent)
    }

    private fun dp() {
        val intent = Intent(this, Admin_Input::class.java)
        startActivity(intent)
    }

    private fun filter() {
       // val intent = Intent(this, MainActivity::class.java)
       // startActivity(intent)
    }

    private fun hitungTarget(){
        //tambahkan semua value dari child (harga) x child (stok)
       // mAuth = FirebaseAuth.getInstance()
        //val uid = mAuth.currentUser?.uid
       q = FirebaseDatabase.getInstance().getReference().child("Data Produk")
       val q1 = FirebaseDatabase.getInstance().getReference("Data Produk").child(q.key!!)
        //val q1 = FirebaseDatabase.getInstance().getReference().child("Data Produk")
            //.child("Data Produk")
            //.orderByChild("id_produk")
            //.equalTo(q.key)
            q1.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p2: DatabaseError) {

                }

                override fun onDataChange(p2: DataSnapshot) {

                    //for(snapshot in p2.children){
                        //var sum = 0
                        val t = p2.child("totalstok_harga").value!!.toString()
                        val nm = p2.child("merk").value!!.toString()
                        Log.d("DashActivity","isinya"+t)
                        Log.d("DashActivity","PERCOBAAN"+nm)
                       // val tint = t.toInt()
                       // sum += tint
                       // val map = HashMap<String,Any>()
                        //val t = map.get("totalstok_harga").toString()
                        //val tint = t.toInt()
                        //sum += tint

                        //t_target.setText(t)
                    //}
                }

            })


       /* q1.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p1: DataSnapshot) {
                p1.children.forEach{
                    var sumTarget = 0

                    //val keyr = q1.key.toString()
                   // val target = p1.child(keyr).child("totalstok_harga").value!!.toString().trim()
                    val target = p1.child("totalstok_harga").value!!.toString().trim()
                     val targetint = target.toInt()
                     //if (targetint != null) {
                         sumTarget += targetint
                         //t_target.text = sumTarget.toString()
                      t_target.setText(sumTarget.toString())
                     //}
                   // else{
                        // Toast.makeText(applicationContext, "Silahkan isi", Toast.LENGTH_SHORT).show()
                    // }


                   /* val p2 = p1.child(keyr)
                    p2.children.forEach{
                        val target = p2.child("totalstok_harga").value!!.toString().trim()
                        val targetint = target.toIntOrNull()
                        if (targetint != null) {
                            sumTarget += targetint
                            Log.d("DashActivity", "$sumTarget")
                           // t_target.text = sumTarget.toString()
                        }
                        else{
                            Log.d("DashActivity", "GAGAL")
                        }
                    }*/
                }

            }
            override fun onCancelled(p1: DatabaseError) {

            }
        })*/
    }

    private fun hitungTerjual(){
       //tambahkan semua total harga yang dilaporkan

    }

    private fun sisa(){
        //jumlah target dikurangi terjual
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



class mLaporan(val ml: ModelLaporan): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (ml.image_lap.isEmpty()) {
            viewHolder.itemView.date_laporan.text = ml.date
            viewHolder.itemView.proname.text = ml.p_name
            viewHolder.itemView.merk.text = ml.merk
            viewHolder.itemView.harga.text = ml.harga
            viewHolder.itemView.qty.text = ml.quantity
            viewHolder.itemView.total.text = ml.total
            //viewHolder.itemView.pengeluaran.text = ml.image_lap
        } else {
            viewHolder.itemView.date_laporan.text = ml.date
            viewHolder.itemView.proname.text = ml.p_name
            viewHolder.itemView.merk.text = ml.merk
            viewHolder.itemView.harga.text = ml.harga
            viewHolder.itemView.qty.text = ml.quantity
            viewHolder.itemView.total.text = ml.total
            //viewHolder.itemView.pengeluaran.text = ml.image_lap
            Picasso.get().load(ml.image_lap).into(viewHolder.itemView.image_produk)
        }
      /*  viewHolder.itemView.setOnClickListener { view ->

        }*/
    }
    override fun getLayout(): Int {
        return R.layout.laporan_row
    }


}
/*class DataItem(val model: ModelProduk): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(model.image.isEmpty()){
            viewHolder.itemView.proname_show.setText(model.nama_produk)
            viewHolder.itemView.merk_show.setText(model.merk)
            viewHolder.itemView.stok_produk_show.setText(model.stok)
            viewHolder.itemView.harga_produk_show.setText(model.harga)
        }else {
            viewHolder.itemView.proname_show.setText(model.nama_produk)
            viewHolder.itemView.merk_show.setText(model.merk)
            viewHolder.itemView.stok_produk_show.setText(model.stok)
            viewHolder.itemView.harga_produk_show.setText(model.harga)
           Picasso.get().load(model.image).into(viewHolder.itemView.image_produk_show)
        }
    }

    override fun getLayout(): Int {
        return R.layout.produk_row
    }

}*/



