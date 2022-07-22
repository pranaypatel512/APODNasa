package com.pranay.apodnasa.di

import com.google.gson.GsonBuilder
import com.pranay.apodnasa.BuildConfig
import com.pranay.apodnasa.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient): ApiService = Retrofit.Builder()
        .baseUrl(ApiService.NASA_API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .build()
        .create(ApiService::class.java)

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    private fun getGson() = GsonBuilder()
        .setLenient()
        .create()
}
