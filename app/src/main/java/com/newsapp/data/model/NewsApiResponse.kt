package com.newsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.newsapp.app.Convertors

data class NewsApiResponse(
    val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    val articles: List<Article>
)

@Entity(tableName = "Articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @TypeConverters(Convertors::class)
    @ColumnInfo(name = "source")
    val source: Source,

    @ColumnInfo(name = "author")
    var author: String? = null,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "urlToImage")
    var urlToImage: String = "",

    @ColumnInfo(name = "publishedAt")
    var publishedAt: String = "",

    @ColumnInfo(name = "content")
    var content: String = ""
) {
    override fun toString(): String {
        return "Article(id=$id, source=$source, author='$author', title='$title', description='$description', url='$url', urlToImage='$urlToImage', publishedAt='$publishedAt', content='$content')"
    }
}

data class Source(
    val id: String,
    val name: String
)