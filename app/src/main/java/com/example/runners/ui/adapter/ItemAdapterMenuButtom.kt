package com.example.runners.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.runners.R
import com.example.runners.model.ItemMenuButton

class ItemAdapter(
    private val context: Context,
    private val items: List<ItemMenuButton>,
    private val onItemClick: (ItemMenuButton) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val text: TextView = view.findViewById(R.id.text)
        val arrow: ImageView = view.findViewById(R.id.arrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu_buttom, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.text.text = item.text
        if (item.isLogout) {
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.shark_950))
        }
        holder.arrow.setImageResource(R.drawable.arrow_forward)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
