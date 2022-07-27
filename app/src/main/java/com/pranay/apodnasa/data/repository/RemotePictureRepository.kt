package com.pranay.apodnasa.data.repository

import com.google.gson.Gson
import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.data.remote.api.ApiService
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.model.ErrorResponse
import javax.inject.Inject

interface RemotePictureRepository {
    suspend fun loadRemotePictures(
        startDate: String,
        endDate: String
    ): Pair<Boolean, ErrorResponse?>

    suspend fun savePictures(pictures: List<APODPictureItem>)
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability.
 */
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
    ): Pair<Boolean, ErrorResponse?> {

        val apiResponse = apiService.getAPODPictures(startDate, endDate)
        return if (apiResponse.isSuccessful && apiResponse.body() != null) {
            aPODPictureDao.addPictures(apiResponse.body()?.toList() ?: listOf())
            Pair(true, null)
        } else {
            val errorBody = Gson().fromJson(
                apiResponse.errorBody()?.string(),
                ErrorResponse::class.java
            ) ?: ErrorResponse(message = apiResponse.message())

            Pair(false, errorBody)
        }

    }

    override suspend fun savePictures(pictures: List<APODPictureItem>) {
        aPODPictureDao.apply {
            deleteAllPictures()
            addPictures(pictures)
        }
    }

}
