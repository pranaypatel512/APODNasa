package com.pranay.apodnasa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pranay.apodnasa.model.APODPictureItem.Companion.TABLE_NAME

/**
 * API response item and database table for storing media items with details
 */
@Entity(tableName = TABLE_NAME)
data class APODPictureItem(
    @SerializedName("url")
    @PrimaryKey
    var url: String,
    @SerializedName("copyright")
    var copyright: String? = null,
    @SerializedName("date")
    var date: String? = null,
    @SerializedName("explanation")
    var explanation: String? = null,
    @SerializedName("hdurl")
    var hdurl: String? = null,
    @SerializedName("media_type")
    var mediaType: String? = null,
    @SerializedName("service_version")
    var serviceVersion: String? = null,
    @SerializedName("thumbnail_url")
    @ColumnInfo(name = "thumbnailUrl")
    var thumbnailUrl: String? = null,
    @SerializedName("title")
    var title: String? = null
) {
    companion object {
        const val TABLE_NAME = "APODList"
    }

    fun isVideo(): Boolean {
        return mediaType == MediaType.video.toString()
    }
}