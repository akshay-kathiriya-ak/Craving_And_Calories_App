package com.example.cravingcalories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cravingcalories.databinding.NotifactionItemBinding

class NotifactionAdapter(private var notifaction:ArrayList<String>, private var notifictionImage: ArrayList<Int>):RecyclerView.Adapter<NotifactionAdapter.NotifictionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifictionViewHolder {
        val binding = NotifactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotifictionViewHolder(binding)
    }



    override fun onBindViewHolder(holder: NotifictionViewHolder, position: Int) {
      holder.bind(position)
    }
    override fun getItemCount(): Int = notifaction.size
    inner class NotifictionViewHolder(private val binding: NotifactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                notificatonTextView.text = notifaction[position]
                notificationimageview.setImageResource(notifictionImage[position])
            }
        }

    }
}