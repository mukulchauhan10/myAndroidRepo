package com.example.toolbar2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MyViewHolder>(){

    private var data: List<String> = ArrayList()

    inner class MyViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(s: String)= with(itemView as TextView){
            text = s
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_expandable_list_item_1,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<String>){
        this.data = data
        notifyDataSetChanged()
    }
}