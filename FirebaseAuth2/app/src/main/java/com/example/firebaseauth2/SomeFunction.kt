package com.example.firebaseauth2

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object SomeFunction {
    fun Fragment.showToast(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()
    }

    fun Fragment.showSnackbar(content: String, duration: Int= 0){
        Snackbar.make(requireView(),content, duration).show()
    }
}