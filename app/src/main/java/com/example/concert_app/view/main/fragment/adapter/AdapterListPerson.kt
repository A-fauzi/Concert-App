package com.example.concert_app.view.main.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.databinding.ItemListPersonBinding
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.squareup.picasso.Picasso

class AdapterListPerson(
    private val callClickListener: CallClickListener,
    private val itemList: ArrayList<UserModel>
) : RecyclerView.Adapter<AdapterListPerson.ViewHolder>() {
    class ViewHolder(val binding: ItemListPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(itemList[position]) {
                if (id != auth.currentUser?.uid) {
                    Picasso.get()
                        .load(photoUrl)
                        .placeholder(R.drawable.img_placeholder_man)
                        .error(R.mipmap.ic_launcher)
                        .into(binding.ivItemPerson)
                    binding.tvItemName.text = name
                    binding.tvItemTitle.text = title
                    binding.itemLayoutCard.setOnClickListener {
                        callClickListener.onClickItem(itemList[position])
                    }
                } else {
                    binding.itemLayoutCard.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface CallClickListener {
        fun onClickItem(data: UserModel)
    }
}