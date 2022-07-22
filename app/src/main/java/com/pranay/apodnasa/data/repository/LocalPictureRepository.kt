package com.pranay.apodnasa.data.repository

import androidx.annotation.MainThread
import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.model.APODPictureItem
import dev.shreyaspatil.foodium.data.repository.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

interface LocalPictureRepository {
    fun getAllPictures(): Flow<Resource<List<APODPictureItem>>>
    fun getPictureByUrl(imageUrl: String): Flow<APODPictureItem>
}

/**
 * Singleton repository for fetching data from database
 * for offline capability. This is Single source of data.
 */
@ExperimentalCoroutinesApi
class LocalPictureRepositoryImpl @Inject constructor(
    private val aPODPictureDao: APODPictureDao
) : LocalPictureRepository {

    /**
     * Fetched the pictures from database.
     */
    override fun getAllPictures(): Flow<Resource<List<APODPictureItem>>> =
        object : LocalRepository<List<APODPictureItem>>() {
            override fun fetchPictureFromLocal(): Flow<List<APODPictureItem>> = aPODPictureDao.getAllPictures()
        }.asFlow()

    /**
     * Retrieves a picture with specified [imageUrl].
     * @param imageUrl Unique image Url of a [APODPictureItem].
     * @return [APODPictureItem] data fetched from the database.
     */
    @MainThread
    override fun getPictureByUrl(imageUrl: String): Flow<APODPictureItem> =
        aPODPictureDao.getPictureByUrl(imageUrl).distinctUntilChanged()
}
