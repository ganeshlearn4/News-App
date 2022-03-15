package com.newsapp.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.newsapp.BuildConfig
import com.newsapp.R
import com.newsapp.app.ui.adapters.BookMarkedNewsAdapter
import com.newsapp.app.ui.adapters.NewsAdapter
import com.newsapp.data.Constants
import com.newsapp.data.network.NewsApi
import com.newsapp.data.network.NewsApiHelper
import com.newsapp.data.repository.DataRepository
import com.newsapp.data.room.Database
import com.newsapp.interfaces.DataHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideAPIKey(): String {
        // return "614d489c8d03479cb8ae86c91f991ce8"
        return BuildConfig.NEWS_API_KEY
    }

    // Provides Arraylist of Interceptor
    @Singleton
    @Provides
    fun provideInterceptors(): ArrayList<Interceptor> {
        val interceptors: ArrayList<Interceptor> = ArrayList()
        val level: HttpLoggingInterceptor.Level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val loggingInterceptor: Interceptor = HttpLoggingInterceptor().setLevel(level)
        interceptors.add(loggingInterceptor)
        return interceptors
    }

    // Provides OkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptors: ArrayList<Interceptor>): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder().followRedirects(false)
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }
        builder.connectTimeout(120, TimeUnit.SECONDS)
        builder.readTimeout(120, TimeUnit.SECONDS)
        return builder.build()
    }

    // Provides Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.SERVER_API)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    fun provideNewsApiHelper(newsApi: NewsApi): NewsApiHelper {
        return NewsApiHelper(newsApi)
    }

    @Singleton
    @Provides
    fun provideNewsAdapter(): NewsAdapter {
        return NewsAdapter()
    }

    @Singleton
    @Provides
    fun provideBookMarkedNewsAdapter(): BookMarkedNewsAdapter {
        return BookMarkedNewsAdapter()
    }

    @Singleton
    @Provides
    fun provideGSON(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideDataRepository(apiHelper: NewsApiHelper, database: Database): DataRepository {
        return DataRepository(apiHelper, database)
    }

    @Singleton
    @Provides
    fun provideDataRepositoryHelper(dataRepository: DataRepository): DataHelper {
        return dataRepository.returnDataHelper()
    }
}