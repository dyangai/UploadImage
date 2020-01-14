@file:Suppress("UNREACHABLE_CODE")

package com.uploadimage


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.*
import com.uploadimage.Model.ModelProduk
import com.uploadimage.admin.Admin_Input
import com.uploadimage.admin.DataItem
import com.uploadimage.admin.DataProduk
import com.uploadimage.client.ILaporan
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemLongClickListener
import kotlinx.android.synthetic.main.data_produk.*
import kotlinx.android.synthetic.main.data_produk.rc_dataproduk
import kotlinx.android.synthetic.main.fragment_showdp.*

/**
 * A simple [Fragment] subclass.
 */
class ShowdpFragment : Fragment() {
    lateinit var db: DatabaseReference
    companion object{
        val NAMA_PRODUK = "NAMA_PRODUK"

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showdp, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rc_dataproduk.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        add.setOnClickListener {
            val intent = Intent(requireContext(), Admin_Input::class.java)
            startActivity(intent)
        }

        db = FirebaseDatabase.getInstance().getReference().child("Data Produk")

        fetchUsers()
        searchdp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

               val username = searchdp.text.toString().trim()
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

                adapter.setOnItemLongClickListener(object : OnItemLongClickListener {
                    override fun onItemLongClick(item: Item<*>, view: View): Boolean {

                        AlertDialog.Builder(requireContext()).apply {
                            val options = arrayOf("Update", "Delete")
                            setItems(options, DialogInterface.OnClickListener() { _, which ->
                                if (which == 0) {
                                    val produkItem = item as DataItem
                                    val intent = Intent(view.context, Admin_Input::class.java)
                                    intent.putExtra(NAMA_PRODUK, produkItem.model)
                                    startActivityForResult(intent, 0)
                                    //finish()
                                }
                                if (which == 1) {
                                    val produkItem = item as DataItem
                                    //val key = db.key
                                    val query3 = FirebaseDatabase.getInstance().getReference().child("Data Produk")
                                        query3.child(produkItem.model.id_produk).removeValue()
                                        Toast.makeText(requireContext(),"Done", Toast.LENGTH_SHORT).show()


                                }
                            }).create().show()

                        }
                        return true
                    }
                })

                rc_dataproduk.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

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
                //if (search.search.text.toString() == "") {
                //adapter.clear()

                ds.children.forEach {

                    val model = it.getValue(ModelProduk::class.java)

                    if (model != null) {
                        adapter.add(DataItem(model))

                    }

                }

                adapter.setOnItemLongClickListener(object : OnItemLongClickListener {
                    override fun onItemLongClick(item: Item<*>, view: View): Boolean {

                        AlertDialog.Builder(requireContext()).apply {
                            val options = arrayOf("Update", "Delete")
                            setItems(options, DialogInterface.OnClickListener() { _, which ->
                                if (which == 0) {
                                    val produkItem = item as DataItem
                                    val intent = Intent(view.context, Admin_Input::class.java)
                                    intent.putExtra(DataProduk.NAMA_PRODUK, produkItem.model)
                                    startActivityForResult(intent, 0)
                                    //finish()
                                }
                                if (which == 1) {
                                    val produkItem = item as DataItem
                                    //val key = db.key
                                    val query4 = FirebaseDatabase.getInstance().getReference().child("Data Produk")
                                    query4.child(produkItem.model.id_produk).removeValue()
                                    Toast.makeText(requireContext(),"Done", Toast.LENGTH_SHORT).show()
                                }
                            }).create().show()

                        }
                        return true
                    }
                })

                rc_dataproduk.adapter = adapter

            }

            override fun onCancelled(ds: DatabaseError) {

            }
        })
    }
}
