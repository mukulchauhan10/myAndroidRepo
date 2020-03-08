package com.example.roomdbapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdbapplication.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        submitBtn.setOnClickListener {

        }
    }

    private fun validation(): Boolean{
        if (taskNameEditText.text.toString().trim().isEmpty()) {
            taskNameEditText.error = "please enter task name"
            taskNameEditText.requestFocus()
            return false
        }else if (taskDescriptionEditText.text.toString().trim().isEmpty()) {
            taskDescriptionEditText.error = "please enter task name"
            taskDescriptionEditText.requestFocus()
            return false
        }else if (taskCommentEditText.text.toString().trim().isEmpty()) {
            taskCommentEditText.error = "please enter task name"
            taskCommentEditText.requestFocus()
            return false
        }else {
            return true
        }
    }
}
