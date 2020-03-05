package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class NotesAdapter(val list: ArrayList<NotesData>, recyclerViewOnClick: RecyclerViewOnClick) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    lateinit var recyclerViewOnClick: RecyclerViewOnClick

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(myNote: NotesData) {
            itemView.headingText.text = myNote.title
            itemView.contentView.text = myNote.note
            itemView.dateView.text = myNote.date
            itemView.timeView.text = myNote.time
//            itemView.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                    recyclerViewOnClick.onItemClick(adapterPosition)
//                }
//            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val eachNotesData = list[position]
        holder.bind(eachNotesData)
    }
}