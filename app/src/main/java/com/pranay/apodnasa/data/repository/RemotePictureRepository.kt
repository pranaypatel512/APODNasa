package com.pranay.apodnasa.data.repository

import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.data.remote.api.ApiService
import com.pranay.apodnasa.model.APODListResponse
import com.pranay.apodnasa.model.APODPictureItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import javax.inject.Inject

interface RemotePictureRepository {
    suspend fun loadRemotePictures(
        startDate: String,
        endDate: String
    ): Response<APODListResponse>

    suspend fun savePictures(pictures: List<APODPictureItem>)
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability.
 */
@ExperimentalCoroutinesApi
class RemotePictureRepositoryImpl @Inject constructor(
    private val aPODPictureDao: APODPictureDao,
    private val apiService: ApiService
) : RemotePictureRepository {

    /**
     * Fetched the pictures from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override suspend fun loadRemotePictures(
        startDate: String,
        endDate: String
    ): Response<APODListResponse> {
        return apiService.getAPODPictures(startDate, endDate)

    }

    override suspend fun savePictures(pictures: List<APODPictureItem>) {
        aPODPictureDao.addPictures(pictures)
    }

}
