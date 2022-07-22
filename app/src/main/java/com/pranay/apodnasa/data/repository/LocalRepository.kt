package com.pranay.apodnasa.data.repository

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * A repository which provides resource from local database.
 *
 * [RESULT] represents the type for a database.
 */
@ExperimentalCoroutinesApi
abstract class LocalRepository<RESULT> {

    fun asFlow() = flow<Resource<RESULT>> {

        // Emit Database content
        emit(Resource.Success(fetchPictureFromLocal().first()))

    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Database error! Can't get pictures list."))
    }

    /**
     * Retrieves all data from persistence storage.
     */
    @MainThread
    protected abstract fun fetchPictureFromLocal(): Flow<RESULT>

}
