package com.pranay.apodnasa.util

import android.util.Log
import com.pranay.apodnasa.BuildConfig

/**
 * This is [Logger] to handle debug logs
 */
object Logger {

    fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message)
    }
}