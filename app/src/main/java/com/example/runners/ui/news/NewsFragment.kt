package com.example.runners.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.runners.databinding.FragmentNewsBinding
import com.example.runners.helper.NewsApiService
import com.example.runners.helper.RetrofitInstance
import com.example.runners.helper.dpToPx
import com.example.runners.model.NewsItemResponse
import com.example.runners.model.NewsResponseData
import com.example.runners.model.Source
import com.example.runners.ui.adapter.ItemSpacingDecoration
import com.example.runners.ui.adapter.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        recyclerView = binding.recyclerView
        progressBar = binding.progressBar
        loadingMessage = binding.loadingMessage

        newsAdapter = NewsAdapter { url ->
//            val action = NewsFragmentDirections.actionNewsHomeFragmentToNewsDetailsScreenFragment(url)
//            findNavController().navigate(action)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = newsAdapter

        val spacingInPixels = dpToPx(requireContext(), 16)
        val itemSpacingDecoration = ItemSpacingDecoration(spacingInPixels)
        recyclerView.addItemDecoration(itemSpacingDecoration)

        fetchNews()

        initClicks()
    }

    private fun initClicks() {
//        binding.backButton.setOnClickListener { backButton() }
    }

    private fun fetchNews() {
        progressBar.visibility = View.VISIBLE
        loadingMessage.visibility = View.VISIBLE

        val newsApiService = RetrofitInstance.getInstance().create(NewsApiService::class.java)
        newsApiService.getNews(apiKey = "49bad1ab6aae47ac9e17b91bf7be7222").enqueue(object : Callback<NewsItemResponse> {
            override fun onResponse(call: Call<NewsItemResponse>, response: Response<NewsItemResponse>) {
                if (response.isSuccessful) {
                    response.body()?.articles?.let { articles ->
                        val newsList = articles.mapNotNull { article ->
                            if (article.title != "[Removed]" && !article.title.isNullOrEmpty()) {
                                NewsResponseData(
                                    source = Source(id = null, name = article.source.name ?: "Unknown Source"),
                                    author = article.author ?: "Unknown Author",
                                    title = article.title ?: "Unknown Title",
                                    description = article.description ?: "Unknown Description",
                                    url = article.url ?: "Unknown URL",
                                    urlToImage = article.urlToImage ?: "",
                                    publishedAt = article.publishedAt,
                                    content = article.content ?: ""
                                )
                            } else {
                                null
                            }
                        }
                        progressBar.visibility = View.GONE
                        loadingMessage.visibility = View.GONE

                        newsAdapter.addNews(newsList)
                    }
                }
            }

            override fun onFailure(call: Call<NewsItemResponse>, t: Throwable) {
                t.printStackTrace() // Trate erros de rede ou de API aqui

                progressBar.visibility = View.GONE
                loadingMessage.visibility = View.GONE
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
