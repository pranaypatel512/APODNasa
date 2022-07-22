package com.pranay.apodnasa.model


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    var error: Error? = null
) {
    data class Error(
        @SerializedName("code")
        var code: String? = null,
        @SerializedName("message")
        var message: String? = null
    )
}