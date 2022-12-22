package com.example.concert_app.view.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.databinding.FragmentTicketBinding
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.databaseReference
import com.example.concert_app.view.main.fragment.adapter.AdapterListPerson
import com.example.concert_app.view.main.fragment.message.ChatActivity
import com.example.concert_app.view.main.fragment.message.MessageFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TicketFragment : Fragment(), AdapterListPerson.CallClickListener {

    private lateinit var binding: FragmentTicketBinding

    private lateinit var listPerson: ArrayList<UserModel>
    private lateinit var rvList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(inflater, container, false)

        getListPerson()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getListPerson() {
        rvList = binding.rvListPerson
        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        listPerson = arrayListOf<UserModel>()

        databaseReference = FirebaseServiceInstance.firebaseDatabase.getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listPerson.clear()
                    for (list in snapshot.children) {
                        val lst = list.getValue(UserModel::class.java)
                        if (lst != null) {
                            listPerson.add(lst)
                        }
                    }
                    rvList.adapter = AdapterListPerson(this@TicketFragment ,listPerson)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(MessageFragment.TAG, error.message)
            }

        })

    }

    override fun onClickItem(data: UserModel) {
        val bundle = Bundle()
        bundle.putString("name", data.name)
        bundle.putString("photo", data.photoUrl)
        bundle.putString("userId", data.id)

        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

