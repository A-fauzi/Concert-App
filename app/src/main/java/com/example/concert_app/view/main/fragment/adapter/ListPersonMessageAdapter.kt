package com.example.concert_app.view.main.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.data.user.Message
import com.example.concert_app.databinding.ItemListMessagePersonBinding
import com.squareup.picasso.Picasso

class ListPersonMessageAdapter(
    private val listItem: ArrayList<Message>
) : RecyclerView.Adapter<ListPersonMessageAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemListMessagePersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListMessagePersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listItem[position]) {
                Picasso.get()
                    .load(photoUrl)
                    .placeholder(R.drawable.img_placeholder_man)
                    .error(R.mipmap.ic_launcher)
                    .into(binding.ivItemPersonMessage)
                binding.tvItemName.text = name
                binding.tvItemMessagePlaceholder.text = message
                binding.tvItemTimeMessage.text = time
            }
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}