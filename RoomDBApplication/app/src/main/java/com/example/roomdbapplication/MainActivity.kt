package com.example.roomdbapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdbapplication.Activity.AddTaskActivity
import com.example.roomdbapplication.Activity.TaskAdapter
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        }

        GlobalScope.launch(Dispatchers.Main) {
            val taskList = TaskDatabase(this@MainActivity).getDao().getAllTask()
            taskAdapter = TaskAdapter(taskList)
            recyclerView.adapter = taskAdapter
            taskAdapter.notifyDataSetChanged()
        }


        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }
}
