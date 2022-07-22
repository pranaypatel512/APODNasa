
package com.pranay.apodnasa.data.remote.api

import com.pranay.apodnasa.BuildConfig
import com.pranay.apodnasa.data.remote.api.ApiService.Companion.NASA_API_URL
import com.pranay.apodnasa.model.APODListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service to fetch pictures using nasa end point [NASA_API_URL].
 */
interface ApiService {

    @GET("apod")
    suspend fun getAPODPictures(@Query("start_date") startDate: String,
                         @Query("end_date") endDate: String,
                         @Query("api_key") apiKey: String= BuildConfig.API_KEY):
            Response<APODListResponse>

    companion object {
        const val NASA_API_URL = "https://api.nasa.gov/planetary/"
    }
}
