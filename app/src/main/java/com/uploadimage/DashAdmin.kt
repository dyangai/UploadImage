@file:Suppress("UNREACHABLE_CODE")

package com.uploadimage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.uploadimage.Model.ModelUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_dash_admin.*
import kotlinx.android.synthetic.main.data_produk.*
import kotlinx.android.synthetic.main.user_row.*
import kotlinx.android.synthetic.main.user_row.view.*

class DashAdmin : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_admin)
        supportActionBar?.title = "Dashboard"
        supportActionBar!!.elevation = 0f

        rc_view.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        image_view.setOnClickListener{

        }

        filter.setOnClickListener{

        }


        ref = FirebaseDatabase.getInstance().getReference("/Users")
        fetchUsers()
        searchuser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val username = searchuser.text.toString().trim()
                searchUsers(username)
            }

        })


    }
   private fun fetchUsers(){

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{
                    val user = it.getValue(ModelUser::class.java)

                    //&& user.equals(mAuth.currentUser!!.uid
                    if(user != null )  {
                      adapter.add(UserItem(user))
                    }

                }
            rc_view.adapter = adapter
           }

           override fun onCancelled(p0: DatabaseError) {

           }
        })

   }

    private fun searchUsers(username : String){
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .orderByChild("name")
            .startAt(username)
            .endAt(username + "\uf8ff")

        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                //if (search.search.text.toString() == "") {
                //adapter.clear()

                ds.children.forEach {

                    val user = it.getValue(ModelUser::class.java)

                    if (user != null) {
                        adapter.add(UserItem(user))

                    }

                }
                /*  adapter.setOnItemClickListener { item, view ->
                      val produkItem = item as DataItem

                      val intent = Intent(view.context, ILaporan::class.java)
                      intent.putExtra(NAMA_PRODUK, produkItem.model)
                      startActivityForResult(intent, 0)
                      finish()

                  }*/
                rc_view.adapter = adapter
                //}
            }

            override fun onCancelled(ds: DatabaseError) {

            }
        })
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
        return super.onOptionsItemSelected(item!!)
    }

    private fun logout(){
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}



class UserItem(val user: ModelUser): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(user.image!!.isEmpty()){
            viewHolder.itemView.username.text = user.name
            viewHolder.itemView.outletrow.text = user.outlet
        }else {
            viewHolder.itemView.username.text = user.name
            viewHolder.itemView.outletrow.text = user.outlet
            Picasso.get().load(user.image).into(viewHolder.itemView.imageView)
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row
    }


}

