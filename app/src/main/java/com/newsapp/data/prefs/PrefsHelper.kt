package com.newsapp.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.newsapp.data.PrefsConstant.PrefsConstant.SHARED_PREF_KEY
import com.newsapp.data.PrefsConstant.PrefsConstant.key_user
import com.newsapp.data.model.User

class PrefsHelper {
    companion object {
        private lateinit var preferences: SharedPreferences
        private lateinit var gson: Gson

        fun init(context: Context) {
            preferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            gson = Gson()
        }

        fun saveUserLogin(user: User) {
            with(preferences.edit()) {
                putString(key_user, gson.toJson(user))
                apply()
            }
        }

        fun getUserLogin(): User? {
            return if (preferences.getString(key_user, null) != null) {
                gson.fromJson(preferences.getString(key_user, null), User::class.java)
            } else {
                null
            }
        }
    }
}