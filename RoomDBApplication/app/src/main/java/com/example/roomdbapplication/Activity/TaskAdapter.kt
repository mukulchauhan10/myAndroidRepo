package com.example.roomdbapplication.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.list_item.view.*

class TaskAdapter(val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item,
                    parent,
                    false)
        )
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.view.taskNameView.text = taskList[position].tName
        holder.view.taskDescView.text = taskList[position].tDescription
        holder.view.taskDateView.text = taskList[position].tCreationDate
    }

    inner class TaskViewHolder(val view: View): RecyclerView.ViewHolder(view)
}