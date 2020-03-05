package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notes.database.Note
import com.example.notes.database.NoteDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.android.synthetic.main.list_item.*

class MainActivity : AppCompatActivity(), RecyclerViewOnClick {
    val db by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, "Note DB")
            .fallbackToDestructiveMigration()
            .build()
    }
    val myNotesAdapter = NotesAdapter(Application.notesList, this)


    val dataAdded = MutableLiveData<Boolean>()
    val msg = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addbtn.setOnClickListener {
            val intent = Intent(this, NewNote::class.java)
            startActivity(intent)
            finish()
        }



        recycleView.apply {
            layoutManager =
                GridLayoutManager(this@MainActivity,2, GridLayoutManager.VERTICAL, false)
            adapter = myNotesAdapter
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this,EditActivity::class.java)
        intent.putExtra("title",headingText.toString())
        intent.putExtra("note",contentView.toString())
        intent.putExtra("date",dateView.toString())
        intent.putExtra("time",timeView.toString())
        startActivity(intent)

    }

}
