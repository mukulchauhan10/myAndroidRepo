package com.example.roomdbapplication

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdbapplication.Activity.AddTaskActivity
import com.example.roomdbapplication.Activity.BinActivity
import com.example.roomdbapplication.Activity.RecyclerViewOnClick
import com.example.roomdbapplication.Activity.SomeFunction.showSnackbar
import com.example.roomdbapplication.Activity.SomeFunction.showSnackbarWithUndoAction
import com.example.roomdbapplication.Activity.SomeFunction.showToast
import com.example.roomdbapplication.Activity.TaskAdapter
import com.example.roomdbapplication.database.Task
import com.example.roomdbapplication.database.TaskDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : CoroutineJob(), RecyclerViewOnClick {

    lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var simpleCallBacks: ItemTouchHelper.SimpleCallback
    var isDark = false
    var taskList = arrayListOf<Task>()
    val taskAdapter = TaskAdapter(taskList, this)

    val db by lazy {
        TaskDatabase.buildDatabase(this)
    }

    val fAuth by lazy {
        FirebaseAuth.getInstance()
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

        enable_Swipe_And_Drag()

        itemTouchHelper = ItemTouchHelper(simpleCallBacks)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        db.getDao().getAllTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                taskList.clear()
                taskList.addAll(it)
                taskAdapter.notifyDataSetChanged()
            }
        })

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        val isNoteEmpty = intent.getBooleanExtra("isNoteEmpty", false)
        if (isNoteEmpty) {
            parentLayout.showSnackbar("Blank note discarded")
        }
    }

    private fun enable_Swipe_And_Drag() {
        simpleCallBacks = object :
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(taskList, fromPosition, toPosition)
                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

                /*GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        return@withContext db.getDao().changeTaskPosition(fromPosition, toPosition)
                    }
                }*/
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedTask = taskList.get(position)
                val tempTaskName =
                    if (!taskList[position].tName.isNullOrEmpty())
                        taskList[position].tName.toString()
                    else
                        "Note"
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        taskList.removeAt(position)
                        taskAdapter.notifyItemRemoved(position)
                        return@withContext db.getDao().deleteTask(position.toLong())
                    }
                }
                recyclerView.showSnackbarWithUndoAction("$tempTaskName is deleted", {
                    taskList.add(position, deletedTask)
                    taskAdapter.notifyItemInserted(position)
                })
            }

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
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.delete_mipmap_small)
                        paint.color = Color.parseColor("#ffffff")
                        canvas.drawRect(
                            itemView.left.toFloat(), itemView.top.toFloat(),
                            itemView.left.toFloat() + dX, itemView.bottom.toFloat(), paint
                        )
                        canvas.drawBitmap(
                            icon,
                            itemView.left.toFloat(),
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                            paint
                        )
                    } else {
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.delete_mipmap_small)
                        paint.color = Color.parseColor("#ffffff")
                        canvas.drawRect(
                            itemView.right.toFloat() + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                        )
                        canvas.drawBitmap(
                            icon,
                            itemView.right.toFloat() - icon.width,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                            paint
                        )
                    }
                    viewHolder.itemView.translationX = dX
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchButton = menu?.findItem(R.id.search)
        searchBox(searchButton!!)
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchBox(searchButton: MenuItem) {
        val searchView: SearchView = searchButton.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    displaySearchResult(newText)
                    return true
                } ?: return false
            }
        })
    }

    private fun displaySearchResult(newText: String) {
        db.getDao().getAllTask().observe(this, Observer {
            if (it.isNotEmpty()) {
                taskList.clear()
                taskList.addAll(
                    it.filter {
                        with(it) {
                            tName!!.contains(newText, true) or tTask!!.contains(newText, true)
                        }
                    }
                )
                taskAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val checkable = menu?.findItem(R.id.darkMode)
        checkable?.setChecked(isDark)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bin -> {
                startActivity(Intent(this, BinActivity::class.java))
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
        startActivity(intent)
    }

    override fun onLongItemClick(position: Int) {
        this.showToast("clicked")
    }
}
