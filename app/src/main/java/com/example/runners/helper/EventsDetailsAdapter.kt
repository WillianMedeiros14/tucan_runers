package com.example.runners.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runners.R
import com.example.runners.databinding.ItemEventsBinding
import com.example.runners.model.Events

class EventsDetailsAdapter(
    private val context: Context,
    private val event: Events,
    val registrationClick: (Events) -> Unit
) : RecyclerView.Adapter<EventsDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text = event.name
        holder.binding.date.text = "Data da corrida: ${event.date}"
        holder.binding.location.text = "Local: ${event.location}"
        Glide.with(context)
            .load(event.image)
            .placeholder(R.drawable.image_default)
            .into(holder.binding.image)



        holder.itemView.setOnClickListener {
            registrationClick(event)
        }
    }

    override fun getItemCount() = 1

    inner class MyViewHolder(val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
