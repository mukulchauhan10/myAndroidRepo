package com.example.roomdbapplication.Activity

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.list_item.view.*

class TaskAdapter(val taskList: List<Task>, val recyclerItemViewClick: RecyclerItemViewClick) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.list_item,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder.view) {
            if (taskList[position].tName != null)
                taskNameView.apply {
                    text = taskList[position].tName
                    visibility = View.VISIBLE
                }
            if (taskList[position].tTask != null)
                taskDescView.apply {
                    text = taskList[position].tTask
                    visibility = View.VISIBLE
                }
            if (taskList[position].tRemainderDate != null || taskList[position].tRemainderTime != null) {
                taskRemainderTextView.apply {
                    visibility = View.VISIBLE
                    text =
                        " ${taskList[position].tRemainderDate.toString()} | ${taskList[position].tRemainderTime.toString()}"
                }
            }
            setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    recyclerItemViewClick.onItemClick(position)
                }
            })
        }
    }

    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface RecyclerItemViewClick {
        fun onItemClick(position: Int)
    }
}