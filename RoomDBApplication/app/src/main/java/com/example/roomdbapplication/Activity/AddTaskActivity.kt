package com.example.roomdbapplication.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.roomdbapplication.Activity.SomeFunction.showToast
import com.example.roomdbapplication.CoroutineJob
import com.example.roomdbapplication.MainActivity
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
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

    var oldTId: Int? = null
    var isTaskOld: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        setSupportActionBar(newNoteToolbar) // back button is toolbar ke liye hi app

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        isTaskOld = intent.getBooleanExtra("isTaskOld", false)
        if (isTaskOld) {
            oldTId = intent.getIntExtra("taskId", 0)
            val oldTitle: String? = intent.getStringExtra("taskTitle")
            val oldTask: String? = intent.getStringExtra("taskDesc")
            val oldEditDate = intent.getStringExtra("taskCreationDate")
            val oldRemDate: String? = intent.getStringExtra("taskRemainderDate")
            val oldRemTime: String? = intent.getStringExtra("taskRemainderTime")

            taskNameEditText.setText(oldTitle)
            taskDescriptionEditText.setText(oldTask)
            editedDateView.text = "Last Edited: $oldEditDate"
            oldRemDate?.let {
                lineImage.visibility = View.VISIBLE
                dateTimeTextView.text = "Alert: $oldRemDate"
                oldRemTime?.let { dateTimeTextView.append(" at $oldRemTime") }
            }
        } else
            editedDateView.text = "Last Edited: ${currentDateProvider()}"

        setRemainderFloatingButton.setOnClickListener {
            dateDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_task_menu, menu)
        menuInflater.inflate(R.menu.bottom_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val taskName: String?
        val taskDesc: String?
        when (item.itemId) {

            R.id.done -> {
                taskName = taskNameEditText.text?.toString()?.trim()
                taskDesc = taskDescriptionEditText.text?.toString()?.trim()
                val activationInfo: Boolean = isRemainderActivate(exactDate, exactTime)
                val taskDate: String = currentDateProvider()
                lateinit var task: Task
                if (!taskName.isNullOrEmpty() || !taskDesc.isNullOrEmpty()) {
                    if (isTaskOld) {
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
                            this@AddTaskActivity.showToast("$taskName is updated")
                        }
                        finish()
                    } else {
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
                                return@withContext db.getDao().insertTask(task)
                            }
                            this@AddTaskActivity.showToast("${task.tName} is saved")
                        }
                        finish()
                    }
                } else if (taskName.isNullOrEmpty() && taskDesc.isNullOrEmpty()) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("isNoteEmpty", true)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.share -> {
                taskName = taskNameEditText.text?.toString()?.trim()
                taskDesc = taskDescriptionEditText.text?.toString()?.trim()
                shareNote(taskName, taskDesc)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareNote(taskName: String?, taskDesc: String?) {
        Log.i("myDebug", "1")
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, taskName)

        if (taskName!!.isNotEmpty() or taskDesc!!.isNotEmpty()) {
            when {
                taskName.isEmpty() ->
                    sendIntent.putExtra(Intent.EXTRA_TEXT, taskDesc)
                taskDesc.isEmpty() ->
                    sendIntent.putExtra(Intent.EXTRA_TEXT, taskName)
                taskName.isNotEmpty() and taskDesc.isNotEmpty() ->
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "*$taskName*\n$taskDesc")
            }
            startActivity(shareIntent)
        }

        /*if(!(taskName.isNullOrBlank() || taskDesc.isNullOrBlank())){
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "*$taskName*\n$taskDesc")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, taskName)

            startActivity(shareIntent)
        }

        taskName?: run {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "*$taskName*\n$taskDesc")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, taskName)

            startActivity(shareIntent)
        }*/
    }

    private fun currentDateProvider(): String {
        val myCalendar = Calendar.getInstance()
        dateTimeFormat = SimpleDateFormat("dd-MMM-YYYY", Locale.US)
        val date = dateTimeFormat.format(myCalendar.time).toString()
        return date
    }
    /*
    * AsycTask was a lenthy process, as well as old technology
    * so, that we always use Coroutines instead.
    */

    private fun dateDialog() {
        //val myCalendar = Calendar.getInstance()
        val dateSetListener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                dateTimeFormat = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.US)
                settableDateTime = dateTimeFormat.format(myCalendar.time)
                exactDate = settableDateTime
                dateTimeTextView.text = "Alert: $settableDateTime"
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
