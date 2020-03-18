package com.example.roomdbapplication.Activity

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.roomdbapplication.CoroutineJob
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : CoroutineJob() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        submitBtn.setOnClickListener {
            if (validation()) {
                val taskName = taskNameEditText.text.toString().trim()
                val taskDesc = taskDescriptionEditText.text.toString().trim()
                val taskDate = dateProvider()
                launch {
                    val task = Task(taskName, taskDesc, taskDate, true)
                    TaskDatabase(this@AddTaskActivity).getDao().insertTask(task)
                    Toast.makeText(
                        this@AddTaskActivity,
                        "${task.tName} is saved",
                        Toast.LENGTH_LONG
                    ).show()
                }
                finish()
            }
        }
    }

    private fun validation(): Boolean {
        val validation = if (taskNameEditText.emptyVerfication())
            false
        else !taskDescriptionEditText.emptyVerfication()
        return validation
    }

    private fun EditText.emptyVerfication(): Boolean {
        if (this.text.toString().trim().isEmpty()) {
            this.error = "Please enter ${this.hint}"
            this.requestFocus()
            return true
        } else
            return false
    }

    private fun dateProvider(): String {
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MMM-YYYY", Locale.US)
        val date = dateFormat.format(now.time).toString()
        return date
    }
    /*
    * AsycTask was a lenthy process, as well as old technology
    * so, that we always use Coroutines instead.
    */

}
