package com.pranay.apodnasa.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pranay.apodnasa.model.APODPictureItem.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class APODPictureItem(
    @SerializedName("url")
    @PrimaryKey
    var url: String? = null,
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
    @SerializedName("title")
    var title: String? = null
) {
    companion object {
        const val TABLE_NAME = "APODList"
    }
}