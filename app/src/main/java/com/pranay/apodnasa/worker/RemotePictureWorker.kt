package com.pranay.apodnasa.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.pranay.apodnasa.R
import com.pranay.apodnasa.data.repository.RemotePictureRepository
import com.pranay.apodnasa.util.createNotification
import com.pranay.apodnasa.util.getTwoDates
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * this worker will fetch remote picture/videos data from nasa api
 */
@HiltWorker
class RemotePictureWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remotePictureRepository: RemotePictureRepository
) : CoroutineWorker(appContext, workerParams) {

    //provide ForegroundInfo instance if the WorkRequest is marked as expedited, so while running job user can see the notification.
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
            val dates = getTwoDates() // can get there two date from work params
            if (dates != null) {
                val apiResponse =
                    remotePictureRepository.loadRemotePictures(
                        dates.first,
                        dates.second
                    )
                if (apiResponse.first) { // api response success
                    Result.success()
                } else {
                    // evaluate error response and send error data in failure result
                    val errorValue: String? =
                        apiResponse.second?.message ?: apiResponse.second?.error?.message
                    Result.failure(
                        errorValue.errorData()
                    )
                }

            } else {
                Result.failure(
                    applicationContext.getString(R.string.str_try_again_case).errorData()
                )
            }
        } catch (e: Exception) {
            Result.failure(
                applicationContext.getString(R.string.str_try_again_case).errorData()
            )
        }
    }

    companion object {
        const val TAG = "RemotePictureWorker"
        private const val NOTIFICATION_ID = 314
        const val Error = "Error"
    }

    private fun String?.errorData(): Data {
        return Data.Builder()
            .put(Error, this)
            .build()
    }
}