package com.example.runners.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.runners.databinding.FragmentNewsDetailsBinding

class NewsDetailsFragment : Fragment() {

    private val args: NewsDetailsFragmentArgs by navArgs()

    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.show()

        getArgs()
        initClicks()
    }

    private fun getArgs() {
        args.newsUrl.let { url ->
            binding.webview.apply {
                setBackgroundColor(android.graphics.Color.WHITE)
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                if (url != null) {
                    loadUrl(url)
                }
            }
        }
    }

    private fun initClicks() {
        binding.backButton.setOnClickListener { backButton() }
    }

    private fun backButton() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

}