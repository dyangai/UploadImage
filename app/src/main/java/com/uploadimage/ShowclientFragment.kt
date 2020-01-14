package com.uploadimage


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.uploadimage.Model.ModelUser
import com.uploadimage.admin.Admin_Input
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_showclient.*
import kotlinx.android.synthetic.main.user_row.view.*


class ShowclientFragment : Fragment() {
    lateinit var ref: DatabaseReference

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showclient, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rc_view.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        filter.setOnClickListener{
            val intent = Intent(requireContext(), Admin_Input::class.java)
            startActivity(intent)
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

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{

                    val user = it.getValue(ModelUser::class.java)

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

        query.addValueEventListener(object: ValueEventListener {
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

                rc_view.adapter = adapter

            }


            override fun onCancelled(ds: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: ModelUser?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(user?.image!!.isEmpty()){
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

