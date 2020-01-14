package com.uploadimage.admin

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.uploadimage.client.ILaporan
import com.uploadimage.Model.ModelProduk
import com.uploadimage.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemLongClickListener
import kotlinx.android.synthetic.main.data_produk.*
import kotlinx.android.synthetic.main.produk_row.view.*

class DataProduk : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var db: DatabaseReference

    companion object{
        val NAMA_PRODUK = "NAMA_PRODUK"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_produk)

        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Data Produk"
        actionbar?.elevation = 0f

        //setbackbutton
        actionbar?.setDisplayHomeAsUpEnabled(true)
        rc_dataproduk.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        db = FirebaseDatabase.getInstance().getReference().child("Data Produk")

        fetchUsers()
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val username = search.text.toString().trim()
                searchUser(username)
            }

        })
    }

    private fun fetchUsers(){
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    val model = it.getValue(ModelProduk::class.java)
                    if(model != null ) {
                        adapter.add(DataItem(model))
                    }

                }
               // if((uid.equals(admin))) {
                   /* adapter.setOnItemLongClickListener(object : OnItemLongClickListener {
                        override fun onItemLongClick(item: Item<*>, view: View): Boolean {

                            AlertDialog.Builder(this@DataProduk).apply {
                                val options = arrayOf("Update", "Delete")
                                setItems(options, DialogInterface.OnClickListener() { _, which ->
                                    if (which == 0) {
                                        val produkItem = item as DataItem
                                        val intent = Intent(view.context, Admin_Input::class.java)
                                        intent.putExtra(NAMA_PRODUK, produkItem.model)
                                        startActivityForResult(intent, 0)
                                        finish()
                                    }
                                    if (which == 1) {
                                        val produkItem = item as DataItem
                                        val key = db.key
                                        val query = FirebaseDatabase.getInstance().getReference().child("Data Produk")
                                        if(query.orderByChild("id_produk").equals(key)) {
                                            query.child(produkItem.model.id_produk).removeValue()
                                        }
                                        else{
                                            query.child(produkItem.model.id_produk).removeValue()
                                            Toast.makeText(this@DataProduk,"Done", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }).create().show()

                            }
                            return true
                        }
                    })
               // }*/

                adapter.setOnItemClickListener { item, view ->
                    val produkItem = item as DataItem
                    val intent = Intent(view.context, ILaporan::class.java)
                    intent.putExtra(NAMA_PRODUK, produkItem.model)
                    startActivityForResult(intent, 0)
                    finish()
                }

                rc_dataproduk.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            }
    }

    private fun searchUser(username : String){
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Data Produk")
            .orderByChild("nama_produk")
            .startAt(username)
            .endAt(username + "\uf8ff")

        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()


                ds.children.forEach {

                    val model = it.getValue(ModelProduk::class.java)

                    if (model != null) {
                         adapter.add(DataItem(model))

                    }

                }


                adapter.setOnItemClickListener { item, view ->
                    val produkItem = item as DataItem
                    val intent = Intent(view.context, ILaporan::class.java)
                    intent.putExtra(NAMA_PRODUK, produkItem.model)
                    startActivityForResult(intent, 0)
                    finish()
                }

                rc_dataproduk.adapter = adapter
            }

            override fun onCancelled(ds: DatabaseError) {

            }
        })
    }
}


class DataItem(val model: ModelProduk): Item<GroupieViewHolder>() {
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

}
