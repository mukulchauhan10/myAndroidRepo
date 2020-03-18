package com.example.roomdbapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdbapplication.Activity.AddTaskActivity
import com.example.roomdbapplication.Activity.BinActivity
import com.example.roomdbapplication.Activity.RecyclerViewOnClick
import com.example.roomdbapplication.Activity.TaskAdapter
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val b: Int = 5
class MainActivity : AppCompatActivity() {
    lateinit var taskList: List<Task>

    private lateinit var taskAdapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        }

        /*GlobalScope.launch(Dispatchers.Main) {
            taskList = TaskDatabase(this@MainActivity).getDao().getAllTask()
            taskAdapter = TaskAdapter(taskList)
            recyclerView.adapter = taskAdapter
            taskAdapter.notifyDataSetChanged()
        }*/


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
            R.id.search ->{}
        }
        return super.onOptionsItemSelected(item)
    }
}

