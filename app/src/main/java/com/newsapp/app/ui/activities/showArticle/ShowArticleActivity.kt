package com.newsapp.app.ui.activities.showArticle

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.newsapp.databinding.ActivityShowArticleBinding

class ShowArticleActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityShowArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityShowArticleBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val url: String? = intent.getStringExtra("url")

        _binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                _binding.progressBar.visibility = View.GONE
            }
        }
        _binding.webView.loadUrl(url!!)
    }
}