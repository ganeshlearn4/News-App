package com.newsapp.app.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.app.ui.activities.showArticle.ShowArticleActivity
import com.newsapp.data.model.Article
import com.newsapp.data.model.User
import com.newsapp.data.prefs.PrefsHelper
import com.newsapp.databinding.ItemNewsBinding
import com.newsapp.databinding.ItemWelcomeBinding
import com.newsapp.interfaces.DataHelper
import javax.inject.Inject

class NewsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var onBookMarkClickListener: OnBookMarkClickListener
    private lateinit var onSignOutListenerListener: OnSignOutListener
    private var articles: ArrayList<Article> = ArrayList()

    @Inject
    lateinit var repository: DataHelper

    private val user = PrefsHelper.getUserLogin()

    companion object ViewTypes {
        const val WELCOME = 0
        const val NEWS_ITEM = 1
    }


    fun setContext(context: Context) {
        this.context = context
    }

    fun setOnBookMarkClickListener(listener: OnBookMarkClickListener) {
        this.onBookMarkClickListener = listener
    }

    fun setOnSignOutListener(listener: OnSignOutListener) {
        this.onSignOutListenerListener = listener
    }

    fun setArticles(articles: List<Article>) {
        this.articles = ArrayList(articles)
        notifyDataSetChanged()
    }

    fun addArticles(articles: List<Article>) {
        val startPosition = this.articles.size + 1
        this.articles.addAll(articles)
        notifyItemRangeInserted(startPosition, articles.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            WELCOME
        } else {
            NEWS_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WELCOME -> {
                val binding =
                    ItemWelcomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WelcomeItemViewHolder(parent.context, binding)
            }
            else -> {
                val binding =
                    ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NewsItemViewHolder(parent.context, binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WelcomeItemViewHolder) {
            holder.setupWelcome(user!!, onSignOutListenerListener)
        } else if (holder is NewsItemViewHolder) {
            holder.setupData(context, articles[position - 1], onBookMarkClickListener)
        }
    }

    override fun getItemCount(): Int {
        return articles.size + 1
    }

    class NewsItemViewHolder(val context: Context, val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupData(context: Context, article: Article, listener: OnBookMarkClickListener) {
            binding.article = article
            Glide.with(context)
                .load(article.urlToImage)
                .into(binding.newsImage)

            binding.bookmark.setOnClickListener {
                listener.onClick(article)
            }

            binding.parent.setOnClickListener {
                val intent = Intent(context, ShowArticleActivity::class.java)
                intent.putExtra("url", article.url)
                context.startActivity(intent)
            }
        }
    }

    class WelcomeItemViewHolder(val context: Context, val binding: ItemWelcomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupWelcome(user: User, listener: OnSignOutListener) {
            Glide.with(context)
                .load(user.photoUrl)
                .into(binding.userPic)

            binding.welcomeMessage.text = "Welcome ${user.displayName}"
            binding.email.text = user.email

            binding.signOut.setOnClickListener {
                listener.onSignOut()
            }
        }
    }

    interface OnBookMarkClickListener {
        fun onClick(article: Article)
    }

    interface OnSignOutListener {
        fun onSignOut()
    }
}