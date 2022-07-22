package com.pranay.apodnasa.di

import com.google.gson.GsonBuilder
import com.pranay.apodnasa.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): ApiService = Retrofit.Builder()
        .baseUrl(ApiService.NASA_API_URL)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .build()
        .create(ApiService::class.java)

    private fun getGson() = GsonBuilder()
        .setLenient()
        .create()
}
