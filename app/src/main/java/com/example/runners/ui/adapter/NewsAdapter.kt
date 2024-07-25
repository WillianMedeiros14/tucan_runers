package com.example.runners.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runners.R
import com.example.runners.model.NewsResponseData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val newsList: MutableList<NewsResponseData> = ArrayList()

    fun addNews(news: List<NewsResponseData>) {
        newsList.clear()
        newsList.addAll(news)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList.getOrNull(position)
//        if (newsItem != null) {
        if (newsItem != null) {
            holder.bind(newsItem)
            holder.itemView.setOnClickListener {
                newsItem.url?.let { it1 -> onItemClick(it1) }
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val source: TextView = itemView.findViewById(R.id.source)
        private val logo_source: ImageView = itemView.findViewById(R.id.logo_source)
        private val publishedAt: TextView = itemView.findViewById(R.id.publishedAt)

        fun bind(news: NewsResponseData) {
            titleTextView.text = news.title ?: "No Title"
            descriptionTextView.text = news.description ?: "No Description"
            source.text = news.source.name ?: "No source"

            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val targetFormat =
                SimpleDateFormat("dd/MM/yyyy 'às' HH:mm 'horas'", Locale.getDefault())
            val date: Date? = originalFormat.parse(news.publishedAt)
            val formattedDate: String = if (date != null) {
                targetFormat.format(date)
            } else {
                "No publishedAt"
            }
            publishedAt.text = formattedDate


            if (!news.urlToImage.isNullOrEmpty()) {
                Glide.with(itemView.context).load(news.urlToImage).into(imageView)
            } else {
                imageView.setImageResource(R.drawable.dias_paris)
            }

                val imageRes = when (news.source.name) {
                    "Metropoles.com" -> R.drawable.logo_portal_metropoles
                    "BBC News" -> R.drawable.logo_bbc_news
                    "Terra.com.br" -> R.drawable.logo_terra
                    "Ig.com.br" -> R.drawable.logo_ig
                    "Uol.com.br" -> R.drawable.logo_uol
                    "Observador.pt" -> R.drawable.logo_obervador
                    else -> R.drawable.image_default
                }
            logo_source.setImageResource(imageRes)


        }
    }
}
