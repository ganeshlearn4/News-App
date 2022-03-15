package com.newsapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsapp.app.Convertors
import com.newsapp.data.model.Article
import com.newsapp.data.room.dao.ArticleDao

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class Database: RoomDatabase() {
    abstract val articleDao: ArticleDao
}