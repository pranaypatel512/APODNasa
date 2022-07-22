package com.pranay.apodnasa.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from remote end point.
 *
 * [REQUEST] represents the type for network.
 */
@ExperimentalCoroutinesApi
abstract class RemoteRepository<RESULT, REQUEST> {

    fun asFlow() = flow<Resource<RESULT>> {

        // Fetch latest pictures from remote
        val apiResponse = fetchFromRemote()

        // Parse body
        val remotePicturesResponse = apiResponse.body()

        // Check for response validation
        if (apiResponse.isSuccessful && remotePicturesResponse != null) {
            // Save pictures into the persistence storage
            saveRemoteData(remotePicturesResponse)
        } else {
            // Something went wrong! Emit Error state.
            emit(Resource.Failed(apiResponse.message()))
        }

    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Network error! Can't get requested pictures."))
    }

    /**
     * Saves retrieved from remote into the persistence storage.
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Fetches [Response] from the remote end point.
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}
