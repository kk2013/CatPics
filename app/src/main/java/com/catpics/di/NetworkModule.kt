package com.catpics.di

import com.catpics.BuildConfig
import com.catpics.api.CatsApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    private val BASE_URL = "https://api.thecatapi.com"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
    }

    private fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            val url = request.url
                .newBuilder()
                .addQueryParameter("x-api-key", BuildConfig.API_KEY)
                .build()
            builder.url(url)
            chain.proceed(builder.build())
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(provideHeaderInterceptor())
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesCatsService(retrofit: Retrofit): CatsApi =
        retrofit.create(CatsApi::class.java)
}