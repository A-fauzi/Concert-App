package com.example.concert_app.view.main.fragment.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.data.concert.ConcertResponse
import com.example.concert_app.databinding.ItemAllConcertBinding
import com.squareup.picasso.Picasso

class AdapterListConcert(
    private var callClickListener: CallClickListener,
    private val listItem: ArrayList<ConcertResponse.Concert>
    ) : RecyclerView.Adapter<AdapterListConcert.ViewHolder>() {
    class ViewHolder(val binding: ItemAllConcertBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllConcertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listItem[position]) {
                Picasso.get()
                    .load(imgThumbnail)
                    .placeholder(R.drawable.image_ini)
                    .error(R.mipmap.ic_launcher)
                    .into(binding.itemIvAllConcert)
                binding.itemTvTitle.text = title
                binding.itemTvLocDate.text = "$locationName $date $time"

                binding.itemIvAllConcert.setOnClickListener {
                    callClickListener.onClickListenerItem(listItem[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ConcertResponse.Concert>) {
        listItem.clear()
        listItem.addAll(data)
        notifyDataSetChanged()
    }

    interface CallClickListener {
        fun onClickListenerItem(data: ConcertResponse.Concert)
    }
}