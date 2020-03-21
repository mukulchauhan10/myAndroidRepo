package com.example.roomdbapplication.Activity

import android.app.*
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.example.roomdbapplication.CoroutineJob
import com.example.roomdbapplication.MainActivity
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.coroutines.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        setSupportActionBar(toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        isTaskOld = intent.getBooleanExtra("isTaskOld", false)
        Log.i("position", "1")


        if (isTaskOld) {
            oldTId = intent.getIntExtra("taskId", 0)
            val oldTitle: String? = intent.getStringExtra("taskTitle")
            val oldTask: String? = intent.getStringExtra("taskDesc")
            val oldEditDate = intent.getStringExtra("taskCreationDate")
            val oldRemDate: String? = intent.getStringExtra("taskRemainderDate")
            val oldRemTime: String? = intent.getStringExtra("taskRemainderTime")

            taskNameEditText.setText(oldTitle)
            taskDescriptionEditText.setText(oldTask)
            editedDateView.append(oldEditDate); Log.i("position", "10")
            dateTimeTextView.text =
                "We will inform you on $oldRemDate at $oldRemTime"
        }else
            editedDateView.append(dateProvider())
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
                    }
                    finish()
                } else if (taskName.isNullOrEmpty() && taskDesc.isNullOrEmpty()) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("isNoteEmpty", true)
                    startActivity(intent)
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

    private fun Context.showToast(content: String) =
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
}
