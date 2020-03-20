package com.example.roomdbapplication

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdbapplication.Activity.AddTaskActivity
import com.example.roomdbapplication.Activity.BinActivity
import com.example.roomdbapplication.Activity.TaskAdapter
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var taskList = arrayListOf<Task>()
    val taskAdapter = TaskAdapter(taskList)
    val db by lazy {
        TaskDatabase.buildDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = this@MainActivity.taskAdapter // use this: taskAdapter
        }

        db.getDao().getAllTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                taskList.clear()
                taskList.addAll(it)
                taskAdapter.notifyDataSetChanged()
            } else {
                taskList.clear()
                taskAdapter.notifyDataSetChanged()
            }
        })

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bin -> {
                startActivity(Intent(this, BinActivity::class.java))
            }
            R.id.search -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

