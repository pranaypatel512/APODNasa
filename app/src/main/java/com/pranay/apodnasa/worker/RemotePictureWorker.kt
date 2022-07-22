package com.pranay.apodnasa.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.pranay.apodnasa.R
import com.pranay.apodnasa.data.repository.RemotePictureRepository
import com.pranay.apodnasa.model.ErrorResponse
import com.pranay.apodnasa.util.createNotification
import com.pranay.apodnasa.util.getTwoDates
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * this worker will fetch remote picture data from nasa api
 */
@HiltWorker
class RemotePictureWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remotePictureRepository: RemotePictureRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(
                applicationContext,
                applicationContext.getString(R.string.notification_title_getting_images)
            )
        )
    }

    override suspend fun doWork(): Result {
        return try {
            val dates = getTwoDates() // todo: get these dates in worker params
            if (dates != null) {
                val apiResponse =
                    remotePictureRepository.loadRemotePictures(dates.first, dates.second)
                if (apiResponse.isSuccessful && apiResponse.body() != null) {
                    remotePictureRepository.savePictures(apiResponse.body()?.toList() ?: listOf())
                    Result.success()
                } else {
                    val errorBody = Gson().fromJson(
                        apiResponse.errorBody()?.string(),
                        ErrorResponse::class.java
                    ) ?: ErrorResponse()
                    var errorValue: String? = null
                    if (errorBody.error != null) {
                        errorBody.error?.let {
                            errorValue = it.message
                        }
                    } else {
                        errorValue = apiResponse.message()
                    }
                    Result.failure(
                        Data.Builder()
                            .put(Error, errorValue)
                            .build()
                    )
                }

            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val TAG = "RemotePictureWorker"
        private const val NOTIFICATION_ID = 314
        const val Error = "Error"
    }
}