package com.example.concert_app.view.main.fragment.message.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.user.ChatModel
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.databinding.ItemLeftChatBinding
import com.example.concert_app.databinding.ItemRightChatBinding
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.user
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class AdapterChat(
    private val itemList: ArrayList<ChatModel>
) : RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.item_tv_chat)
//        val imgUser: TextView = view.findViewById(R.id.item_iv_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_RIGHT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = itemList[position]
        holder.txtMessage.text = chat.message
//        Picasso.get().load(auth.curren)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].senderId != auth.currentUser?.uid) {
            MESSAGE_TYPE_LEFT
        } else {
            MESSAGE_TYPE_RIGHT
        }
    }
}