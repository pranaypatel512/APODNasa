package com.pranay.apodnasa.di

import android.app.Application
import com.pranay.apodnasa.data.local.APODPicturesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(application: Application) = APODPicturesDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providePicturesDao(database: APODPicturesDatabase) = database.getPicturesDao()
}
