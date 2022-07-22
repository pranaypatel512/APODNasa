package com.pranay.apodnasa.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message:String,length:Int=Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(),message,length).show()
}
