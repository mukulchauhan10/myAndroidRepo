package com.example.roomdbapplication

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdbapplication.Activity.AddTaskActivity
import com.example.roomdbapplication.Activity.BinActivity
import com.example.roomdbapplication.Activity.RecyclerViewOnClick
import com.example.roomdbapplication.Activity.TaskAdapter
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewOnClick {

    var taskList = arrayListOf<Task>()
    val taskAdapter = TaskAdapter(taskList, this)
    val db by lazy {
        TaskDatabase.buildDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val isNoteEmpty = intent.getBooleanExtra("isNoteEmpty", false)
        if (isNoteEmpty) {
            Log.i("position", "done")
            Snackbar.make(parentLayout, "Blank note discarded", Snackbar.LENGTH_LONG).show()
        }

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
            }
        })

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
            //val value = intent.getIntExtra("key", 0)

        }


    }

    private fun swipe() {
        val simpleCallbacks = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()
                    val icon: Bitmap

                    if (dX > 0) {
                        icon = BitmapFactory.decodeResource(resources, R.drawable.delete_icon)
                        paint.color = Color.parseColor((Color.WHITE).toString())
                        canvas.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )
                        canvas.drawBitmap(icon, )
                    }
                } else
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        db.getDao().deleteTask(taskAdapter.getItemId(position).toInt())
                        taskAdapter.notifyItemRemoved(position)
                    }
                    ItemTouchHelper.RIGHT -> {

                    }
                }
            }

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

    override fun onItemClick(position: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)

        intent.putExtra("isTaskOld", true)
        intent.putExtra("taskId", taskList[position].tID)
        intent.putExtra("taskTitle", taskList[position].tName)
        intent.putExtra("taskDesc", taskList[position].tTask)
        intent.putExtra("taskCreationDate", taskList[position].tEditDate)
        intent.putExtra("taskRemainderDate", taskList[position].tRemainderDate)
        intent.putExtra("taskRemainderTime", taskList[position].tRemainderTime)
        //intent.putExtra("taskActivation", taskList[position].tActivate)
        startActivity(intent)
    }

    override fun onLongItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}

