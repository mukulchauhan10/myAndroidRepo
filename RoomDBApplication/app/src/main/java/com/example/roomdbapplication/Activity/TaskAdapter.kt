package com.example.roomdbapplication.Activity

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.list_item.view.*

class TaskAdapter(val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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
        holder.view.taskNameView.text = taskList[position].tName
        holder.view.taskDescView.text = taskList[position].tDescription
        if(taskList[position].tRemainderDate != null || taskList[position].tRemainderTime != null ){
            holder.view.taskRemainderTextView.apply {
                visibility = View.VISIBLE
                text = " ${taskList[position].tRemainderDate.toString()} | ${taskList[position].tRemainderTime.toString()}"
            }
        }
    }

    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}