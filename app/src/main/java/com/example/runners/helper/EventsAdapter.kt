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

class EventsAdapter(
    private val context: Context,
    private val eventsList: List<Events>,
    val eventSelect: (Events, Int) -> Unit
) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.MyViewHolder {
        return MyViewHolder(
            ItemEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsAdapter.MyViewHolder, position: Int) {
        val event = eventsList[position]
        holder.binding.name.text = event.name
        holder.binding.date.text ="Data da corrida: ${event.date}"
        holder.binding.location.text ="Local: ${event.location}"
        Glide.with(context)
            .load(event.image)
            .placeholder(R.drawable.image_default)
            .into(holder.binding.image)

        for (distance in event.distance) {
            println("distance")
            println(distance)

            if(distance != null){
                val textView = TextView(context).apply {
                    text = distance
                    setTextColor(ContextCompat.getColor(context, R.color.shark_950))
                    textSize = 12f
                    setPadding(9, 3, 9, 3)
                    background = ContextCompat.getDrawable(context, R.drawable.background_events_distance_items)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 10, 0)
                    }
                    this.layoutParams = layoutParams
                }

                holder.binding.distanceContainer.addView(textView)
            }else{
                null
            }
        }

        holder.itemView.setOnClickListener {
            eventSelect(event, position)
        }
    }

    override fun getItemCount() = eventsList.size

    inner class MyViewHolder(val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root)
}