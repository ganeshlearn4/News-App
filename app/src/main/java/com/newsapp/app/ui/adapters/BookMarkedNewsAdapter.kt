package com.newsapp.app.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.app.ui.activities.showArticle.ShowArticleActivity
import com.newsapp.data.model.Article
import com.newsapp.databinding.ItemNewsBinding
import com.newsapp.interfaces.DataHelper
import javax.inject.Inject

class BookMarkedNewsAdapter() : RecyclerView.Adapter<BookMarkedNewsAdapter.NewsItemViewHolder>() {
    private lateinit var context: Context
    private lateinit var onDeleteClickListener: OnDeleteClickListener
    private var articles: ArrayList<Article> = ArrayList()

    @Inject
    lateinit var repository: DataHelper

    fun setContext(context: Context) {
        this.context = context
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.onDeleteClickListener = listener
    }

    fun setArticles(articles: List<Article>) {
        this.articles = ArrayList(articles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.setupData(context, articles[position], onDeleteClickListener)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class NewsItemViewHolder(val context: Context, private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupData(context: Context, article: Article, listener: OnDeleteClickListener) {
            binding.bookmark.visibility = View.GONE
            binding.delete.visibility = View.VISIBLE

            binding.article = article
            Glide.with(context)
                .load(article.urlToImage)
                .into(binding.newsImage)

            binding.delete.setOnClickListener {
                listener.onClick(article)
            }

            binding.parent.setOnClickListener {
                val intent = Intent(context, ShowArticleActivity::class.java)
                intent.putExtra("url", article.url)
                context.startActivity(intent)
            }
        }
    }

    interface OnDeleteClickListener {
        fun onClick(article: Article)
    }
}