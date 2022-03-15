package com.newsapp.app.ui.activities.showArticle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsapp.databinding.ActivityShowArticleBinding

class ShowArticleActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityShowArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityShowArticleBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val url: String? = intent.getStringExtra("url")
        _binding.webView.loadUrl(url!!)
    }
}