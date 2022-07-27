package com.pranay.apodnasa.data.repository

import androidx.annotation.MainThread
import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.model.APODPictureItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

interface LocalPictureRepository {
    fun getAllMediaItems(): Flow<List<APODPictureItem>>
    fun getPictureByUrl(imageUrl: String): Flow<APODPictureItem>
}

/**
 * Singleton repository for fetching data from database
 * for offline capability. This is Single source of data.
 */
class LocalPictureRepositoryImpl @Inject constructor(
    private val aPODPictureDao: APODPictureDao
) : LocalPictureRepository {

    /**
     * Fetched the pictures from database.
     */
    override fun getAllMediaItems(): Flow<List<APODPictureItem>> = aPODPictureDao.getAllPictures()

    /**
     * Retrieves a picture with specified [imageUrl].
     * @param imageUrl Unique image Url of a [APODPictureItem].
     * @return [APODPictureItem] data fetched from the database.
     */
    @MainThread
    override fun getPictureByUrl(imageUrl: String): Flow<APODPictureItem> =
        aPODPictureDao.getPictureByUrl(imageUrl).distinctUntilChanged()
}
