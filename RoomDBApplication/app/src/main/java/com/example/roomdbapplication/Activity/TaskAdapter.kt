package com.example.roomdbapplication.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item.view.taskNameView

class TaskAdapter(val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    lateinit var mContext:Context
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
        holder.view.taskDateView.text = taskList[position].tCreationDate

    }

    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}