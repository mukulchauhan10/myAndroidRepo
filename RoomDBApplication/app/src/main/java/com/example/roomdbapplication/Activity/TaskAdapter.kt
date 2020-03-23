package com.example.roomdbapplication.Activity

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapplication.R
import com.example.roomdbapplication.database.Task
import kotlinx.android.synthetic.main.list_item.view.*

class TaskAdapter(val taskList: List<Task>, val recyclerItemViewClick: RecyclerViewOnClick) :
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

    @SuppressLint("SetTextI18n") // yuhi, alt + Enter
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder.view) {
            taskList[position].tName?.let {
                taskNameView.apply {
                    text = taskList[position].tName
                    visibility = View.VISIBLE
                }
            }
            taskList[position].tTask?.let {
                taskDescView.apply {
                    text = taskList[position].tTask
                    visibility = View.VISIBLE
                }
            }
            taskList[position].tRemainderDate?.let {
                taskRemainderTextView.apply {
                    visibility = View.VISIBLE
                    text = " ${taskList[position].tRemainderDate}"
                }
                taskList[position].tRemainderTime?.let {
                    taskRemainderTextView.append(" | ${taskList[position].tRemainderTime}")
                }
            }
            setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    recyclerItemViewClick.onItemClick(position)
                }
            })
            setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    recyclerItemViewClick.onLongItemClick(position)
                    return true
                }

            })
        }
    }

    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}