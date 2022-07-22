package com.pranay.apodnasa.di

import com.pranay.apodnasa.data.repository.LocalPictureRepository
import com.pranay.apodnasa.data.repository.LocalPictureRepositoryImpl
import com.pranay.apodnasa.data.repository.RemotePictureRepository
import com.pranay.apodnasa.data.repository.RemotePictureRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * local and remote Pictures Repository that we are using to get and store pictures data
 */
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
abstract class PicturesRepositoryModule {

    @Binds
    abstract fun bindLocalPictureRepository(repository: LocalPictureRepositoryImpl): LocalPictureRepository

    @Binds
    abstract fun bindRemotePictureRepository(repository: RemotePictureRepositoryImpl): RemotePictureRepository
}
