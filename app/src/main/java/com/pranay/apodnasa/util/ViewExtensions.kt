package com.pranay.apodnasa.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


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
