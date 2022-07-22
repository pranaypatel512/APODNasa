package com.pranay.apodnasa.data.repository

import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.data.remote.api.ApiService
import com.pranay.apodnasa.model.APODListResponse
import dev.shreyaspatil.foodium.data.repository.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface RemotePictureRepository {
    fun loadRemotePictures(startDate: String, endDate: String): Flow<Resource<APODListResponse>>
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
    override fun loadRemotePictures(
        startDate: String,
        endDate: String
    ): Flow<Resource<APODListResponse>> {
        return object : RemoteRepository<APODListResponse, APODListResponse>() {

            override suspend fun saveRemoteData(response: APODListResponse) =
                aPODPictureDao.addPictures(response.toList())

            override suspend fun fetchFromRemote(): Response<APODListResponse> =
                apiService.getAPODPictures(
                    startDate = startDate,
                    endDate = endDate
                )
        }.asFlow()
    }

}
