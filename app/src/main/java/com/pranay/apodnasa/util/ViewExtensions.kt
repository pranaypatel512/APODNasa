package com.pranay.apodnasa.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.regex.Matcher
import java.util.regex.Pattern


fun Fragment.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, length).show()
}

/**
 * Hide View
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Show View
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * common functions that gives video id from given youtube URL
 */
fun String.getVideoId(): String? {
    val videoId: String?
    val regex =
        "https?://(?:m.)?(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?(?:feature=youtu.be&)?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\\n]+)"
    val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(this)
    videoId = if (matcher.find()) {
        matcher.group(1)
    } else {
        null
    }
    return videoId
}
