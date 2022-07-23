package com.pranay.apodnasa.model


import com.google.gson.annotations.SerializedName
//{"code":400,"msg":"Date must be between Jun 16, 1995 and Jul 22, 2022.","service_version":"v1"}
data class ErrorResponse(
    @SerializedName("error")
    var error: Error? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("msg")
    var message: String? = null
) {
    data class Error(
        @SerializedName("code")
        var code: String? = null,
        @SerializedName("message")
        var message: String? = null
    )
}