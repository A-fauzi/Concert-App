package com.example.concert_app.view.main.fragment.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.user.ChatModel
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.databaseReference
import com.example.concert_app.utils.FirebaseServiceInstance.firebaseDatabase
import com.example.concert_app.utils.Libs
import com.example.concert_app.utils.Libs.randomString
import com.example.concert_app.utils.Libs.simpleDateFormat
import com.example.concert_app.view.main.fragment.message.adapter.AdapterChat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {

    private lateinit var textName: TextView
    private lateinit var photo: CircleImageView
    private lateinit var inputSendMessage: EditText
    private lateinit var btnSend: Button

    private var chatList = ArrayList<ChatModel>()
    private lateinit var rvChat: RecyclerView

    private fun initView(){
        textName = findViewById(R.id.tv_user_name_receiver)
        photo = findViewById(R.id.iv_person_receiver)
        inputSendMessage = findViewById(R.id.et_send_message)
        btnSend = findViewById(R.id.btn_send_message)
        rvChat = findViewById(R.id.rv_chat)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initView()

        rvChat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Picasso.get()
            .load(intent.extras?.getString("photo"))
            .placeholder(R.drawable.img_placeholder_man)
            .error(R.mipmap.ic_launcher)
            .into(photo)
        textName.text =  intent.extras?.getString("name")
    }

    override fun onStart() {
        super.onStart()

        val userId = intent.extras?.getString("userId")
        btnSend.setOnClickListener {
            val message = inputSendMessage.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()
            } else {
                if (userId != null && photo != null) {
                    databaseReference = firebaseDatabase.getReference("users").child(auth.currentUser?.uid ?: "")
                    databaseReference.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val photoUrl = snapshot.child("photoUrl").value.toString()
                                sendMessage(auth.currentUser?.uid ?: "", userId, message, photoUrl)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                inputSendMessage.setText("")
            }
        }

        if (userId != null) {
            readMessage(auth.currentUser?.uid ?: "", userId)
        }
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, photoUrl: String) {
        databaseReference = firebaseDatabase.getReference("messages").child(randomString(25))
        val hashmap : HashMap<String, String> = HashMap()
        hashmap["senderId"] = senderId
        hashmap["receiverId"] = receiverId
        hashmap["message"] = message
        hashmap["photoUrl"] = photoUrl
        hashmap["time"] = simpleDateFormat()

        databaseReference.setValue(hashmap).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Success sending message", Toast.LENGTH_SHORT).show()
                val photo = intent.extras?.getString("photo")
                if (photo != null) {
                    receiver(receiverId, message, photo)
                }
            } else {
                Toast.makeText(this, "Failure sending message", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readMessage(senderId: String, receiverId: String) {
        databaseReference = firebaseDatabase.getReference("messages")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(ChatModel::class.java)
                    if (chat?.senderId.equals(senderId) && chat?.receiverId.equals(receiverId) ||
                        chat?.senderId.equals(receiverId) && chat?.receiverId.equals(senderId)) {
                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                }
                chatList.sortWith(compareBy { it.time })
                val chatAdapter = AdapterChat(chatList)
                rvChat.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun receiver(receiverId: String, message: String, photoUrl: String) {
        databaseReference = firebaseDatabase.getReference("receiver").child(auth.currentUser?.uid ?: "").child(receiverId)
        val hashmap : HashMap<String, String> = HashMap()
        hashmap["receiverId"] = receiverId
        hashmap["message"] = message
        hashmap["photoUrl"] = photoUrl
        hashmap["time"] = simpleDateFormat()

        databaseReference.setValue(hashmap).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Receiver saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Receiver not saved", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}