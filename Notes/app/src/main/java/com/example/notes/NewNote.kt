package com.example.notes

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.example.notes.database.Note
import kotlinx.android.synthetic.main.activity_new_note.*
import java.text.SimpleDateFormat
import java.util.*

class NewNote : AppCompatActivity(), TextWatcher {

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        doneBtn.isEnabled = true
        addRemainderBtn.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        titleEditText.addTextChangedListener(this)
        noteEditText.addTextChangedListener(this)
        addRemainderBtn.setOnClickListener {
            handleDate()
        }

        doneBtn.setOnClickListener {
            Application.notesList.add(
                NotesData(
                    titleEditText.text.toString().trim(),
                    noteEditText.text.toString().trim(),
                    dateInvisibleView.text.toString(),
                    timeInvisibleView.text.toString()
                )
            )

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    fun handleDate() {
        val dateFormat = SimpleDateFormat("dd MMM YYYY", Locale.US)
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // samajhna hai
                val select = Calendar.getInstance()
                select.set(Calendar.YEAR, year)
                select.set(Calendar.MONTH, month)
                select.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = dateFormat.format(select.time).toString()
                dateInvisibleView.text = date
                handleTime(date)

            },
            year, month, day
        )
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun handleTime(date: String) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val select = Calendar.getInstance()
                select.set(Calendar.HOUR, hourOfDay)
                select.set(Calendar.MINUTE, minute)

                val time = timeFormat.format(select.time).toString()
                timeInvisibleView.text = time
                addRemainderBtn.text = "$date \n $time"
            },
            now.get(Calendar.HOUR),
            now.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }


}