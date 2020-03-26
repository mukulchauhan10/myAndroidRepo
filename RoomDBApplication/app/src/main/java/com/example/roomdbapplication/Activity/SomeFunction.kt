package com.example.roomdbapplication.Activity

import android.content.Context
import android.graphics.Color
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

    fun View.showSnackbarWithUndoAction(content: String, myFunc: () -> Unit, duration: Int = 0){
        Snackbar.make(this,content,duration)
            .setTextColor(Color.BLACK)
            .setBackgroundTint(Color.WHITE)
            .setActionTextColor(Color.parseColor("#FF05B30E"))
            .setAction("UNDO") {
                myFunc()
            }
            .show()
    }
}
