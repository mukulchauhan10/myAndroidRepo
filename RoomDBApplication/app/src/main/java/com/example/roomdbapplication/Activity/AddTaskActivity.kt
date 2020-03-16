package com.example.roomdbapplication.Activity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.DataBaseClient
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        submitBtn.setOnClickListener {
            if (validation()) {

                val taskName = taskNameEditText.text.toString().trim()
                val taskDesc = taskDescriptionEditText.text.toString().trim()
                val taskCmt = taskCommentEditText.text.toString().trim()
                val taskDate = dateProvider()
                val task = Task(taskName, taskDesc, taskDate, taskCmt, true)
                saveTask(task)
            }
        }
    }

    private fun validation(): Boolean {
        val validation = if (taskNameEditText.emptyVerfication())
            false
        else if (taskDescriptionEditText.emptyVerfication())
            false
        else !taskCommentEditText.emptyVerfication()
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

    private fun saveTask(task: Task) {
        /*
        * Main thread doesn't allow DB operation, because DB operation takes long time,
          and due to such problems our main thread can be block.

        * So, to overcome this problem we use AsyncTask means Asynchronized Tasking
        */


        class SaveTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                DataBaseClient.getInstance(applicationContext)?.taskDatabase?.setDao()?.insertTask(task)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(this@AddTaskActivity, "${task.tName} is saved", Toast.LENGTH_LONG).show()
            }
        }
        SaveTask().execute()
    }
}
