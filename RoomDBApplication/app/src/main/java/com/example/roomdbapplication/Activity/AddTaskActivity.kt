package com.example.roomdbapplication.Activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.example.roomdbapplication.CoroutineJob
import com.example.roomdbapplication.MainActivity
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "noteDatabase1"

class AddTaskActivity : CoroutineJob() {

    val db by lazy { TaskDatabase.buildDatabase(this) }
    val myCalendar by lazy { Calendar.getInstance() }
    var exactDate: String? = null
    var exactTime: String? = null
    lateinit var settableDateTime: String
    lateinit var dateTimeFormat: SimpleDateFormat

    var task1: Task? = null
    var oldTId: Int? = null
    var oldTitle: String? = null
    var oldTask: String? = null
    var oldEditDate: String? = null
    var oldRemDate: String? = null
    var oldRemTime: String? = null
    var oldActivation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        editedDateView.append(dateProvider())
        setSupportActionBar(toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        oldTId = intent.getIntExtra("taskId", 1)
        oldTitle = intent.getStringExtra("taskTitle")
        oldTask = intent.getStringExtra("taskDesc")
        oldEditDate = intent.getStringExtra("taskCreationDate")
        oldRemDate = intent.getStringExtra("taskRemainderDate")
        oldRemTime = intent.getStringExtra("taskRemainderTime")
        oldActivation = intent.getBooleanExtra("taskActivation", false)
        taskNameEditText.setText(oldTitle)
        taskDescriptionEditText.setText(oldTask)
        editedDateView.append(oldEditDate)
        dateTimeTextView.text = "We will inform you on $oldRemDate at $oldRemTime"
        task1 =
            Task(oldTitle, oldTask, oldEditDate!!, oldRemDate, oldRemTime, oldActivation, oldTId!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_task_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                val taskName: String? = taskNameEditText.text?.toString()?.trim()
                val taskDesc: String? = taskDescriptionEditText.text?.toString()?.trim()
                val activationInfo: Boolean = isRemainderActivate(exactDate, exactTime)
                val taskDate: String = dateProvider()
                lateinit var task: Task
                if (!taskName.isNullOrEmpty() || !taskDesc.isNullOrEmpty()) {
                    oldTId?.let {
                        launch {
                            withContext(Dispatchers.IO) {
                                return@withContext db.getDao().updateTask(
                                    taskName,
                                    taskDesc,
                                    taskDate,
                                    exactDate,
                                    exactTime,
                                    activationInfo,
                                    oldTId!!
                                )
                            }
                            Toast.makeText(
                                this@AddTaskActivity,
                                "$taskName is updated",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }?
                }
                 if (!taskName.isNullOrEmpty() || !taskDesc.isNullOrEmpty()) {

                    launch {
                        withContext(Dispatchers.IO) {
                            task = Task(
                                taskName,
                                taskDesc,
                                taskDate,
                                exactDate,
                                exactTime,
                                activationInfo
                            )
                            return@withContext db.getDao().insertTask(task!!)
                        }
                        Toast.makeText(
                            this@AddTaskActivity,
                            "${task!!.tName} is saved",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    finish()
                } else if (taskName.isNullOrEmpty() && taskDesc.isNullOrEmpty()) {
                    Log.i("position", "process")
                    Intent().putExtra("key", 1)
                    finish()
                }
            }
            R.id.setRemainder -> {
                dateDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dateProvider(): String {
        dateTimeFormat = SimpleDateFormat("dd-MMM-YYYY", Locale.US)
        val date = dateTimeFormat.format(myCalendar.time).toString()
        return date
    }
    /*
    * AsycTask was a lenthy process, as well as old technology
    * so, that we always use Coroutines instead.
    */

    private fun dateDialog() {
        val dateSetListener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateTimeFormat = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.US)
                settableDateTime = dateTimeFormat.format(myCalendar.time)
                exactDate = settableDateTime
                dateTimeTextView.text = "We will inform you on $settableDateTime"
                lineImage.visibility = View.VISIBLE
                timeDialog()
            }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis()  // previous date will not visible from current date
        datePickerDialog.show()
    }

    private fun timeDialog() {
        val timesetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, minute)
                dateTimeFormat = SimpleDateFormat("hh:mm a", Locale.US)
                settableDateTime = dateTimeFormat.format(myCalendar.time)
                exactTime = settableDateTime
                dateTimeTextView.append(" at $settableDateTime")
            }
        val timePickerDialog = TimePickerDialog(
            this,
            timesetListener,
            myCalendar.get(Calendar.HOUR),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun isRemainderActivate(date: String?, time: String?): Boolean =
        date != null || time != null
}
