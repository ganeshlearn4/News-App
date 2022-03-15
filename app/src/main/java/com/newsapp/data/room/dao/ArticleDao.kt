package com.newsapp.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.newsapp.data.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticle(article: Article)

    @Query("SELECT * FROM articles WHERE title = :articleTitle")
    fun containsArticleTitle(articleTitle: String): Boolean

    @Delete
    fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAll(): LiveData<List<Article>>
}