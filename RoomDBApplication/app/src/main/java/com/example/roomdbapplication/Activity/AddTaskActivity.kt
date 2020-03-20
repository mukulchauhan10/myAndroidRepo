package com.example.roomdbapplication.Activity

import android.app.*
import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.roomdbapplication.CoroutineJob
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

    val myCalendar by lazy {
        Calendar.getInstance()
    }
    var exactDate: String? = null
    var exactTime: String? = null
    lateinit var settableDateTime: String
    lateinit var dateTimeFormat: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        editedDateView.append(dateProvider())
        setSupportActionBar(toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_task_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                if (validation()) {
                    val taskName = taskNameEditText.text.toString().trim()
                    val taskDesc = taskDescriptionEditText.text.toString().trim()
                    val activationInfo = isRemainderActivate(exactDate, exactTime)
                    val taskDate = dateProvider()
                    lateinit var task: Task
                    launch {
                        val id = withContext(Dispatchers.IO) {
                            task = Task(
                                taskName,
                                taskDesc,
                                taskDate,
                                exactDate,
                                exactTime,
                                activationInfo
                            )
                            return@withContext TaskDatabase(this@AddTaskActivity).getDao()
                                .insertTask(task)
                        }
                        Toast.makeText(
                            this@AddTaskActivity,
                            "${task.tName} is saved",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    finish()
                }
            }
            R.id.setRemainder -> {
                dateDialog()
            }
        }
        return super.onOptionsItemSelected(item)
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
        val dateSetListener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateTimeFormat = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.US)
                settableDateTime = dateTimeFormat.format(myCalendar.time)
                exactDate = settableDateTime
                dateTimeTextView.text = "We will inform you on ${settableDateTime.toString()}"
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
                dateTimeTextView.append(" at ${settableDateTime}")
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
