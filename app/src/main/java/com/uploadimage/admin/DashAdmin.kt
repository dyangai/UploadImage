//@file:Suppress("UNREACHABLE_CODE")

package com.uploadimage.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.uploadimage.LoginActivity
import com.uploadimage.Model.ModelUser
import com.uploadimage.PagerAdapter
import com.uploadimage.R
import com.uploadimage.client.DashActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_dash_admin.*
import kotlinx.android.synthetic.main.fragment_showclient.*
import kotlinx.android.synthetic.main.user_row.view.*

class DashAdmin : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_admin)
        setSupportActionBar(toolbar)

        vp_main.adapter = PagerAdapter(supportFragmentManager)
        tabs_main.setupWithViewPager(vp_main)
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


   /* private fun fetchUsers(){

         ref.addListenerForSingleValueEvent(object : ValueEventListener{
             override fun onDataChange(p0: DataSnapshot) {
                 val adapter = GroupAdapter<GroupieViewHolder>()
                  // val cuser = mAuth.currentUser

                 p0.children.forEach{

                     val user = it.getValue(ModelUser::class.java)

                     //&& user.equals(mAuth.currentUser!!.uid
                     //?.id.equals(cuser!!.uid
                     //&& user.equals(cuser)
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
                 // adapter.setOnItemClickListener { item, view ->

                   //}
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

    private fun dashAdmin(){
        val intent = Intent(this, DashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }


   /* override fun onStart() {
        super.onStart()
        //val uid = mAuth.currentUser?.email.toString()

           // if (uid == "admin@gmail.com") {
                //uid.let {
                   // dashAdmin()
                //}
           // }
        mAuth.currentUser?.let {
            dashAdmin()
            }
    } */

*/
}

