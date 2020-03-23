package com.example.roomdbapplication.Activity

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object SomeFunction {

    fun Context.showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    fun View.showSnackbar(content: String, duration: Int = 0) {
        Snackbar.make(this, content, duration).show()
    }
}
