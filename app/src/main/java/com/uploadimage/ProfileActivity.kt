
package com.uploadimage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_profilegal.*
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_input)

        /*
        ref = FirebaseDatabase.getInstance().getReference("Produk")

        search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val proname = search.text.toString().trim()

                loadProduk(proname)
            }

        })
*/
    }

 /*   private fun loadProduk(proname : String) {


        if(proname.isEmpty()){

            FirebaseRecyclerAdapter.cleanup()
            rc_dataproduk.adapter = FirebaseRecyclerAdapter

        }else {

            val firebaseSearchQuery = ref.orderByChild("name").startAt(name).endAt(name + "\uf8ff")


            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<ModelUser, FriendsViewHolder>(

                FriendsListModel::class.java,
                R.layout.friend_list_layout,
                FriendsViewHolder::class.java,
                firebaseSearchQuery
            ) {
                override fun populateViewHolder(viewHolder: FriendsViewHolder, model: FriendsListModel?, position: Int) {

                    viewHolder.mView.userName.setText(model?.name)
                    viewHolder.mView.userStatus.setText(model?.status)

                    Picasso.with(applicationContext).load(model?.image).into(viewHolder.mView.userImageView)

                }

            }

        }

        mFriendList.adapter = FirebaseRecyclerAdapter

    }



    // Friends View Holder
    class FriendsViewHolder(var mView : View) : RecyclerView.ViewHolder(mView) {

    }
*/
}
