package com.example.concert_app.view.main.fragment.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.data.user.Message
import com.example.concert_app.databinding.FragmentMessageBinding
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.databaseReference
import com.example.concert_app.utils.FirebaseServiceInstance.firebaseDatabase
import com.example.concert_app.utils.Preference
import com.example.concert_app.view.main.fragment.adapter.ListPersonMessageAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MessageFragment : Fragment() {

    companion object {
        const val TAG = "MessageFragment"
    }

    private lateinit var binding: FragmentMessageBinding

    private lateinit var listPersonMessage: ArrayList<Message>
    private lateinit var rvList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        getListPersonMessage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun getListPersonMessage() {
        rvList = binding.rvListPersonMessage
        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        listPersonMessage = arrayListOf<Message>()

        val receiverId = Preference.loadData(requireActivity(), "RECEIVER_ID_KEY")

        databaseReference = firebaseDatabase.getReference("receiver").child(auth.currentUser!!.uid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                   listPersonMessage.clear()
                   for (list in snapshot.children) {
                       val lst = list.getValue(Message::class.java)
                       if (lst != null) {
                           listPersonMessage.add(lst)
                       }
                   }
                   Log.i(TAG, listPersonMessage.toString())
                   rvList.adapter = ListPersonMessageAdapter(listPersonMessage)
               }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }

        })

    }
}