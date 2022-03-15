package com.newsapp.app

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.newsapp.data.model.Source
import dagger.hilt.EntryPoint
import javax.inject.Inject
import javax.inject.Singleton

class Convertors {
    val gson: Gson = Gson()

    @TypeConverter
    fun mapSourceToJson(source: Source): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun mapFromStringToSource(string: String): Source {
        return gson.fromJson(string, Source::class.java)
    }
}